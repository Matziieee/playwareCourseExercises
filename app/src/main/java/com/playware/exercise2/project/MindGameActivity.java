package com.playware.exercise2.project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.camera2.params.ColorSpaceTransform;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Button;

import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.MotoSound;
import com.livelife.motolibrary.OnAntEventListener;
import com.playware.exercise2.R;
import com.playware.exercise2.maxhttp.GameChallenge;

import java.util.ArrayList;
import java.util.HashMap;

public class MindGameActivity extends AppCompatActivity implements OnAntEventListener {

    TextView levelText,scoreText;
    Button startGame;
    HashMap<Integer, ColorBox> tileColorViewMap = new HashMap<>();
    ArrayList<ColorBox> colorViews = new ArrayList<>();
    ColorViewAdapter gridAdapter;
    GridView gridView;
    MindGame game;
    boolean isChallenge = false;
    GameChallenge challenge;

    MotoConnection connection = MotoConnection.getInstance();
    MotoSound sound = MotoSound.getInstance();


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
    public void onBackPressed() {
        super.onBackPressed();
        if(isChallenge){
            Intent resultIntent = new Intent();
            resultIntent.putExtra("score", -1);
            setResult(Activity.RESULT_CANCELED, resultIntent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        targetHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mind_game);
        //Init UI elements
        levelText = findViewById(R.id.mindLevelText);
        scoreText = findViewById(R.id.mindScoreText);
        startGame = findViewById(R.id.startGameBtn);

        startGame.setBackgroundColor(Color.BLUE);

        initGrid();
        connection.registerListener(this);
        connection.setAllTilesToInit();
        Bundle b = getIntent().getExtras();
        if(b!= null){
            isChallenge = true;
            challenge = (GameChallenge) b.get("challenge");
        }

        startGame.setOnClickListener((v) -> {
            startGame.setEnabled(false);
            game.startGame = false;
            game.advanceGame();
        });

        game = new MindGame();
        game.setSelectedGameType(0);
        game.startGame();
        targetHandler.postDelayed(uiHandler,updateSpeed);
    }


    private void initGrid(){
        //init hashMap and list
        for(int i = 1; i < 5; i++){
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
                game.handlePress(position+1);
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
            if (game.startGame){
                startGame.setEnabled(true);
            }

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
        if(isChallenge && game.isGameOver){
            Intent resultIntent = new Intent();
            resultIntent.putExtra("score", game.getPlayerScore()[0]);
            resultIntent.putExtra("challenge", challenge);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
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