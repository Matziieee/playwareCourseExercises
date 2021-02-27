package com.playware.exercise2.beergame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;

import com.playware.exercise2.shared.GameView;

public class BeerGameView extends GameView {

    public BeerGameView(Context context, AttributeSet as) {
        super(context, as);
    }

    @Override
    protected void step() {

    }

    @Override
    protected void render(Canvas canvas) {
        canvas.drawColor(Color.RED);
    }

    private void renderLanes(Canvas canvas){

    }
}
