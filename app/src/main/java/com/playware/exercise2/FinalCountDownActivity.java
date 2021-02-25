package com.playware.exercise2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.MotoSound;
import com.livelife.motolibrary.OnAntEventListener;

public class FinalCountDownActivity extends AppCompatActivity implements OnAntEventListener {
    MotoConnection connection = MotoConnection.getInstance();
    Button playGameBtn;
    FinalCountdownGame game;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        game.stopGame();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_count_down);
        connection.registerListener(this);
        connection.setAllTilesToInit();

        playGameBtn = findViewById(R.id.fcPlay);
        game = new FinalCountdownGame();

        playGameBtn.setOnClickListener((v) -> {
            game.selectedGameType = game.getGameTypes().get(0);
            game.startGame();
        });
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