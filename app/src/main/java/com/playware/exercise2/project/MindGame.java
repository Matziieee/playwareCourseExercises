package com.playware.exercise2.project;

import com.livelife.motolibrary.AntData;
import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.MotoSound;

import java.util.ArrayList;
import java.util.Random;

import static com.livelife.motolibrary.AntData.CMD_COUNTDOWN_TIMEUP;
import static com.livelife.motolibrary.AntData.EVENT_PRESS;
import static com.livelife.motolibrary.AntData.LED_COLOR_GREEN;
import static com.livelife.motolibrary.AntData.LED_COLOR_OFF;

public class MindGame extends Game {
    /*
    Uncomment when using tiles
    MotoConnection motoConnection = MotoConnection.getInstance();
    MotoSound sound = MotoSound.getInstance(); */

    ArrayList<Level> levels;
    Level currentLevel;
    TileClick currentTileClick;

    //init random with a static seed for testing purposes. todo remove seed
    Random rand = new Random(123);

    public MindGame(){
        setName("Mind Game");
        setDescription("something something");
        addGameType(new GameType(0, GameType.GAME_TYPE_SCORE, 100000, "No time limit",1));
        levels = new ArrayList<>();
    }

    @Override
    public void onGameStart() {
        super.onGameStart();
        advanceGame();
    }

    @Override
    public void onGameUpdate(byte[] message) {
        super.onGameUpdate(message);

    }

    @Override
    public void onGameEnd() {
        super.onGameEnd();

    }

    protected void advanceGame(){
        //Start case
        if(currentLevel == null){
            nextLevel();
            return;
        }

        //Advance tileClick case
        if(currentTileClick == null || levels.size() < currentLevel.size){
            nextTileClick();
        }

        //Advance level case
        else{
            nextLevel();
        }
    }

    protected void nextLevel(){
        //Create a new level, with number = size + 1 of current list. Thus it starts at one and always increments.
        Level l = new Level(levels.size());
        levels.add(l);
        currentLevel = l;
    }

    protected void nextTileClick(){
        TileClick tc = new TileClick(getNextTile(), getNextTime(), getNextColor());
        currentLevel.tileClicks.add(tc);
        currentTileClick = tc;
    }

    protected int getNextTile(){
        return rand.nextInt(3);
    }

    protected double getNextTime(){
        return 1000;
    }

    protected int getNextColor(){
        /*
         * Colors
         * RED == 1
         * BLUE == 2
         * GREEN == 3
         * INDIGO == 4
        */
        return rand.nextInt(3)+ 1;
    }
}
