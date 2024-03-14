package com.example.capteurapp;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AllCaptorActivity extends AppCompatActivity {


    private Button btnAllSensors;
    private Button btnAvailableSensors;
    private Button btnUnavailableSensors;
    private TextView txtSensorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_captor);

        btnAllSensors = findViewById(R.id.btnAllSensors);
        btnAvailableSensors = findViewById(R.id.btnAvailableSensors);
        btnUnavailableSensors = findViewById(R.id.btnUnavailableSensors);
        txtSensorList = findViewById(R.id.txtSensorList);

        btnAllSensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAllSensors();
            }
        });

        btnAvailableSensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAvailableSensors();
            }
        });

        btnUnavailableSensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayUnavailableSensors();
            }
        });
    }


    private void displayAllSensors() {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        StringBuilder sensorStringBuilder = new StringBuilder();

        for (Sensor sensor : sensorList) {
            sensorStringBuilder.append(sensor.getName()).append("\n");
        }

        txtSensorList.setText(sensorStringBuilder.toString());
    }

    private void displayAvailableSensors() {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        StringBuilder availableSensorStringBuilder = new StringBuilder();

        for (Sensor sensor : sensorList) {
            if (sensor != null) {
                availableSensorStringBuilder.append(sensor.getName()).append("\n");
            }
        }

        txtSensorList.setText(availableSensorStringBuilder.toString());
    }

    private void displayUnavailableSensors() {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        StringBuilder unavailableSensorStringBuilder = new StringBuilder();

        for (Sensor sensor : sensorList) {
            if (sensor == null) {
                unavailableSensorStringBuilder.append("Capteur indisponible : ").append(sensor.getName()).append("\n");
            }
        }

        txtSensorList.setText(unavailableSensorStringBuilder.toString());
    }


}