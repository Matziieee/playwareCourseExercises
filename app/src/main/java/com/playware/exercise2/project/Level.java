package com.playware.exercise2.project;

import java.util.ArrayList;

public class Level {
    public int size;
    public int currentClickNum = 0;
    ArrayList<TileClick> tileClicks = new ArrayList<>();

    public Level(int size) {
        this.size = size;
    }


}
