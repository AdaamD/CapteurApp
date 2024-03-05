package com.example.capteurapp;


import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Initialisation du SensorManager et du capteur d'accéléromètre
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
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
        // Récupération des valeurs de l'accéléromètre
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // Calcul de la magnitude de l'accélération
        double acceleration = Math.sqrt(x * x + y * y + z * z);

        // Détermination de la couleur du fond en fonction de l'accélération
        int color;
        if (acceleration < 10) {
            color = Color.GREEN; // Valeurs inférieures : vert
        } else if (acceleration < 15) {
            color = Color.BLACK; // Valeurs moyennes : noir
        } else {
            color = Color.RED; // Valeurs supérieures : rouge
        }

        // Changement de la couleur de fond de l'activité
        getWindow().getDecorView().setBackgroundColor(color);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    // Non utilisé
    }

}
