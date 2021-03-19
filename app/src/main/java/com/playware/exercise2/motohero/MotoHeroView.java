package com.playware.exercise2.motohero;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;

import com.playware.exercise2.shared.GameView;

public class MotoHeroView extends GameView {
    MotoHero motoHero;

    public MotoHeroView(Context context, AttributeSet as) {
        super(context, as);
        motoHero = new MotoHero(screenWidth, screenHeight);
    }

    @Override
    protected void step() {

    }

    @Override
    protected void render(Canvas canvas) {
        motoHero.render(canvas);
    }
}
