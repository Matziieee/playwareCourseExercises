package com.playware.exercise2.motohero;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.playware.exercise2.R;


public class MotoHeroActivity extends AppCompatActivity {
    MotoHeroView gameView;

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
