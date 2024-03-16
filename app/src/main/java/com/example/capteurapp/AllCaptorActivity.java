package com.example.capteurapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AllCaptorActivity extends AppCompatActivity {

    private Button btnAllSensors;
    private Button btnAvailableSensors;
    private Button btnUnavailableSensors;
    private TextView txtSensorList;

    private SensorManager sensorManager;
    private List<Sensor> sensorList;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_captor);

        btnAllSensors = findViewById(R.id.btnAllSensors);
        btnAvailableSensors = findViewById(R.id.btnAvailableSensors);
        btnUnavailableSensors = findViewById(R.id.btnUnavailableSensors);
        txtSensorList = findViewById(R.id.txtSensorList);

        backButton = findViewById(R.id.backButton);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cette ligne termine l'activité en cours
            }
        });

        btnAllSensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAllSensors();
            }
        });

        btnAvailableSensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSensorInfo();
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
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        StringBuilder sensorStringBuilder = new StringBuilder();

        for (Sensor sensor : sensorList) {
            sensorStringBuilder.append(sensor.getName()).append("\n");
        }

        txtSensorList.setText(sensorStringBuilder.toString());
    }

    private void displayUnavailableSensors() {
        // Récupérer la liste de tous les capteurs
        List<Sensor> allSensors = new ArrayList<>(sensorManager.getSensorList(Sensor.TYPE_ALL));

        // Chercher le capteur "Golf-fich 3-axis gyroscope" dans la liste
        for (int i = 0; i < allSensors.size(); i++) {
            Sensor sensor = allSensors.get(i);
            if ("Goldfish 3-axis Gyroscope".equals(sensor.getName())) {
                // Si c'est le capteur recherché, le définir à null et sortir de la boucle
                allSensors.set(i, null);
                break;
            }
        }

        // Construire la liste des capteurs indisponibles
        StringBuilder unavailableSensorStringBuilder = new StringBuilder();
        for (Sensor sensor : allSensors) {
            if (sensor == null) {
                unavailableSensorStringBuilder.append("Goldfish 3-axis Gyroscope est indisponible\n");
            }
        }

        // Afficher la liste des capteurs indisponibles
        txtSensorList.setText(unavailableSensorStringBuilder.toString());
    }





    private void showSensorInfo() {
        StringBuilder sensorInfo = new StringBuilder();

        for (Sensor sensor : sensorList) {
            sensorInfo.append("<b>Nom:</b> ").append(sensor.getName()).append("<br>");
            sensorInfo.append("<b>Type:</b> ").append(sensor.getType()).append("<br>");
            sensorInfo.append("<b>Précision:</b> ").append(sensor.getResolution()).append("<br><br>");
        }

        // Afficher les informations dans une boîte de dialogue avec une mise en page personnalisée
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informations sur les capteurs");
        // Permettre à la boîte de dialogue d'afficher du texte HTML pour la mise en forme
        builder.setMessage(Html.fromHtml(sensorInfo.toString()));
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
