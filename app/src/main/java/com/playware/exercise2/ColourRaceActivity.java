package com.playware.exercise2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.OnAntEventListener;

public class ColourRaceActivity extends AppCompatActivity implements OnAntEventListener {

    MotoConnection connection = MotoConnection.getInstance();
    LinearLayout gameTypeContainer;
    ColourRaceManager colorRaceManager;

    //Stop the game when we exit activity
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        colorRaceManager.stopGame();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        connection.registerListener(this);
        connection.setAllTilesToInit();

        colorRaceManager = new ColourRaceManager();
        gameTypeContainer = findViewById(R.id.gameTypeContainer);

        for (final GameType gt : colorRaceManager.getGameTypes()) {
            Button b = new Button(this);
            b.setText(gt.getName());
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    colorRaceManager.selectedGameType = gt;
                    colorRaceManager.startGame();
                }
            });
            gameTypeContainer.addView(b);
        }

        colorRaceManager.setOnGameEventListener(new Game.OnGameEventListener() {
            @Override

            public void onGameTimerEvent(int i) {

            }

            @Override
            public void onGameScoreEvent(int i, int i1) {

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
        });
    }

    @Override
    public void onMessageReceived(byte[] bytes, long l) {
        colorRaceManager.addEvent(bytes);
    }

    @Override
    public void onAntServiceConnected() {

    }

    @Override
    public void onNumbersOfTilesConnected(int i) {

    }
}