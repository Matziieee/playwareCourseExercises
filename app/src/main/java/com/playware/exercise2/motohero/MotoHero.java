package com.playware.exercise2.motohero;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

public class MotoHero implements Renderable {
    int screenWidth;
    int screenHeight;
    float laneWidth;
    ArrayList<Lane> lanes = new ArrayList<>();


    public MotoHero(int screenWidth, int screenHeight){
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.laneWidth = (float) screenWidth/3;
        init();
    }

    private void init(){
        //Left/red lane
        Lane l = new Lane(0,laneWidth, Color.RED);
        //Middle/blue lane
        Lane m = new Lane(laneWidth,laneWidth,Color.BLUE);
        //Right/green lane
        Lane r = new Lane(laneWidth*2, laneWidth, Color.GREEN);

        this.lanes.add(l);
        this.lanes.add(m);
        this.lanes.add(r);

    }

    @Override
    public void render(Canvas canvas) {
        //Render lanes
        for (Lane l: this.lanes) {
            Paint p = new Paint();
            p.setColor(l.color);
            p.setStyle(Paint.Style.FILL);
            //p.setAlpha(50);
            Rect r = new Rect((int)l.startX,5,(int)l.width,canvas.getHeight());
            canvas.drawRect(r,p);
        }
    }
}
