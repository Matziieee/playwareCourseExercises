package com.playware.exercise2.project;

//todo rename
public class TileClick {
    int tile;
    long timeVisibleMs;
    long timeToPressMs;
    int color;

    public TileClick(int tile, long timeVisibleMs, int color, long timeToPressMs) {
        this.tile = tile;
        this.timeVisibleMs = timeVisibleMs;
        this.timeToPressMs = timeToPressMs;
        this.color = color;
    }

}
