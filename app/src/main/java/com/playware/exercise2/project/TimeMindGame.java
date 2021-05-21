package com.playware.exercise2.project;

import android.os.CountDownTimer;
import static com.livelife.motolibrary.AntData.LED_COLOR_GREEN;

public class TimeMindGame extends MindGame {
    CountDownTimer timer;
    int timeLeftAlpha = 255;


    public TimeMindGame(){
        super();
        //Set base time to click to 6000;
        this.BASE_TIME_TO_CLICK_MS = 10000;
    }

    @Override
    protected void advanceGame() {
        //Scale time to click - Reduce by 1000ms after each round.
        if(BASE_TIME_TO_CLICK_MS > 3000){
            BASE_TIME_TO_CLICK_MS -= 1000;
        }
        //call super
        super.advanceGame();
    }

    @Override
    protected void setGameOngoingTileColor(){
        for (Integer tile : motoConnection.connectedTiles) {
            motoConnection.setTileColorCountdown(LED_COLOR_GREEN, tile, (int) (BASE_TIME_TO_CLICK_MS / 1000 * 2.5));
        }
        if(timer != null){
            timer.cancel();
        }
        if(isGameStarted){
            setTimer();
            timer.start();
        }
    }

    @Override
    protected void finishLevel() {
        super.finishLevel();
        timer.cancel();
        timeLeftAlpha = 255;
    }

    private void setTimer(){
        timer = new CountDownTimer(this.BASE_TIME_TO_CLICK_MS, 50) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftAlpha = (int)((double)millisUntilFinished/BASE_TIME_TO_CLICK_MS*100*2.55);
            }

            @Override
            public void onFinish() {
                //If timer finishes, player has lost and game should be ended.
                endGame();
            }
        };
    }
    @Override
    protected void endGame(){
        //cancel timer in case it hasn't finished.
        timer.cancel();
        super.endGame();
    }
}
