package com.example.capteurapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.content.Context;
import androidx.core.content.ContextCompat;


import androidx.appcompat.app.AppCompatActivity;

public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    Sensor accelerometer;
    float[] lastAccelerometer = new float[3];
    View view1, view2, view3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        // Trouver les vues à colorer
        view1 = findViewById(R.id.top_zone);
        view2 = findViewById(R.id.middle_zone);
        view3 = findViewById(R.id.bottom_zone);

        // Obtenir l'instance du SensorManager et de l'accéléromètre
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Enregistrer l'écouteur du capteur d'accéléromètre
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Arrêter l'écouteur du capteur d'accéléromètre pour économiser les ressources
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            // Obtenir les valeurs d'accéléromètre sur les trois axes
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Mettre à jour la dernière valeur d'accéléromètre
            lastAccelerometer[0] = x;
            lastAccelerometer[1] = y;
            lastAccelerometer[2] = z;

            // Changer la couleur des vues en fonction des valeurs d'accéléromètre
            if (x < -5f) {
                view1.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
            } else if (x > 5f) {
                view1.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
            } else {
                view1.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
            }

            if (y < -5f) {
                view2.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
            } else if (y > 5f) {
                view2.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
            } else {
                view2.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
            }

            if (z < -5f) {
                view3.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
            } else if (z > 5f) {
                view3.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
            } else {
                view3.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Ne rien faire pour le moment
    }
}
