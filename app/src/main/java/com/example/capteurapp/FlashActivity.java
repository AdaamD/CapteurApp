package com.example.capteurapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class FlashActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private boolean isFlashOn = false;
    private float acceleration;
    private float accelerationCurrent;
    private float accelerationLast;
    private static final float SHAKE_THRESHOLD = 12f;

    private CameraManager cameraManager;
    private String cameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);

        // Vérifier si l'appareil dispose d'un flash
        if (!getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            Toast.makeText(this, "Votre appareil ne dispose pas de flash!", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initialisation du capteur d'accéléromètre
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Initialisation de la caméra pour contrôler le flash
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
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
        // Récupération de la valeur de l'accélération sur les trois axes
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        // Calcul de l'accélération linéaire
        accelerationLast = accelerationCurrent;
        accelerationCurrent = (float) Math.sqrt(x * x + y * y + z * z);
        float delta = accelerationCurrent - accelerationLast;
        acceleration = acceleration * 0.9f + delta;
        // Si le seuil de secousse est dépassé, basculer l'état du flash
        if (acceleration > SHAKE_THRESHOLD) {
            Toast.makeText(this, "Secousse détectée!", Toast.LENGTH_SHORT).show();
            toggleFlashlight();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Méthode non utilisée dans cette implémentation
    }

    private void toggleFlashlight() {
        try {
            if (isFlashOn) {
                // Éteindre le flash
                cameraManager.setTorchMode(cameraId, false);
                isFlashOn = false;
            } else {
                // Allumer le flash
                cameraManager.setTorchMode(cameraId, true);
                isFlashOn = true;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
