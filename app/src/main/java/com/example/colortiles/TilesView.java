package com.example.colortiles;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TilesView extends View {

    int displayWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

    ArrayList<Card> cards = new ArrayList<>();

    public TilesView(Context context) {
        super(context);
    }

    public TilesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setColorsAndTiles();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Card c : cards) {
            c.draw(canvas, getResources());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            for (Card c : cards) {
                if (c.touched(x, y)) {
                    int currentIndex = cards.indexOf(c);
                    c.nextColor();
                    affectNear(currentIndex);

                    invalidate();
                    if (checkWin()) {
                        handleWin();
                    }
                    return true;
                }
            }
        }
        invalidate();
        return true;
    }

    public boolean checkWin() {
        Card firstCard = cards.get(0);
        int counter = 0;
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).color == firstCard.color) counter++;
        }
        return counter == 16;
    }

    public void handleWin() {
        Toast toast = Toast.makeText(getContext(),
                "Победа!", Toast.LENGTH_SHORT);
        toast.show();
        newGame();
    }


    public void setColorsAndTiles() {
        float tmpWidth = displayWidth / 5;
        float tmpHeight = displayWidth / 5;
        float tmpX = tmpWidth / 3;
        float tmpY = tmpWidth / 3;

        List<Integer> colors = Card.colors;
        for (int i = 0; i < 16; i++) {
            if (i % 3 == 0) {
                Collections.shuffle(colors);
            }
            cards.add(new Card(tmpX, tmpY, tmpWidth, tmpHeight, colors.get(i % 3)));

            tmpX += tmpWidth + tmpWidth / 10;
            if (((i + 1) % 4) == 0) {
                tmpX = tmpWidth / 3;
                tmpY += tmpWidth + tmpWidth / 10;
            }
        }
    }


    public void newGame() {
        cards.clear();
        setColorsAndTiles();
        invalidate();
    }

    public void affectNear(int currentIndex) {
        ArrayList<Integer> nextIndexes = new ArrayList<>();
        int x = currentIndex % 4;
        int y = currentIndex / 4;
        if (x - 1 >= 0) {
            nextIndexes.add(currentIndex - 1);
        }
        if (x + 1 < 4) {
            nextIndexes.add(currentIndex + 1);
        }
        if (y - 1 >= 0) {
            nextIndexes.add(currentIndex - 4);
        }
        if (y + 1 < 4) {
            nextIndexes.add(currentIndex + 4);
        }

        for (Integer n : nextIndexes) {
            cards.get(n).nextColor();
        }
        nextIndexes.clear();
    }

}