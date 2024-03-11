package com.example.capteurapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ThirdActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView directionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        // Initialisation du SensorManager et du capteur d'accéléromètre
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Liaison avec le TextView pour afficher la direction
        directionTextView = findViewById(R.id.directionTextView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Enregistrement du SensorEventListener pour écouter les changements de l'accéléromètre
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Désenregistrement du SensorEventListener lorsque l'activité est en pause
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // Détection de la direction du mouvement en fonction des valeurs d'accélération
        String direction;
        if (Math.abs(x) > Math.abs(y) && Math.abs(x) > Math.abs(z)) {
            direction = (x > 0) ? "Gauche" : "Droite";
        } else if (Math.abs(y) > Math.abs(x) && Math.abs(y) > Math.abs(z)) {
            direction = (y > 0) ? "Haut" : "Bas";
        } else {
            direction = (z > 0) ? "Avant" : "Arrière";
        }

        // Affichage de la direction détectée sur l'écran
        directionTextView.setText("Direction : " + direction);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Méthode non utilisée dans cette implémentation
    }
}
