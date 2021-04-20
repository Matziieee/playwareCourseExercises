package com.playware.exercise2.project;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.livelife.motolibrary.OnAntEventListener;
import com.playware.exercise2.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MindGameActivity extends AppCompatActivity implements OnAntEventListener {

    TextView levelText,scoreText;
    HashMap<Integer, ColorView> tileColorViewMap = new HashMap<>();
    ArrayList<ColorView> colorViews = new ArrayList<>();
    ArrayAdapter<ColorView> gridAdapter;
    GridView gridView;
    MindGame game;

    Handler targetHandler = new Handler();
    long updateSpeed = 100;
    Runnable uiHandler = new Runnable() {
        @Override
        public void run() {
            updateUI();
            targetHandler.postDelayed(this,updateSpeed);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mind_game);
        //Init UI elements
        levelText = findViewById(R.id.mindLevelText);
        scoreText = findViewById(R.id.mindScoreText);
        initGrid();
        game = new MindGame();
    }


    private void initGrid(){
        //init hashMap and list
        for(int i = 0; i < 4; i++){
            ColorView cv = new ColorView(this,null);
            tileColorViewMap.put(i,cv);
            colorViews.add(cv);
        }
        //grid adapter and grid
        gridAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, colorViews);
        gridView = findViewById(R.id.gridView);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            //todo code for manual click here until we have tile.
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

    private void updateUI(){
        runOnUiThread(() -> {
            levelText.setText("Level: " + game.currentLevel.size);
            scoreText.setText("Score: " + game.getPlayerScore()[0]);
            if(game.currentTileClick != null){
                tileColorViewMap.get(game.currentTileClick.tile)
                        .setCurrentColor(translateColor(game.currentTileClick.color));
            }
        });
    }

    private int translateColor(int motoColor){
        switch (motoColor){
            case 1: return Color.RED;
            case 2: return Color.BLUE;
            case 3: return Color.GREEN;
            case 4: return Color.rgb(128,0,128);
            default: return Color.TRANSPARENT;
        }
    }
}