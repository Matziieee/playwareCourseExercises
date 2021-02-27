package com.playware.exercise2.beergame;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.livelife.motolibrary.Game;
import com.playware.exercise2.R;


public class BeerGameActivity extends AppCompatActivity {
    BeerGameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_game);

        gameView = findViewById(R.id.beerGameView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }
}
