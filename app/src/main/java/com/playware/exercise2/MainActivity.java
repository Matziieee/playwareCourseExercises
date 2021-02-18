package com.playware.exercise2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.OnAntEventListener;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements OnAntEventListener {

    MotoConnection connection;
    Button pairingButton;
    Button gameButton;
    TextView statusText;
    boolean isPairing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connection = MotoConnection.getInstance();
        connection.startMotoConnection(MainActivity.this);
        connection.saveRfFrequency(5*10+6);
        connection.setDeviceId(5);
        connection.registerListener(MainActivity.this);
        statusText = findViewById(R.id.statusText);
        pairingButton = findViewById(R.id.pairingButton);
        pairingButton.setOnClickListener( ev -> {
            if(isPairing){
                connection.pairTilesStart();
                pairingButton.setText("Stop Pairing");
            }else{
                connection.pairTilesStop();
                pairingButton.setText("Start Pairing");
            }
            isPairing = !isPairing;
        });
        gameButton = findViewById(R.id.playGameButton);
        gameButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ColourRaceActivity.class);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public void onMessageReceived(byte[] bytes, long l) {
        System.out.println(Arrays.toString(bytes));
    }

    @Override
    public void onAntServiceConnected() {

    }

    @Override
    public void onNumbersOfTilesConnected(int i) {
        System.out.println("Number of tiles con: " + i);
        runOnUiThread(() -> statusText.setText(i + " Number of tiles connected"));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        connection.startMotoConnection(MainActivity.this);
        connection.registerListener(MainActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        connection.stopMotoConnection();
        connection.unregisterListener(MainActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.stopMotoConnection();
        connection.unregisterListener(MainActivity.this);
    }
}