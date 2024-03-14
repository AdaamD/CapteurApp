package com.example.capteurapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DirectionActivity extends AppCompatActivity {

    // Indique si les données de capteur sont fiables
    private boolean reliable = true;

    // Valeur seuil pour déterminer un mouvement significatif
    private final static double THRESHOLD_VAL = 1.f;

    private long nextTiming;

    private TextView directionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        nextTiming = System.currentTimeMillis();

        directionTextView = findViewById(R.id.directionTextView);

        // Initialisation du SensorManager et récupération du capteur d'accélération linéaire
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        // Création d'un SensorEventListener pour écouter les changements du capteur
        SensorEventListener sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                // Vérifie si les données sont fiables et si c'est le moment pour la prochaine mise à jour
                if (reliable && System.currentTimeMillis() > nextTiming) {
                    // Mise à jour du timing pour la prochaine mise à jour
                    nextTiming = System.currentTimeMillis();

                    // Calcul de la valeur de l'accélération totale
                    double val = Math.sqrt(sensorEvent.values[0] * sensorEvent.values[0] +
                            sensorEvent.values[1] * sensorEvent.values[1] +
                            sensorEvent.values[2] * sensorEvent.values[2]);

                    // Vérification si le mouvement est significatif
                    if (val > THRESHOLD_VAL) {
                        // Mise à jour du timing pour la prochaine mise à jour
                        nextTiming += 400;

                        // Identification de la direction du mouvement
                        int idMaxAbs;
                        double valXAbs, valYAbs, valZAbs;
                        valXAbs = Math.abs(sensorEvent.values[0]);
                        valYAbs = Math.abs(sensorEvent.values[1]);
                        valZAbs = Math.abs(sensorEvent.values[2]);
                        if (valXAbs > valYAbs && valXAbs > valZAbs) {
                            idMaxAbs = 0;
                        } else if (valYAbs > valXAbs && valYAbs > valZAbs) {
                            idMaxAbs = 1;
                        } else {
                            idMaxAbs = 2;
                        }

                        // Affichage de la direction détectée
                        switch (idMaxAbs) {
                            case 0:
                                if (sensorEvent.values[idMaxAbs] > 0) {
                                    directionTextView.setText("➡️ ");
                                } else {
                                    directionTextView.setText("⬅️ ");
                                }
                                break;
                            case 1:
                                if (sensorEvent.values[idMaxAbs] > 0) {
                                    directionTextView.setText("⬆️ ");
                                } else {
                                    directionTextView.setText("⬇️ ");
                                }
                                break;
                            case 2:
                                if (sensorEvent.values[idMaxAbs] > 0) {
                                    directionTextView.setText("⏬");
                                } else {
                                    directionTextView.setText("⏫");
                                }
                                break;
                        }
                    } else {
                        // Mouvement non significatif
                        directionTextView.setText("❌");
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // Mise à jour de la fiabilité des données du capteur
                reliable = accuracy == SensorManager.SENSOR_STATUS_ACCURACY_HIGH || accuracy == SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM;
            }
        };

        // Enregistrement du SensorEventListener pour écouter les changements du capteur
        boolean supported = sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
        if (!supported) {
            // Désenregistrement si le capteur n'est pas supporté
            sensorManager.unregisterListener(sensorEventListener, accelerometer);
        }
    }
}
