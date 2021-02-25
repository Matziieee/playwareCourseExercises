package com.playware.exercise2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.MotoSound;
import com.livelife.motolibrary.OnAntEventListener;

public class MainActivity extends AppCompatActivity implements OnAntEventListener {

    MotoConnection connection = MotoConnection.getInstance();
    MotoSound sound = MotoSound.getInstance();
    Button pairingButton, startGameButton;
    boolean isPairing;
    TextView statusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connection.startMotoConnection(MainActivity.this);
        connection.saveRfFrequency(5*10+6);         //(Group No.)*10+6
        connection.setDeviceId(5);              //Your group number
        connection.registerListener(MainActivity.this);

//        sound.initializeSounds(this);

        statusTextView = findViewById(R.id.statusTextView);
        pairingButton = findViewById(R.id.pairingButton);
        startGameButton = findViewById(R.id.startGameButton);

        startGameButton.setOnClickListener(v -> {
            connection.unregisterListener(MainActivity.this);
            Intent i = new Intent(MainActivity.this, GameSelectorActivity.class);
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