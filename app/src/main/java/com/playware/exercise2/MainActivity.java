package com.playware.exercise2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.content.SharedPreferences;


import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.MotoSound;
import com.livelife.motolibrary.OnAntEventListener;
import com.livelife.motolibrary.AntData;
import com.playware.exercise2.project.MindGameSelectorActivity;

import android.os.AsyncTask;
import android.os.Debug;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements OnAntEventListener {

    MotoConnection connection = MotoConnection.getInstance();
    MotoSound sound = MotoSound.getInstance();
    Button pairingButton, startGameButton, startMindGameBtn;

    boolean isPairing;
    TextView statusTextView;
    TextView apiOutput;
    SharedPreferences sharedPref;

    String endpoint = "https://centerforplayware.com/api/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connection.startMotoConnection(MainActivity.this);
        connection.saveRfFrequency(4*10+6);         //(Group No.)*10+6
        connection.setDeviceId(4);              //Your group number
        connection.registerListener(MainActivity.this);

        sound.initializeSounds(this);

        statusTextView = findViewById(R.id.statusTextView);
        pairingButton = findViewById(R.id.pairingButton);
        startMindGameBtn = findViewById(R.id.mindGameBtn);
        startGameButton = findViewById(R.id.startGameButton);

        startGameButton.setOnClickListener(v -> {
            connection.unregisterListener(MainActivity.this);
            Intent i = new Intent(MainActivity.this, GameSelectorActivity.class);
            startActivity(i);
        });
        startMindGameBtn.setOnClickListener(v -> {
            connection.unregisterListener(MainActivity.this);
            Intent i = new Intent(MainActivity.this, MindGameSelectorActivity.class);
            startActivity(i);
        });

        pairingButton.setOnClickListener(view -> {
            if(!isPairing){
                connection.pairTilesStart();
                pairingButton.setText("Stop pairing!");
            }else{
                connection.pairTilesStop();
                pairingButton.setText("Start pairing!");
            }
            isPairing = !isPairing;
        });

    }

    @Override
    public void onMessageReceived(byte[] bytes, long l) {
        System.out.println("Hello world");
    }

    @Override
    public void onAntServiceConnected() {
        connection.setAllTilesToInit();
    }

    @Override
    public void onNumbersOfTilesConnected(final int i) {
        runOnUiThread(() -> statusTextView.setText(i +" connected tiles"));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        connection.registerListener(MainActivity.this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.stopMotoConnection();
        connection.unregisterListener(this);
    }
}