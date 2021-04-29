package com.playware.exercise2.project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.playware.exercise2.maxhttp.TokenManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ActiveChallengesActivity extends AppCompatActivity {

    GameChallengeManager challengeManager;
    SharedPreferences sharedPreferences;
    Button updateChallengesBtn, getMyChallengesBtn;
    TextView listTitle;

    ArrayList<GameChallenge> items = new ArrayList<>();
    HashMap<Integer, GameChallenge> myChallengesMap = new HashMap<>();
    HashMap<Integer, GameChallenge> challengesMap = new HashMap<>();
    ArrayList<String> serializedAllChallenges = new ArrayList<>();
    ArrayList<String> serializedMyChallenges = new ArrayList<>();
    ArrayAdapter<String> allChallengesAdapter, myChallengesAdapter;
    ListView allChallengesListView, myChallengesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_challenges);

        sharedPreferences = this.getApplicationContext().getSharedPreferences("PLAYWARE_COURSE", Context.MODE_PRIVATE);
        updateChallengesBtn = findViewById(R.id.getMGChallengesBtn);
        getMyChallengesBtn = findViewById(R.id.getMGMyChallengesBtn);
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

        updateChallengesBtn.setOnClickListener(v -> {
            flipVisibleLists(true);
            listTitle.setText("Showing all newly created challenges");
            challengesMap.clear();
            items.clear();
            serializedAllChallenges.clear();
            ArrayList<GameChallenge> toAdd = challengeManager.getGameChallenges(TokenManager.getDeviceToken(sharedPreferences));
            if(toAdd != null){
                for (int i = 0; i < toAdd.size(); i++) {
                    if (isStatusCreated(toAdd.get(i))) {
                        items.add(toAdd.get(i));
                        challengesMap.put(i, toAdd.get(i));
                    }
                }
                serializedAllChallenges.addAll(this.getChallengesAsStrings());
                allChallengesAdapter.notifyDataSetChanged();
            }

        });

        getMyChallengesBtn.setOnClickListener(v -> {
            flipVisibleLists(false);
            listTitle.setText("Showing all your active challenges");
            myChallengesMap.clear();
            items.clear();
            serializedMyChallenges.clear();
            ArrayList<GameChallenge> toAdd =  challengeManager.getGameChallenges(TokenManager.getDeviceToken(sharedPreferences));
            if(toAdd != null){
                for (int i = 0; i < toAdd.size(); i++) {
                    if (isMyChallenge(toAdd.get(i))) {
                        items.add(toAdd.get(i));
                        challengesMap.put(i, toAdd.get(i));
                    }
                }
                serializedMyChallenges.addAll(this.getChallengesAsStrings());
                myChallengesAdapter.notifyDataSetChanged();
            }
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
                    .setPositiveButton("Start Game", (dialog, which) -> {
                        //Start
                        Intent i = new Intent(this, MindGameActivity.class);
                        i.putExtra("challenge", myChallengesMap.get(position));
                        startActivityForResult(i, 1111);
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                    .show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1111){
            int score = data == null ? -1 : data.getIntExtra("score",-1);
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
        for (GameChallenge c : this.items){
            list.add(c.toString());
        }
        return list;
    }

    private void startGameFromChallenge(int challNum){}
}