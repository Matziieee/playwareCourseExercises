package com.playware.exercise2.project;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
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
    HashMap<Integer, ColorBox> tileColorViewMap = new HashMap<>();
    ArrayList<ColorBox> colorViews = new ArrayList<>();
    ColorViewAdapter gridAdapter;
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

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mind_game);
        //Init UI elements
        levelText = findViewById(R.id.mindLevelText);
        scoreText = findViewById(R.id.mindScoreText);
        initGrid();
        game = new MindGame();
        game.setSelectedGameType(0);
        game.startGame();
        targetHandler.postDelayed(uiHandler,updateSpeed);
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    private void initGrid(){
        //init hashMap and list
        for(int i = 0; i < 4; i++){
            ColorBox cv = new ColorBox();
            tileColorViewMap.put(i,cv);
            colorViews.add(cv);
        }
        //grid adapter and grid
        gridAdapter = new ColorViewAdapter(this, android.R.layout.simple_list_item_1,colorViews);
        gridView = findViewById(R.id.gridView);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            if(game.isGameStarted){
                game.handlePress(position);
            }
        });
        gridAdapter.notifyDataSetChanged();
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

            if(game.shouldClear){
                for(ColorBox b : tileColorViewMap.values()){
                    b.setCurrentColor(Color.TRANSPARENT);
                }
                game.shouldClear = false;
            }

            // LEVEL TEXT
            if(game.currentLevel != null){
                levelText.setText("Level: " + game.currentLevel.size);
            }

            // SCORE TEXT
            scoreText.setText("Score: " + game.getPlayerScore()[0]);

            // VISUALIZE SHOW PHASE
            if (game.currentTileShown != null) {
                for(TileClick tc : game.currentLevel.tileClicks){
                    if(tc.tile != game.currentTileShown.tile){
                        tileColorViewMap.get(tc.tile)
                                .setCurrentColor(Color.TRANSPARENT);
                        continue;
                    }
                    tileColorViewMap.get(tc.tile)
                            .setCurrentColor(translateColor(game.currentTileShown.color));
                }
            }

            // VISUALIZE PLAY PHASE
            if(game.isGameStarted) {
                for(ColorBox b : tileColorViewMap.values()){
                    b.setCurrentColor(Color.YELLOW);
                }
            }
        });

        gridAdapter.notifyDataSetChanged();
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