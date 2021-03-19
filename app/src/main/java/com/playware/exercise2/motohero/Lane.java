package com.playware.exercise2.motohero;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

public class Lane implements Renderable{
    float startX;
    float width;
    int color;
    private ArrayList<Note> notes = new ArrayList<>();

    public Lane(float startX, float width, int color){
        this.startX = startX;
        this.width = width;
        this.color = color;
    }

    public void addNote(Note note){
        this.notes.add(note);
    }

    public ArrayList<Note> getNotes(){
        return this.notes;
    }

    @Override
    public void render(Canvas canvas) {
        //draw a transparent rectangle
        Paint p = new Paint();
        p.setColor(this.color);
        p.setStyle(Paint.Style.FILL);
        p.setAlpha(50);
        Rect r = new Rect((int)startX,0,(int)width,canvas.getHeight());
        canvas.drawRect(r,p);
    }
}
