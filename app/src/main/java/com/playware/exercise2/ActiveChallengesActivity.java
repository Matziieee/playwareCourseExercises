package com.playware.exercise2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.playware.exercise2.R;
import com.playware.exercise2.maxhttp.ChallengeStatus;
import com.playware.exercise2.maxhttp.GameChallenge;
import com.playware.exercise2.maxhttp.GameChallengeManager;
import com.playware.exercise2.maxhttp.GameSessionManager;
import com.playware.exercise2.maxhttp.GameSessionPostRequest;
import com.playware.exercise2.maxhttp.TokenManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ActiveChallengesActivity extends AppCompatActivity {

    GameChallengeManager challengeManager;
    GameSessionManager sessionManager;
    SharedPreferences sharedPreferences;
    Button updateChallengesBtn, getMyChallengesBtn, addChallBtn;
    TextView listTitle;

    ArrayList<GameChallenge> items = new ArrayList<>();
    HashMap<Integer, GameChallenge> myChallengesMap = new HashMap<>();
    HashMap<Integer, GameChallenge> challengesMap = new HashMap<>();
    ArrayList<String> serializedAllChallenges = new ArrayList<>();
    ArrayList<String> serializedMyChallenges = new ArrayList<>();
    ArrayAdapter<String> allChallengesAdapter, myChallengesAdapter;
    ListView allChallengesListView, myChallengesListView;

    int selectedMode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_challenges);

        sharedPreferences = this.getApplicationContext().getSharedPreferences("PLAYWARE_COURSE", Context.MODE_PRIVATE);
        updateChallengesBtn = findViewById(R.id.getMGChallengesBtn);
        getMyChallengesBtn = findViewById(R.id.getMGMyChallengesBtn);
        addChallBtn = findViewById(R.id.MGAddChallengeBtn);
        listTitle = findViewById(R.id.listStatusText);

        allChallengesListView = findViewById(R.id.MGChallengeList);
        myChallengesListView = findViewById(R.id.MGMyChallengeList);
        myChallengesListView.setVisibility(View.INVISIBLE);

        myChallengesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, serializedMyChallenges);

        allChallengesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, serializedAllChallenges);

        allChallengesListView.setAdapter(allChallengesAdapter);
        myChallengesListView.setAdapter(myChallengesAdapter);

        challengeManager = new GameChallengeManager();
        sessionManager = new GameSessionManager();

        addChallBtn.setOnClickListener(v ->{
            selectedMode = 0;
            new AlertDialog.Builder(v.getContext())

                    .setSingleChoiceItems(new CharSequence[]{"Normal", "Hard", "Normal Time", "Hard Time"},0, (dialog, which) -> {
                        selectedMode = which;
                    })
                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton("Create Challenge", (dialog,v1) ->{
                        if(challengeManager.postGameChallenge(TokenManager.getDeviceToken(sharedPreferences), selectedMode)){
                            new AlertDialog.Builder(v.getContext())
                                .setTitle("Created Challenge successfully!")
                                .setNegativeButton(android.R.string.no, null)
                                .show();
                        }else{
                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("Failed to create challenge!")
                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();


        });
        updateChallengesBtn.setOnClickListener(v -> {
            flipVisibleLists(true);
            listTitle.setText("Showing all newly created challenges");
            challengesMap.clear();
            items.clear();
            serializedAllChallenges.clear();
            ArrayList<GameChallenge> toAdd = challengeManager.getGameChallenges(TokenManager.getDeviceToken(sharedPreferences));
            if(toAdd != null){
                int i = 0;
                for(GameChallenge gc : toAdd){
                    if (isStatusCreated(gc)) {
                        items.add(gc);
                        challengesMap.put(i,gc);
                        i++;
                    }
                }
                serializedAllChallenges.addAll(this.getChallengesAsStrings());
                allChallengesAdapter.notifyDataSetChanged();
            }

        });

        getMyChallengesBtn.setOnClickListener(v -> {
            updateMyChallenges();
        });

        allChallengesListView.setOnItemClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(view.getContext())
                    .setTitle("Accept this challenge?")
                    .setPositiveButton("Accept", (dialog, which) -> {
                        String token = TokenManager.getDeviceToken(sharedPreferences);
                        if (challengeManager.postGameChallengeAccept(token, challengesMap.get(position))) {
                            new AlertDialog.Builder(view.getContext())
                                    .setTitle("Accepted Challenge!")
                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        } else {
                            new AlertDialog.Builder(view.getContext())
                                    .setTitle("Failed to accept challenge! Has it already been accepted?")
                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    })
                    .setNegativeButton("Decline", (dialog, which) -> dialog.cancel())
                    .show();
        });

        myChallengesListView.setOnItemClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(view.getContext())
                    .setTitle("Attempt this challenge?")
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                    .setNeutralButton("Delete Challenge", (dialog, which) -> {
                        String token = TokenManager.getDeviceToken(sharedPreferences);
                        if(this.challengeManager.deleteGameChallenge(""+myChallengesMap.get(position).getGcid(), token)){
                            updateMyChallenges();
                            new AlertDialog.Builder(view.getContext())
                                    .setTitle("Deleted Challenge!")
                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton("Ok", null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }else{
                            new AlertDialog.Builder(view.getContext())
                                    .setTitle("Failed to delete challenge!")
                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    })
                    .show();
        });
    }

    private void postGameSessionResult(GameChallenge gc, int score, String token){
        GameSessionPostRequest req = new GameSessionPostRequest(""+gc.getGameId(), ""+gc.getGameTypeId(), ""+score, "-1", "4",gc.getGcid());
        if(sessionManager.postGameSession(req,token)){
            updateMyChallenges();
            new AlertDialog.Builder(this)
                    .setTitle("Successfully submitted result!")
                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        }else{
            new AlertDialog.Builder(this)
                    .setTitle("Failed to submit result, try again!")
                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1111 && resultCode == Activity.RESULT_OK){
            int score = data == null ? -1 : data.getIntExtra("score",-1);
            postGameSessionResult((GameChallenge) data.getExtras().get("challenge"), score, TokenManager.getDeviceToken(sharedPreferences));
        }
    }

    private void flipVisibleLists(boolean isShowingAll){
        if(isShowingAll){
            allChallengesListView.setVisibility(View.VISIBLE);
            myChallengesListView.setVisibility(View.GONE);
        }else{
            allChallengesListView.setVisibility(View.GONE);
            myChallengesListView.setVisibility(View.VISIBLE);
        }

    }
    private boolean isMyChallenge(GameChallenge gc){
        String token = TokenManager.getDeviceToken(sharedPreferences);
        return gc.getDeviceToken().equals(token) || gc.getDeviceToken_c().equals(token);
    }

    private boolean isStatusCreated(GameChallenge gc){
        return gc.getcStatus() == ChallengeStatus.ZERO;
    }
    private ArrayList<String> getChallengesAsStrings(){
        ArrayList<String> list = new ArrayList<>();
        String me = TokenManager.getDeviceToken(sharedPreferences);
        for (GameChallenge c : this.items){
            if(me.equals(c.getDeviceToken())) {
                c.setChallengerName("Me");
            }
            list.add(c.toString());
        }
        return list;
    }

    private ArrayList<String> getMyChallengesAsStrings(){
        ArrayList<String> list = new ArrayList<>();
        for (GameChallenge c : this.items){
            String me = TokenManager.getDeviceToken(sharedPreferences);
            if(me.equals(c.getDeviceToken())){
                c.setChallengerName("Me");
            }else{
                c.setChallengedName("Me");
            }
            list.add(c.toString() + getHighScores(c));
        }
        return list;
    }

    private String getHighScores(GameChallenge gc) {
        String me = TokenManager.getDeviceToken(sharedPreferences);
        String other = gc.getDeviceToken().equals(me) ? gc.getDeviceToken_c() : gc.getDeviceToken();
        int myScore = 0;
        int opponentScore = 0;
        //Find highest scores
        try {
            for (int i = 0; i < gc.getSummaryObject().length(); i++) {
                JSONObject obj = gc.getSummaryObject().getJSONObject(i);
                String token = obj.getString("device_token");
                int score = Integer.parseInt(obj.getString("game_score"));
                if (token.equals(me) && score > myScore) {
                    myScore = score;
                }
                else if (token.equals(other) && score > opponentScore) {
                    opponentScore = score;
                }
            }
            return "My best score: " + myScore + "\n" +
                    "Opponents best score: " + opponentScore;
        }catch (Exception e) {
            return "";
        }

    }
    private void updateMyChallenges(){
        flipVisibleLists(false);
        listTitle.setText("Showing all your challenges");
        myChallengesMap.clear();
        items.clear();
        serializedMyChallenges.clear();
        ArrayList<GameChallenge> toAdd =  challengeManager.getGameChallenges(TokenManager.getDeviceToken(sharedPreferences));
        if(toAdd != null){
            int i = 0;
            for(GameChallenge gc : toAdd){
                if (isMyChallenge(gc)) {
                    items.add(gc);
                    myChallengesMap.put(i, gc);
                    i++;
                }
            }
            serializedMyChallenges.addAll(this.getMyChallengesAsStrings());
            myChallengesAdapter.notifyDataSetChanged();
        }
    }
}