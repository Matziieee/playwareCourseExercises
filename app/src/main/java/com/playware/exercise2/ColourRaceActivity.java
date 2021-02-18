package com.playware.exercise2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.MotoSound;
import com.livelife.motolibrary.OnAntEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class ColourRaceActivity extends AppCompatActivity implements OnAntEventListener {

    MotoConnection connection = MotoConnection.getInstance();
    MotoSound motoSound = MotoSound.getInstance();
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
        motoSound.initializeSounds(this);
        setContentView(R.layout.activity_game);
        connection.registerListener(this);
        connection.setAllTilesToInit();

        colorRaceManager = new ColourRaceManager();
        gameTypeContainer = findViewById(R.id.gameTypeContainer);

        for (final GameType gt : colorRaceManager.getGameTypes()) {
            Button b = new Button(this);
            b.setText(gt.getName());
            b.setOnClickListener(v -> {
                motoSound.playStart();
                colorRaceManager.selectedGameType = gt;
                colorRaceManager.startGame();
            });
            gameTypeContainer.addView(b);
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.stopMotoConnection();
        connection.unregisterListener(this);
    }
}