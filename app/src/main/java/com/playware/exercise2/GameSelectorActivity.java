package com.playware.exercise2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;

public class GameSelectorActivity extends AppCompatActivity {
    ArrayList<Class> games = new ArrayList();
    TableLayout layout;

    public GameSelectorActivity (){
        games.add(ColourRaceActivity.class);
        games.add(SpecialOneActivity.class);
        games.add(FinalCountDownActivity.class);
        games.add(AdaptiveGameActivity.class);
        games.add(AdaptiveGame2Activity.class);
        games.add(HitTargetActivity.class);
        games.add(PrettyListActivity.class);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selector);
        layout = findViewById(R.id.table);
        games.forEach( (c) -> {
            TableRow r = new TableRow(this);
            Button b = new Button(this);
            b.setText(c.getSimpleName());
            b.setOnClickListener(v ->{
                Intent i = new Intent(this, c);
                startActivity(i);
            });
            r.addView(b);
            layout.addView(r);
        });
    }
}