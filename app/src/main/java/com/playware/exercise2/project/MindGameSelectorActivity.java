package com.playware.exercise2.project;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.OnAntEventListener;
import com.playware.exercise2.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MindGameSelectorActivity extends AppCompatActivity{
    HashMap<String, Class> games = new HashMap<>();
    TableLayout layout;

    public MindGameSelectorActivity(){
        //Add new views here for auto generated buttons
        games.put("Base Game", MindGameActivity.class);
        //Todo implement and add (some of) these?
        games.put("Challenge Viewer", ActiveChallengesActivity.class);
        //games.put("Stored Games", StoredGamesActivity) #Save each game played as .json on local storage?
        //games.put("High Score list", HighScoreActivity) #Shows High Scores for all players.
        //games.put("Time based game", MindGameWithTimeActivity.class)
        //Other views/game types?
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mind_game_selector);
        layout = findViewById(R.id.mindGameSelectorTable);
        games.forEach( (key,val) -> {
            TableRow r = new TableRow(this);
            Button b = new Button(this);
            b.setText(key);
            b.setOnClickListener(v ->{
                Intent i = new Intent(this, val);
                startActivity(i);
            });
            r.addView(b);
            layout.addView(r);
        });
    }
}