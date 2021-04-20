package com.playware.exercise2.project;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class ColorView extends View {
    private int currentColor;

    public ColorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        currentColor = Color.TRANSPARENT;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        //Borders
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(1.5f);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0,0,getWidth(),getHeight(), paint);
        //background
        this.setBackgroundColor(currentColor);
    }

    public int getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(int currentColor) {
        this.currentColor = currentColor;
    }
}
