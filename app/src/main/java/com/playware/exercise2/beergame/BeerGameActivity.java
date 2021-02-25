package com.playware.exercise2.beergame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.livelife.motolibrary.Game;
import com.playware.exercise2.R;

public class BeerGameActivity extends AppCompatActivity {
    TableLayout gameLayout;
    GameTable gameTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_game);
        gameLayout = findViewById(R.id.gameTableLayout);
        gameTable = new GameTable();
        for (TableElement[] te_arr : gameTable.table) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1
            ));
            for(TableElement te : te_arr){
                TextView tv = new TextView(this);
                tv.setText("hello");
                tv.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1));
                row.addView(tv);
            }
            gameLayout.addView(row);
        }
    }
}