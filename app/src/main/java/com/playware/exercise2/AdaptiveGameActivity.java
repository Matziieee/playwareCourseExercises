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

public class AdaptiveGameActivity extends AppCompatActivity implements OnAntEventListener {

    MotoConnection connection = MotoConnection.getInstance();
    MotoSound motoSound = MotoSound.getInstance();
    LinearLayout gameTypeContainer;
    AdaptiveGame AdaptiveGameManager;

    //Stop the game when we exit activity
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AdaptiveGameManager.stopGame();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        connection.registerListener(this);
        connection.setAllTilesToInit();

        AdaptiveGameManager = new AdaptiveGame();
        gameTypeContainer = findViewById(R.id.gameTypeContainer);

        for (final GameType gt : AdaptiveGameManager.getGameTypes()) {
            Button b = new Button(this);
            b.setText(gt.getName());
            b.setOnClickListener(v -> {
                motoSound.playStart();
                AdaptiveGameManager.selectedGameType = gt;
                AdaptiveGameManager.startGame();
            });
            gameTypeContainer.addView(b);
        }
    }

    @Override
    public void onMessageReceived(byte[] bytes, long l) {
        AdaptiveGameManager.addEvent(bytes);
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
