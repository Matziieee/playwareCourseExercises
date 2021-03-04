package com.playware.exercise2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.OnAntEventListener;

public class AdaptiveGameActivity extends AppCompatActivity implements Game.OnGameEventListener, OnAntEventListener {

    TextView currentColorText, scoreText, time;
    Button startBtn;
    AdaptiveGame game;

    MotoConnection connection = MotoConnection.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adaptive_game);

        connection.registerListener(this);
        currentColorText = findViewById(R.id.currentColorText);
        time = findViewById(R.id.timeView);
        scoreText = findViewById(R.id.scoreTextView);
        startBtn = findViewById(R.id.playAdaptive);
        game = new AdaptiveGame();
        game.setOnGameEventListener(this);
        startBtn.setOnClickListener((View v) -> {
            game.selectedGameType = game.getGameTypes().get(0);
            game.startGame();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        game.stopGame();
    }

    @Override
    public void onGameTimerEvent(int i) {
        game.time_left -=1;
        if(game.time_left == 0){
            game.endGame("Time ran out");
        }
        runOnUiThread(()-> {
            this.scoreText.setText(
                    "Your score: " + game.getPlayerScore()[0]+  "/7");
            this.currentColorText.setText(game.getCurrentColorString());
            this.time.setText("TIME LEFT: " + game.time_left);
        });

    }

    @Override
    public void onGameScoreEvent(int i, int i1) {
        runOnUiThread(()-> {
            this.scoreText.setText(
                "Your score: " + game.getPlayerScore()[0]+  "/7");
            this.currentColorText.setText(game.getCurrentColorString());
        });
        game.speed -= 1;
        game.time_left = game.speed;
        if(game.getPlayerScore()[0] == 7){
            game.endGame("You won");
        }
    }

    @Override
    public void onGameStopEvent() {

    }

    @Override
    public void onSetupMessage(String s) {

    }

    @Override
    public void onGameMessage(String s) {

    }

    @Override
    public void onSetupEnd() {

    }

    @Override
    public void onMessageReceived(byte[] bytes, long l) {
        game.addEvent(bytes);
    }

    @Override
    public void onAntServiceConnected() {

    }

    @Override
    public void onNumbersOfTilesConnected(int i) {

    }
}