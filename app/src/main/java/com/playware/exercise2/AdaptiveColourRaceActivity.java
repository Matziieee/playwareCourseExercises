package com.playware.exercise2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.MotoSound;
import com.livelife.motolibrary.OnAntEventListener;

public class AdaptiveColourRaceActivity extends AppCompatActivity implements OnAntEventListener {

    MotoConnection connection = MotoConnection.getInstance();
    MotoSound motoSound = MotoSound.getInstance();
    LinearLayout gameTypeContainer;
    AdaptiveColourRaceGame game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adaptive_colour_race);

        connection.registerListener(this);

        game = new AdaptiveColourRaceGame();
        gameTypeContainer = findViewById(R.id.adaptiveColourRaceLayout);

        for (final GameType gt : game.getGameTypes()) {
            Button b = new Button(this);
            b.setText(gt.getName());
            b.setOnClickListener(v -> {
                motoSound.playStart();
                game.selectedGameType = gt;
                game.startGame();
            });
            gameTypeContainer.addView(b);
        }
    }

    //Stop the game when we exit activity
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        game.stopGame();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.stopMotoConnection();
        connection.unregisterListener(this);
    }
}