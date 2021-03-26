package com.example.colortiles;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Arrays;
import java.util.List;


public class Card {

    int color;
    float x, y, width, height;
    static List<Integer> colors = Arrays.asList(
            R.color.tileColorR, R.color.tileColorG,
            R.color.tileColorB);

    public Card(float x, float y, float width, float height, int color) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean touched(float touchX, float touchY) {
        return touchX >= x && touchX <= x + width && touchY >= y && touchY <= y + height;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void draw(Canvas c, Resources res) {
        Paint p = new Paint();
        p.setColor(res.getColor(color));
        c.drawRoundRect(x, y, x + width, y + height, 25, 25, p);
    }

    public void nextColor() {
        int nextColor = colors.indexOf(color);
        nextColor = (nextColor + 1) % colors.size();
        color = colors.get(nextColor);
    }

}
