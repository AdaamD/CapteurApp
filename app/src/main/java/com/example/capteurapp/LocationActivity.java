package com.example.capteurapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private TextView locationTextView;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        locationTextView = findViewById(R.id.locationTextView);

        // Initialisation du client de localisation
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialisation de l'objet LocationCallback
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    updateLocationUI(location);
                }
            }
        };

        // Vérification et demande des autorisations de géolocalisation si nécessaire
        if (checkLocationPermissions()) {
            // Obtention de la position géographique
            createLocationRequest();
        }
    }

    private boolean checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Demander les autorisations de géolocalisation à l'utilisateur
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    private void createLocationRequest() {
        // Création d'une requête de localisation
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Vérification de la permission ACCESS_FINE_LOCATION
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Demande de mises à jour de localisation
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            // Demander la permission ACCESS_FINE_LOCATION à l'utilisateur si elle n'a pas encore été accordée
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    private void updateLocationUI(Location location) {
        // Affichage des coordonnées géographiques dans TextView
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        locationTextView.setText("Latitude : " + latitude + "\nLongitude : " + longitude);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Autorisations accordées, obtenir la position géographique
                createLocationRequest();
            } else {
                // Autorisations refusées, afficher un message d'erreur
                Toast.makeText(this, "Autorisations de géolocalisation refusées", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Arrêter les mises à jour de localisation lorsque l'activité est détruite
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}
