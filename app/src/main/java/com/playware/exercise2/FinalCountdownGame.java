package com.playware.exercise2;

import com.livelife.motolibrary.AntData;
import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.MotoSound;

import static com.livelife.motolibrary.AntData.*;

public class FinalCountdownGame extends Game {
    MotoConnection motoConnection = MotoConnection.getInstance();
    MotoSound sound = MotoSound.getInstance();
    final int GAME_SPEED = 15;

    public FinalCountdownGame (){
        setName("Final Countdown");
        setDescription("Yet another cool game");

        GameType gt = new GameType(1, GameType.GAME_TYPE_TIME, 120,"1 player 2 min",1);
        addGameType(gt);
    }

    @Override
    public void onGameStart() {
        super.onGameStart();
        motoConnection.setAllTilesIdle(LED_COLOR_OFF);
        sound.playStart();
        for (int tile : motoConnection.connectedTiles) {
            motoConnection.setTileColorCountdown(LED_COLOR_GREEN,tile,this.GAME_SPEED);
        }
    }

    @Override
    public void onGameUpdate(byte[] message) {
        super.onGameUpdate(message);
        int event = AntData.getCommand(message);
        int id = AntData.getId(message);
        if(event == CMD_COUNTDOWN_TIMEUP){
            sound.speak("Game over");
            this.stopGame();
        }
        else if(event == EVENT_PRESS){
            motoConnection.setTileColorCountdown(LED_COLOR_GREEN,id,this.GAME_SPEED);
            this.incrementPlayerScore(1,0);
            sound.speak("" + this.getPlayerScore()[0]);
        }
    }

    @Override
    public void onGameEnd() {
        super.onGameEnd();
        sound.speak("Your score was: " + this.getPlayerScore()[0]);
        motoConnection.setAllTilesIdle(LED_COLOR_OFF);
    }
}
