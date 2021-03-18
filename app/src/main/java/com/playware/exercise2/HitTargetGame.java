package com.playware.exercise2;

import com.livelife.motolibrary.AntData;
import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.MotoSound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import android.os.Handler;

import static com.livelife.motolibrary.AntData.EVENT_PRESS;
import static com.livelife.motolibrary.AntData.LED_COLOR_BLUE;
import static com.livelife.motolibrary.AntData.LED_COLOR_GREEN;
import static com.livelife.motolibrary.AntData.LED_COLOR_INDIGO;
import static com.livelife.motolibrary.AntData.LED_COLOR_OFF;
import static com.livelife.motolibrary.AntData.LED_COLOR_ORANGE;
import static com.livelife.motolibrary.AntData.LED_COLOR_RED;

public class HitTargetGame extends Game{
    MotoConnection connection = MotoConnection.getInstance();
    MotoSound motoSound = MotoSound.getInstance();

    public long targetSpeed;
    public int targetIDX;
    public long min = 50;
    public long max = 8000;

    HitTargetGame() {
        setName("Hit The Target");
        setMaxPlayers(1);
        setDescription("Hit the correct tile, but beware, the game evolves with you");

        GameType gt = new GameType(1, GameType.GAME_TYPE_TIME, 30,"1 player 30 sec",1);
        addGameType(gt);

        GameType gt2 = new GameType(2, GameType.GAME_TYPE_TIME, 60,"1 player 1 min",1);
        addGameType(gt2);

        GameType gt3 = new GameType(3, GameType.GAME_TYPE_TIME, 60*2,"1 player 2 min",1);
        addGameType(gt3);
    }

    public Handler targetHandler = new Handler();
    public Runnable targetRunnable = new Runnable() {

        @Override
        public void run(){
            motoSound.playStep();
            targetIDX++;
            if(targetIDX==connection.connectedTiles.size())
                targetIDX = 0;
                int nextTile = connection.connectedTiles.get(targetIDX);
                for (int t : connection.connectedTiles){
                    if(t==nextTile){
                        connection.setTileColor(LED_COLOR_RED, t);
                    } else{
                        connection.setTileColor(LED_COLOR_OFF,t);
                    }
                }
        float percent = 1.5f;
        targetSpeed += (targetSpeed * percent) / 100;
        if (targetSpeed >= max){targetSpeed = max;}
        else if(targetSpeed <= min){targetSpeed = min;}
        targetHandler.postDelayed(this,targetSpeed);
        }
    };

    @Override
    public void onGameUpdate(byte[] message) {
        super.onGameUpdate(message);
        int tileID = AntData.getId(message);
        int event = AntData.getCommand(message);
        int color= AntData.getColorFromPress(message);
        boolean success = false;

        if (event == AntData.EVENT_PRESS && color!=LED_COLOR_OFF)
        {
            motoSound.playMatched();
            incrementPlayerScore(1,0);
            success = true;
        }
        else {
            motoSound.playError();
        }

        int percent = 10;
        if (success){
            targetSpeed -= (targetSpeed * percent) / 100;
            if (targetSpeed < min){targetSpeed = min;}
        } else {
            targetSpeed += (targetSpeed * percent) / 100;
            if (targetSpeed > min){targetSpeed = max;}
        }

    }

    @Override
    public void onGameEnd() {
        super.onGameEnd();
        targetHandler.removeCallbacksAndMessages(null);
        connection.setAllTilesBlink(4,LED_COLOR_RED);
        motoSound.playStop();
    }
}
