package com.playware.exercise2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.OnAntEventListener;

import java.util.Arrays;

public class ColourRaceActivity extends AppCompatActivity implements OnAntEventListener {
    MotoConnection connection = MotoConnection.getInstance();
    ColourRaceManager manager;
    ColourRaceManager colourRaceManager;
    LinearLayout layout;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        colourRaceManager.stopGame();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        layout = findViewById(R.id.gameLayout);
        manager = new ColourRaceManager();
        for (final GameType gt : colourRaceManager.getGameTypes()) {
            Button b = new Button(this);
            b.setText(gt.getName());
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    colourRaceManager.selectedGameType = gt;
                    colourRaceManager.startGame();
                }
            });
            layout.addView(b);
        }
    }

    @Override
    public void onMessageReceived(byte[] bytes, long l) {
        System.out.println(Arrays.toString(bytes));
        manager.addEvent(bytes);
    }

    @Override
    public void onAntServiceConnected() {

    }

    @Override
    public void onNumbersOfTilesConnected(int i) {

    }
}