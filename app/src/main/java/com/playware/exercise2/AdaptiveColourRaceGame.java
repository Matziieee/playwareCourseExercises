package com.playware.exercise2;

import android.os.Handler;

import com.livelife.motolibrary.AntData;
import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.MotoSound;

import static com.livelife.motolibrary.AntData.*;

public class AdaptiveColourRaceGame extends Game {

    MotoConnection connection = MotoConnection.getInstance();
    MotoSound motoSound = MotoSound.getInstance();
    long delay = 5000;
    double changeFactor = 0.2;
    int currentTile = -1;
    boolean pressed = false, isInit = false;
    Handler handler;
    Runnable runnable;

    public AdaptiveColourRaceGame() {
        setName("Adaptive Color Race");
        setDescription("A nice game");

        GameType gt = new GameType(1, GameType.GAME_TYPE_TIME, 30,"1 player 30 sec",1);
        addGameType(gt);

        GameType gt2 = new GameType(2, GameType.GAME_TYPE_TIME, 60,"1 player 1 min",1);
        addGameType(gt2);

        GameType gt3 = new GameType(3, GameType.GAME_TYPE_TIME, 60*2,"1 player 2 min",1);
        addGameType(gt3);

    }

    @Override
    public void onGameStart() {
        super.onGameStart();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if(isInit && !pressed){
                    motoSound.playPianoSound(0);
                    //game is initialized but player did not press in time. Make speed slower
                    long delayChange = (long) (delay * changeFactor);
                    delay += delayChange;
                    pressed = false;
                }
                progressGame();
                isInit = true;
                handler.postDelayed(this, delay);
            }
        };
        handler.post(runnable);
    }

    private void progressGame(){
        connection.setAllTilesIdle(LED_COLOR_OFF);
        int tile = connection.randomIdleTile();
        currentTile = tile;
        for (int t: connection.connectedTiles) {
            if(t == tile){
                connection.setTileColor(LED_COLOR_GREEN,t);
            }
            else{
                connection.setTileColor(LED_COLOR_OFF,t);
            }
        }
    }

    @Override
    public void onGameUpdate(byte[] message) {
        super.onGameUpdate(message);
        int tile = AntData.getId(message);
        int event = AntData.getCommand(message);
        System.out.println("TILE: " + tile + " WAS PRESSED");
        if(event == EVENT_PRESS){
            if(tile == currentTile){
                motoSound.playMatched();
                pressed = true;
                incrementPlayerScore(1,0);
                long delayChange = (long) (delay * changeFactor);
                delay -= delayChange;
                if(delay < 1000)
                    delay = 1000;
                connection.setAllTilesIdle(LED_COLOR_OFF);
            }
            else{
                motoSound.playPianoSound(0);
                long delayChange = (long) (delay * changeFactor);
                delay += delayChange;
            }
        }
    }

    @Override
    public void onGameEnd() {
        super.onGameEnd();
        handler.removeCallbacksAndMessages(null);
        connection.setAllTilesIdle(LED_COLOR_OFF);
        motoSound.playStop();
    }
}
