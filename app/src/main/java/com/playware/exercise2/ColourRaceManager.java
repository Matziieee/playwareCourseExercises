package com.playware.exercise2;

import android.util.Log;

import com.livelife.motolibrary.AntData;
import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.MotoSound;

import static com.livelife.motolibrary.AntData.LED_COLOR_BLUE;
import static com.livelife.motolibrary.AntData.LED_COLOR_GREEN;
import static com.livelife.motolibrary.AntData.LED_COLOR_OFF;
import static com.livelife.motolibrary.AntData.LED_COLOR_RED;

public class ColourRaceManager extends Game {

    MotoConnection connection = MotoConnection.getInstance();
    MotoSound motoSound = MotoSound.getInstance();

    ColourRaceManager() {
        setName("Color Race");
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

        connection.setAllTilesIdle(LED_COLOR_OFF);

        int randomTile = connection.randomIdleTile();
        Log.i("TAG", "random id " + randomTile);
        motoSound.speak("Step on green to start");
        connection.setTileColor(LED_COLOR_GREEN, randomTile);
    }

    @Override
    public void onGameUpdate(byte[] message) {
        super.onGameUpdate(message);
        int event = AntData.getCommand(message);
        int color= AntData.getColorFromPress(message);
        if (event == AntData.EVENT_PRESS && color!=LED_COLOR_OFF)
        {
            motoSound.playPianoSound(0);
            incrementPlayerScore(1,0);
            int randomTile = connection.randomIdleTile();
            connection.setAllTilesColor(LED_COLOR_OFF);
            connection.setTileColor(LED_COLOR_BLUE, randomTile);
        }
        else {
            motoSound.playAnimalSound(1);
        }
    }

    @Override
    public void onGameEnd() {
        super.onGameEnd();

        connection.setAllTilesBlink(4,LED_COLOR_RED);
        motoSound.playStop();
    }
}

