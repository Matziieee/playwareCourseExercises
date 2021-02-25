package com.playware.exercise2;

import com.livelife.motolibrary.AntData;
import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.MotoSound;

import java.util.ArrayList;
import java.util.Random;

import static com.livelife.motolibrary.AntData.*;

public class SpecialOneGame extends Game {

    MotoConnection connection = MotoConnection.getInstance();
    MotoSound motoSound = MotoSound.getInstance();

    ArrayList<Integer> colours = new ArrayList<>();
    Random r = new Random(1);
    int currentSpecialTile = -1;
    int score = 0;

    public SpecialOneGame() {
        setName("Special One");
        setDescription("Another cool game");

        GameType gt = new GameType(1, GameType.GAME_TYPE_TIME, 30,"1 player 30 sec",1);
        addGameType(gt);

        GameType gt2 = new GameType(2, GameType.GAME_TYPE_TIME, 60,"1 player 1 min",1);
        addGameType(gt2);
        addColours();
    }

    @Override
    public void onGameStart() {
        super.onGameStart();
        progressGame();
    }

    private void progressGame(){
        connection.setAllTilesIdle(LED_COLOR_OFF);
        int tile = connection.randomIdleTile();
        this.currentSpecialTile = tile;
        int specialColor = getRandomColor();
        int restColor = getRandomColor(specialColor);
        for (int i : connection.connectedTiles) {
            if(i == tile){
                connection.setTileColor(specialColor,i);
            }else{
                connection.setTileColor(restColor, i);
            }
        }
    }

    private void addColours(){
        colours.add(LED_COLOR_BLUE);
        colours.add(LED_COLOR_ORANGE);
        colours.add(LED_COLOR_GREEN);
        colours.add(LED_COLOR_RED);
        colours.add(LED_COLOR_INDIGO);
    }

    private int getRandomColor(){
       return getRandomColor(-1);
    }

    private int getRandomColor(int ignore){
        int color = ignore;
        while(color == ignore){
            color = this.colours.get(r.nextInt(this.colours.size()));
        }
        return color;
    }

    @Override
    public void onGameUpdate(byte[] message) {
        super.onGameUpdate(message);
        int event = AntData.getCommand(message);
        int id = AntData.getId(message);
        if(event == EVENT_PRESS && id == this.currentSpecialTile){
            this.score++;
            motoSound.speak(""+this.score);
            progressGame();
        }
    }

    @Override
    public void onGameEnd() {
        super.onGameEnd();
        motoSound.speak("Game ended, your score was: " + this.score);
        connection.setAllTilesIdle(LED_COLOR_OFF);

    }
}
