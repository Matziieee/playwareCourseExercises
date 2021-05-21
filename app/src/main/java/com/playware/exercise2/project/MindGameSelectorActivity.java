package com.playware.exercise2.project;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.OnAntEventListener;
import com.playware.exercise2.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MindGameSelectorActivity extends AppCompatActivity{
    Button normalBtn, hardBtn, challengesBtn, normaTimelBtn, hardTimeBtn;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mind_game_selector);
        normalBtn = findViewById(R.id.normalBtn);
        hardBtn = findViewById(R.id.hardBtn);
        normaTimelBtn = findViewById(R.id.normalTimeBtn);
        hardTimeBtn = findViewById(R.id.hardTimeBtn);
        challengesBtn = findViewById(R.id.challViewerBtn);

        normalBtn.setOnClickListener(v -> {
            Intent i = new Intent(this, MindGameActivity.class);
            i.putExtra("mode", 0);
            startActivity(i);
        });

        hardBtn.setOnClickListener(v -> {
            Intent i = new Intent(this, MindGameActivity.class);
            i.putExtra("mode", 1);
            startActivity(i);
        });

        normaTimelBtn.setOnClickListener(v -> {
            Intent i = new Intent(this, MindGameActivity.class);
            i.putExtra("mode", 2);
            startActivity(i);
        });

        hardTimeBtn.setOnClickListener(v -> {
            Intent i = new Intent(this, MindGameActivity.class);
            i.putExtra("mode", 3);
            startActivity(i);
        });

        challengesBtn.setOnClickListener(v -> {
            Intent i = new Intent(this, ActiveChallengesActivity.class);
            startActivity(i);
        });
    }
}