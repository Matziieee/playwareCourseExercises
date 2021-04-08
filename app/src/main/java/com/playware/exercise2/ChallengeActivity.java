package com.playware.exercise2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.playware.exercise2.maxhttp.ChallengeStatus;
import com.playware.exercise2.maxhttp.GameChallenge;
import com.playware.exercise2.maxhttp.GameChallengeManager;
import com.playware.exercise2.maxhttp.TokenManager;

import java.util.ArrayList;
import java.util.Arrays;

public class ChallengeActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    GameChallengeManager challengeManager;

    Button getChallengeBtn, postChallengeBtn;
    ArrayList<GameChallenge> items = new ArrayList<>();
    ArrayList<String> serializedItems = new ArrayList<>(Arrays.asList("hello","world"));
    ArrayAdapter<String> adapter;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        sharedPreferences = this.getApplicationContext().getSharedPreferences("PLAYWARE_COURSE", Context.MODE_PRIVATE);
        getChallengeBtn = findViewById(R.id.getChallengesBtn);
        postChallengeBtn = findViewById(R.id.postChallengesBtn);
        list = findViewById(R.id.challengeList);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, serializedItems);
        list.setAdapter(adapter);

        challengeManager = new GameChallengeManager();

        getChallengeBtn.setOnClickListener(v -> {
            items.clear();
            serializedItems.clear();

            ArrayList<GameChallenge> toAdd = challengeManager.getGameChallenges(TokenManager.getDeviceToken(sharedPreferences));
            if(toAdd != null){
                items.addAll(toAdd);
                serializedItems.addAll(this.getChallengesAsStrings());
                adapter.notifyDataSetChanged();
            }

        });
        postChallengeBtn.setOnClickListener(v -> {

            if(challengeManager.postGameChallenge(TokenManager.getDeviceToken(sharedPreferences),"666","Mads")){
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Posted Challenge!")
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }else{
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Failed to post challenge!")
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

        });

        list.setOnItemClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(view.getContext())
                    .setTitle("Accept this challenge?")
                    .setPositiveButton("Accept", (dialog, which) -> {
                        if(isStatusCreated(items.get(position)) &&
                                challengeManager.postGameChallengeAccept(TokenManager.getDeviceToken(sharedPreferences),
                                    String.valueOf(items.get(position).getGcid()),"testName")){
                            new AlertDialog.Builder(view.getContext())
                                    .setTitle("Accepted Challenge!")
                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }else{
                            new AlertDialog.Builder(view.getContext())
                                    .setTitle("Failed to accept challenge! Has it already been accepted?")
                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }
                        dialog.cancel();
                })
                    .setNegativeButton("Decline", (dialog, which) -> dialog.cancel())
                    .show();
        });

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
}