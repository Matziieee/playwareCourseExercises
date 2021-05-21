package com.playware.exercise2.project;

import android.os.Build;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.livelife.motolibrary.AntData;
import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.MotoSound;

import java.util.ArrayList;
import java.util.Random;


import static com.livelife.motolibrary.AntData.CMD_COUNTDOWN_TIMEUP;
import static com.livelife.motolibrary.AntData.EVENT_PRESS;
import static com.livelife.motolibrary.AntData.LED_COLOR_BLUE;
import static com.livelife.motolibrary.AntData.LED_COLOR_GREEN;
import static com.livelife.motolibrary.AntData.LED_COLOR_OFF;
import static com.livelife.motolibrary.AntData.LED_COLOR_RED;

public class MindGame extends Game {

    protected long BASE_TIME_VISIBLE_MS = 2000; // Each tile is visible for 3 seconds
    protected long BASE_TIME_TO_CLICK_MS = 1000; //User has 100 seconds to click each tile, effectively making it not matter. Intended for this game mode.
    /*
    Uncomment when using tiles*/
    MotoConnection motoConnection;
    MotoSound sound = MotoSound.getInstance(); /**/

    ArrayList<Level> levels;
    Level currentLevel;
    TileClick currentTileClick;
    TileClick currentTileShown;

    boolean isGameStarted = false; //Should be flipped after showing user what pattern they must click.
    boolean hasPressed = false;
    boolean isCorrectPressed = false;
    boolean shouldClear = false;
    boolean isGameOver = false;
    boolean startGame = false;

    public Random rand = new Random();

    Handler handler = new Handler();

    int lastColor = -1;

    public MindGame(){
        setName("Mind Game");
        setDescription("something something");
        //Set scores unreasonably high because we don't use this at all.
        addGameType(new GameType(0, GameType.GAME_TYPE_SCORE, 100000, "Normal",1));
        addGameType(new GameType(1, GameType.GAME_TYPE_SCORE, 100000, "Hard",1));
        addGameType(new GameType(2, GameType.GAME_TYPE_SCORE, 100000, "Normal Time Attack",1));
        addGameType(new GameType(3, GameType.GAME_TYPE_SCORE, 100000, "Hard Time Attack",1));
        levels = new ArrayList<>();
        motoConnection = MotoConnection.getInstance();
    }

    @Override
    public void onGameStart() {
        super.onGameStart();
    }

    @Override
    public void onGameUpdate(byte[] message) {
        super.onGameUpdate(message);
        if(!isGameStarted || startGame) return;
        int id = AntData.getId(message);
        int event = AntData.getCommand(message);
        if(event == EVENT_PRESS){
            hasPressed = true;
            handlePress(id);
        }
    }

    @Override
    public void onGameEnd() {
        super.onGameEnd();
        isGameOver = true;
    }


    public void handlePress(int tile){
        System.out.println("TILE CLICKED : " + tile);
        System.out.println("SHOULD BE: "  +  currentTileClick.tile);
        if(tile == currentTileClick.tile){
            this.isCorrectPressed =  true;
        }
        else{
            //handle failure
            endGame();
        }
    }

    private void generateLevel(){
        //Create level
        Level l = new Level(levels.size()+1);
        for(int i = 0; i < l.size; i++){
            l.tileClicks.add(generateTileClick());
        }
        levels.add(l);
        currentLevel = l;
    }

    protected void showLevel(Level level){
        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                motoConnection.setAllTilesIdle(0);
                if(level.currentClickNum == level.size){
                    currentTileShown.color = 0;
                    currentTileShown = null;
                    playLevel(currentLevel);
                    return;
                }
                if(currentTileShown!= null){
                    currentTileShown.color = 0;
                }
                currentTileShown = level.tileClicks.get(level.currentClickNum);
                //Only set color if we are in normal mode.
                if(selectedGameType.getTypeId() == 0 || selectedGameType.getTypeId() == 2){
                    motoConnection.setTileColor(currentTileShown.color, currentTileShown.tile);
                }
                level.currentClickNum++;
                handler.postDelayed(this, currentTileShown.timeVisibleMs);
            }
        }, 0);
    }

    protected void playLevel(Level currentLevel){
        // Game loop runnable
        isGameStarted = true;
        sound.playStart();
        currentTileClick = currentLevel.tileClicks.get(0);
        setGameOngoingTileColor();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //If level is still being shown to user, do nothing.
                /*if(!isGameStarted || startGame){
                    handler.postDelayed(this, 100);
                    return;
                }*/
                //wait for user to press...
                if(!hasPressed && !isCorrectPressed){
                    handler.postDelayed(this, 10);
                    return;
                }

                sound.playMatched();
                hasPressed = false;
                isCorrectPressed = false;

                //increment score and expect next input
                incrementScore();

                //if correct press was last in level
                if(currentLevel.tileClicks.indexOf(currentTileClick) == currentLevel.tileClicks.size()-1){
                    finishLevel();
                    return;
                }
                //make next tile new target
                currentTileClick = currentLevel.tileClicks.get(currentLevel.tileClicks.indexOf(currentTileClick)+1);
                setGameOngoingTileColor();
                handler.postDelayed(this, 0);
            }
        },0);
    }

    protected void finishLevel(){
        startGame = true;
        motoConnection.setAllTilesColor(LED_COLOR_OFF);
        sound.speak("PRESS BUTTON TO CONTINUE");
    }

    private void incrementScore(){
        this.incrementPlayerScore(1, 0);
    }

    protected void setGameOngoingTileColor(){
        motoConnection.setAllTilesColor(LED_COLOR_GREEN);
    }

    protected TileClick generateTileClick(){
       return new TileClick(getNextTile(), BASE_TIME_VISIBLE_MS, getNextColor(), BASE_TIME_TO_CLICK_MS);

    }

    protected int getNextTile(){
        return rand.nextInt(4)+1;
    }

    protected int getNextColor(){
        /*
         * Colors
         * RED == 1
         * BLUE == 2
         * GREEN == 3
         * INDIGO == 4

        */
        int newColor = rand.nextInt(4)+1;

        while(newColor  == lastColor){
            newColor = rand.nextInt(4)+1;
        }
        System.out.println("COLOR: " + newColor);
        lastColor = newColor;
        return newColor;
    }

    protected void advanceGame(){
        sound.speak("NEXT LEVEL");
        this.shouldClear = true;
        handler.removeCallbacksAndMessages(null);
        this.isGameStarted = false;
        this.generateLevel();
        showLevel(this.currentLevel);
    }

    protected void endGame(){
        sound.speak("Game over");
        handler.removeCallbacksAndMessages(null);
        motoConnection.setAllTilesIdle(LED_COLOR_RED);
        for (TileClick tc : currentLevel.tileClicks) {
            tc.color = 0;
        }
        this.stopGame();
    }

}
