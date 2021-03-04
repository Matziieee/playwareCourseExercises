package com.playware.exercise2;

import com.livelife.motolibrary.AntData;
import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.MotoSound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static com.livelife.motolibrary.AntData.EVENT_PRESS;
import static com.livelife.motolibrary.AntData.LED_COLOR_BLUE;
import static com.livelife.motolibrary.AntData.LED_COLOR_GREEN;
import static com.livelife.motolibrary.AntData.LED_COLOR_INDIGO;
import static com.livelife.motolibrary.AntData.LED_COLOR_OFF;
import static com.livelife.motolibrary.AntData.LED_COLOR_ORANGE;
import static com.livelife.motolibrary.AntData.LED_COLOR_RED;

public class AdaptiveGame extends Game {

    MotoConnection connection = MotoConnection.getInstance();
    MotoSound motoSound = MotoSound.getInstance();
    int currentColor = -1;
    ArrayList<Integer> colours = new ArrayList<>();
    Random random = new Random(0);
    HashMap<Integer,Integer>tileColorMap = new HashMap<>();
    public int speed = 10;
    public int time_left = 10;

    public AdaptiveGame(){
        setName("Adaptive Game");
        setDescription("Another one");

        GameType gt = new GameType(1, GameType.GAME_TYPE_TIME, 100,"Play Game",1);
        addGameType(gt);
        addColours();
    }

    private void addColours(){
        colours.add(LED_COLOR_BLUE); //2
        colours.add(LED_COLOR_ORANGE); //5
        colours.add(LED_COLOR_GREEN); //3
        colours.add(LED_COLOR_RED); //1
        colours.add(LED_COLOR_INDIGO); //4
    }

    private void progressGame(){
        connection.setAllTilesIdle(LED_COLOR_OFF);
        //Select random color
        this.currentColor = this.colours.get(random.nextInt(this.colours.size()));
        //Select random tile and give it that random color
        int tile = connection.randomIdleTile();
        connection.setTileColor(this.currentColor,tile);
        this.tileColorMap.put(tile,this.currentColor);

        //Paint the rest of the tiles in random colors (can be the same)
        for(int t : connection.connectedTiles){
            if(t!= tile){
                int color = this.colours.get(random.nextInt(this.colours.size()));
                connection.setTileColor(color,t);
                this.tileColorMap.put(t,color);
            }
        }
    }

    @Override
    public void onGameStart() {
        super.onGameStart();
        this.progressGame();
    }

    @Override
    public void onGameUpdate(byte[] message) {
        super.onGameUpdate(message);
        int id = AntData.getId(message);
        int event = AntData.getCommand(message);
        int color = AntData.getColor(message);
        System.out.println("This tile is: " + color + " = " + this.getColorString(color));

        if(event == EVENT_PRESS){
            if(tileColorMap.get(id) == this.currentColor) {
                this.incrementPlayerScore(1,0);
                motoSound.speak("Good job!");
                progressGame();
            }
            else{
                if(this.getPlayerScore()[0] != 0){
                    this.incrementPlayerScore(-1,0);
                    motoSound.playError();
                }
            }
        }
    }

    public void endGame(String toSay){
        motoSound.speak(toSay);
        this.stopGame();
    }
    @Override
    public void onGameEnd() {
        super.onGameEnd();
        connection.setAllTilesIdle(LED_COLOR_OFF);
    }



    private String getColorString(int color){
        switch (color){
            case 1:
                return "RED";
            case 2:
                return "BLUE";
            case 3:
                return "GREEN";
            case 4:
                return "INDIGO";
            case 5:
                return "ORANGE";
            default:
                return "Game not running";
        }
    }

    public String getCurrentColorString(){
        return this.getColorString(this.currentColor);
    }
}
