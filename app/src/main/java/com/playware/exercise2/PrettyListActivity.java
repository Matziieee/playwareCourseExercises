package com.playware.exercise2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.playware.exercise2.maxhttp.GameSessionManager;
import com.playware.exercise2.maxhttp.GameSessionPostRequest;

import java.util.ArrayList;

public class PrettyListActivity extends AppCompatActivity {

    ArrayList<String> items = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ListView list;
    Button getSessionsBtn, postSessionBtn;
    GameSessionManager manager;
    int inc1 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pretty_list);
        getSessionsBtn = findViewById(R.id.getSessionsBtn);
        postSessionBtn = findViewById(R.id.postSessionBtn);
        list = findViewById(R.id.sessionlist);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,items);
        list.setAdapter(adapter);

        manager = new GameSessionManager();
        getSessionsBtn.setOnClickListener(v -> {
            items.clear();
            ArrayList<String> toAdd = manager.getGameSessions(50);
            if(toAdd != null){
                items.addAll(toAdd);
                adapter.notifyDataSetChanged();
            }

        });
        postSessionBtn.setOnClickListener(v -> {
            GameSessionPostRequest req = new GameSessionPostRequest("50","1",String.valueOf(inc1++),
                    "10", "100","4");

            if( manager.postGameSession(req)){
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Posted!")
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }else{
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Failed to post!")
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

        });

    }
}