package com.playware.exercise2;

import com.livelife.motolibrary.AntData;
import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.MotoSound;

import android.os.Handler;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


import static com.livelife.motolibrary.AntData.LED_COLOR_BLUE;
import static com.livelife.motolibrary.AntData.LED_COLOR_OFF;
import static com.livelife.motolibrary.AntData.LED_COLOR_RED;

public class AdaptiveGame2 extends Game {

    MotoConnection connection = MotoConnection.getInstance();
    MotoSound motoSound = MotoSound.getInstance();
    public long targetSpeed;
    public int targetIndex = 0;

    public long minSpeed = 50;
    public long maxSpeed = 8000;


    public AdaptiveGame2() {
        setName("Adaptive Game2");
        setMaxPlayers(1);

        GameType gt1 = new GameType(1, GameType.GAME_TYPE_TIME, 60, "1 Player 1 minute", 1);
        GameType gt2 = new GameType(1, GameType.GAME_TYPE_TIME, 90, "1 Player  90 seconds", 1);
        GameType gt3 = new GameType(1, GameType.GAME_TYPE_TIME, 120, "1 Player  2  minutes", 1);


        addGameType(gt1);
        addGameType(gt2);
        addGameType(gt3);
    }


    public Handler targetHandler = new Handler();
    public Runnable targetRunnable = new Runnable() {


        @Override
        public void run() {

            motoSound.playStep();

            targetIndex++;

           if (targetIndex == connection.connectedTiles.size())
                targetIndex = 0;

            int nextTile = connection.connectedTiles.get(targetIndex);
            for (int t : connection.connectedTiles) {
                if (t == nextTile) {
                    connection.setTileColor(LED_COLOR_RED, t);
                } else {
                    connection.setTileColor(LED_COLOR_OFF, t);
                }
            }
/*
            int randomTile = connection.randomIdleTile();
            connection.setAllTilesColor(LED_COLOR_OFF);
            connection.setTileColor(LED_COLOR_RED, randomTile);

*/
            float percent = 1.5f;

            targetSpeed += (targetSpeed * percent) / 100;

            if (targetSpeed >= maxSpeed) {
                targetSpeed = maxSpeed;
            }
            targetHandler.postDelayed(this, targetSpeed);
        }
    };


    @Override
    public void onGameStart() {
        super.onGameStart();

        targetIndex = 0;
        targetSpeed = 4500;

        int numPlayers = getPlayers().size();

        connection.setAllTilesIdle(LED_COLOR_OFF);
        int nextTile = connection.connectedTiles.get(0);
        for (int t : connection.connectedTiles) {
            if (t == nextTile) {
                connection.setTileColor(LED_COLOR_RED, t);
            } else {
                connection.setTileColor(LED_COLOR_OFF, t);
            }
        }
        targetHandler.postDelayed(targetRunnable, targetSpeed);
    }

    @Override
    public void onGameUpdate(byte[] message) {
        super.onGameUpdate(message);

        int tileId = AntData.getId(message);
        int event = AntData.getCommand(message);

        if (event == AntData.EVENT_PRESS) {

            int numPlayers = getPlayers().size();
            int color = AntData.getColorFromPress(message);
            boolean success = false;

            if (color == LED_COLOR_OFF) {
                motoSound.playError();
            } else {
                incrementPlayerScore(1, 0);
                motoSound.playMatched();
                connection.setAllTilesIdle(LED_COLOR_OFF);

                success = true;
            }

            int percent = 10;

            if (success) {

                targetSpeed -= (targetSpeed * percent) / 100;

                if (targetSpeed < minSpeed) {
                    targetSpeed = minSpeed;
                }
            } else {
                targetSpeed += (targetSpeed * percent) / 100;

                if (targetSpeed > maxSpeed) {
                    targetSpeed = maxSpeed;
                }
            }
        }
    }


    @Override
    public void onGameEnd() {
        super.onGameEnd();
        targetHandler.removeCallbacksAndMessages(null);

        connection.setAllTilesBlink(4, MotoConnection.playerColor[0]);
        motoSound.playStop();
    }
}

