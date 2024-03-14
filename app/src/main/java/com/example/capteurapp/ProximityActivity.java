package com.example.capteurapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ProximityActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity);

        // Initialisation du SensorManager et du capteur de proximité
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        // Référence à l'image view
        imageView = findViewById(R.id.imageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Enregistrement du SensorEventListener pour écouter les changements de proximité
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Désenregistrement du SensorEventListener lorsque l'activité est en pause
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            // La valeur de la proximité est contenue dans event.values[0]
            float proximityValue = event.values[0];

            // Mettre à jour l'image en fonction de la proximité
            if (proximityValue < proximitySensor.getMaximumRange()) {
                // L'objet est proche
                imageView.setImageResource(R.drawable.bluesky_back);
            } else {
                // L'objet est loin
                imageView.setImageResource(R.drawable.desert);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Méthode non utilisée dans cette implémentation
    }
}