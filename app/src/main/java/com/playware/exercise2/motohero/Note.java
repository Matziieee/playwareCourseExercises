package com.playware.exercise2.motohero;

import android.graphics.Canvas;

//The note/tile which has to be clicked
public class Note implements Renderable{
    float x;
    float y;
    boolean isPressable = false;
    int color;

    public Note(float x, float y, int color){
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public void onClicked(){
        //Do something
    }

    @Override
    public void render(Canvas canvas) {

    }
}
