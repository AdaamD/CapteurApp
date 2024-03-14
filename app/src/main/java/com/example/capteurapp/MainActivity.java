package com.example.capteurapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnGoToFirstActivity;
    private Button btnGoToSecondActivity;
    private Button btnGoToThirdActivity;
    private Button btnGoToFlashActivity;
    private Button btnGoToProximityActivity;
    private  Button btnGoToLocationActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGoToFirstActivity = findViewById(R.id.btnGoToFirstActivity);
        btnGoToSecondActivity = findViewById(R.id.btnGoToSecondActivity);
        btnGoToThirdActivity = findViewById(R.id.btnGoToThirdActivity);
        btnGoToFlashActivity = findViewById(R.id.btnGoToFlashActivity);
        btnGoToProximityActivity = findViewById(R.id.btnGoToProximityActivity);
        btnGoToLocationActivity = findViewById(R.id.btnGoToLocationActivity);


        btnGoToFirstActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AllCaptorActivity.class));
            }
        });


        btnGoToSecondActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AccelerometerActivity.class));
            }
        });

        btnGoToThirdActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DirectionActivity.class));
            }
        });

        btnGoToFlashActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FlashActivity.class));
            }
        });

        btnGoToProximityActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProximityActivity .class));
            }
        });

        btnGoToLocationActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LocationActivity .class));
            }
        });

    }

}
