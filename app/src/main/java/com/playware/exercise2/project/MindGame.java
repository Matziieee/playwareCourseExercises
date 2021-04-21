package com.playware.exercise2.project;

import android.graphics.Color;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.RequiresApi;

import com.livelife.motolibrary.AntData;
import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.MotoSound;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import static com.livelife.motolibrary.AntData.CMD_COUNTDOWN_TIMEUP;
import static com.livelife.motolibrary.AntData.EVENT_PRESS;
import static com.livelife.motolibrary.AntData.LED_COLOR_GREEN;
import static com.livelife.motolibrary.AntData.LED_COLOR_OFF;

public class MindGame extends Game {

    protected long BASE_TIME_VISIBLE_MS = 1500; // Each tile is visible for 3 seconds
    protected long BASE_TIME_TO_CLICK_MS = 2000; //User has 100 seconds to click each tile, effectively making it not matter. Intended for this game mode.
    /*
    Uncomment when using tiles
    MotoConnection motoConnection = MotoConnection.getInstance();
    MotoSound sound = MotoSound.getInstance(); */

    ArrayList<Level> levels;
    Level currentLevel;
    TileClick currentTileClick;
    TileClick currentTileShown;

    boolean isGameStarted = false; //Should be flipped after showing user what pattern they must click.
    boolean hasPressed = false;
    boolean isCorrectPressed = false;
    boolean shouldClear = false;

    //init random with a static seed for testing purposes. todo remove seed
    public Random rand = new Random(123);

    Handler handler = new Handler();

    int lastColor = -1;

    public MindGame(){
        setName("Mind Game");
        setDescription("something something");
        addGameType(new GameType(0, GameType.GAME_TYPE_SCORE, 100000, "No time limit",1));
        levels = new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onGameStart() {
        super.onGameStart();
        advanceGame();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onGameUpdate(byte[] message) {
        super.onGameUpdate(message);
        if(!isGameStarted) return;
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
        //Save state
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    public void handlePress(int tile){
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
            @Override
            public void run() {
                if(level.currentClickNum == level.size){
                    currentTileShown.color = 0;
                    currentTileShown = null;
                    isGameStarted = true;
                    currentTileClick = currentLevel.tileClicks.get(0);
                    return;
                }
                if(currentTileShown!= null){
                    currentTileShown.color = 0;
                }
                currentTileShown = level.tileClicks.get(level.currentClickNum);
                level.currentClickNum++;
                handler.postDelayed(this, currentTileShown.timeVisibleMs);
            }
        }, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    protected void playLevel(Level currentLevel, MindGame game){

        // Game loop runnable
        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void run() {
                //If level is still being shown to user, do nothing.
                if(!isGameStarted){
                    handler.postDelayed(this, 1000);
                    return;
                }
                //Start new runnable that asserts user presses within time limit
                UUID token = UUID.randomUUID();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //if this runs, user has failed.
                        System.out.println("\n\ntime ran out\n\n");
                        endGame();
                        return;
                    }
                }, token, currentTileClick.timeToPressMs);


                //wait for user to press...
                if(!hasPressed && !isCorrectPressed){
                    handler.postDelayed(this, 20);
                    return;
                }


                handler.removeCallbacksAndMessages(token);
                hasPressed = false;
                isCorrectPressed = false;

                //increment score and expect next input
                game.incrementPlayerScore(1, 0);
                System.out.println("you did it - score is " + game.getPlayerScore()[0]);

                //if correct press was last in level
                if(currentLevel.tileClicks.indexOf(currentTileClick) == currentLevel.tileClicks.size()-1){
                    advanceGame();
                    return;
                }
                //make next tile new target
                currentTileClick =currentLevel.tileClicks.get(currentLevel.tileClicks.indexOf(currentTileClick)+1);
                handler.postDelayed(this, 0);
            }
        },0);
    }

    protected TileClick generateTileClick(){
       return new TileClick(getNextTile(), BASE_TIME_VISIBLE_MS, getNextColor(), BASE_TIME_TO_CLICK_MS);

    }

    protected int getNextTile(){
        return rand.nextInt(4);
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

    @RequiresApi(api = Build.VERSION_CODES.P)
    protected void advanceGame(){
        this.shouldClear = true;
        handler.removeCallbacksAndMessages(null);
        this.isGameStarted = false;
        this.generateLevel();
        showLevel(this.currentLevel);
        playLevel(this.currentLevel,this);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void endGame(){
        System.out.println("GAME ENDED");
        handler.removeCallbacksAndMessages(null);
        this.stopGame();
        currentLevel.tileClicks.forEach((tc) -> tc.color = 0);
    }
}
