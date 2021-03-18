package com.playware.exercise2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.MotoSound;
import com.livelife.motolibrary.OnAntEventListener;

public class HitTargetActivity extends AppCompatActivity implements OnAntEventListener {

    MotoConnection connection = MotoConnection.getInstance();
    MotoSound motoSound = MotoSound.getInstance();
    LinearLayout gameTypeContainer;
    HitTargetGame HitTargetGame;

    //Stop the game when we exit activity
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HitTargetGame.stopGame();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        connection.registerListener(this);
        connection.setAllTilesToInit();

        HitTargetGame = new HitTargetGame();
        gameTypeContainer = findViewById(R.id.gameTypeContainer);

        for (final GameType gt : HitTargetGame.getGameTypes()) {
            Button b = new Button(this);
            b.setText(gt.getName());
            b.setOnClickListener(v -> {
                motoSound.playStart();
                HitTargetGame.selectedGameType = gt;
                HitTargetGame.startGame();
            });
            gameTypeContainer.addView(b);
        }
    }

    @Override
    public void onMessageReceived(byte[] bytes, long l) {
        HitTargetGame.addEvent(bytes);
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