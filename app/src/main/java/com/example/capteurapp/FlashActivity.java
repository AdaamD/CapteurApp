package com.example.capteurapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.hardware.Sensor;
import android.graphics.Camera;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.hardware.SensorEventListener;
import android.hardware.SensorEvent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;
import android.hardware.camera2.CameraManager;

import android.os.Build;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class FlashActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private static final int SHAKE_THRESHOLD = 800;

    private long lastUpdate;
    private float last_x, last_y, last_z;

    private boolean isFlashOn;

    private TextView textView;
    private ImageView imageView;

    private CameraManager mCameraManager;
    private String mCameraId;

    ImageButton backButton;
    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            if (se.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                long curTime = System.currentTimeMillis();
                if ((curTime - lastUpdate) > 100) {
                    long diffTime = (curTime - lastUpdate);
                    lastUpdate = curTime;

                    float x = se.values[0];
                    float y = se.values[1];
                    float z = se.values[2];

                    float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                    if (speed > SHAKE_THRESHOLD) {
                        toggleFlashlight();
                    }

                    last_x = x;
                    last_y = y;
                    last_z = z;
                }
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);

        textView = findViewById(R.id.instructionTextView);
        imageView = findViewById(R.id.imageFlash);

        backButton = findViewById(R.id.backButton);

        // Initialize the sensor manager and accelerometer
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cette ligne termine l'activité en cours
            }
        });

        // Check if the device has a flash
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            Toast.makeText(this, "Votre appareil ne dispose pas d'un flash", Toast.LENGTH_LONG).show();
            finish();
        }

        // Get the camera instance
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        isFlashOn = false;


    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register the accelerometer sensor listener
        mSensorManager.registerListener(mSensorListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the accelerometer sensor listener
        mSensorManager.unregisterListener(mSensorListener);
        // Turn off the flash if it is on
        if (isFlashOn) {
            turnOffFlash();
        }
    }

    private void toggleFlashlight() {
        if (isFlashOn) {
            turnOffFlash();
        } else {
            turnOnFlash();
        }
    }

    private void turnOnFlash() {
        if (!isFlashOn) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mCameraManager.setTorchMode(mCameraId, true);
                } else {
                    Toast.makeText(this, "Impossible d'activer le flash pour la version Android inférieure à Marshmallow", Toast.LENGTH_SHORT).show();
                }
                isFlashOn = true;
                textView.setText("Flash allumé");
                imageView.setImageResource(R.drawable.flash_on);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void turnOffFlash() {
        if (isFlashOn) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mCameraManager.setTorchMode(mCameraId, false);
                } else {
                    Toast.makeText(this, "Impossible de désactiver le flash pour la version Android inférieure à Marshmallow", Toast.LENGTH_SHORT).show();
                }
                isFlashOn = false;
                textView.setText("Flash éteint");
                imageView.setImageResource(R.drawable.flash_off);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
