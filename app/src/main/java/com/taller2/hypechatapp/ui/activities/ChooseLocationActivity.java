package com.taller2.hypechatapp.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.components.PermissionsRequester;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

public class ChooseLocationActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private LatLng startLocation;
    private static final int DEFAULT_ZOOM = 14;
    public static final int STEP_CODE = 400;

    GoogleMap map;
    private boolean mLocationPermissionGranted;
    private Marker marker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);

        startLocation = getIntent().getParcelableExtra("startLocation");
        if (startLocation == null) {
            startLocation = new LatLng(-34.6131500, -58.3772300);
        }

        setUpUi();
    }

    private void setUpUi() {
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        MaterialButton button = findViewById(R.id.choose_location_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseLocationActivity.this, CreateOrganizationActivity.class);
                intent.putExtra("selectedLocation", marker.getPosition());
                setResult(STEP_CODE, intent);
                finish();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        marker = map.addMarker(new MarkerOptions().position(startLocation));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                startLocation, DEFAULT_ZOOM));

        map.setOnMapClickListener(this);
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return true;
            }
        });


        getLocationPermission();
    }

    @Override
    public void onMapClick(LatLng point) {
        map.clear();
        marker = map.addMarker(new MarkerOptions().position(point));
        marker.hideInfoWindow();
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (PermissionsRequester.hasPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            mLocationPermissionGranted = true;
            updateLocationUI();
        } else {
            PermissionsRequester.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = PermissionsRequester.analyzeResults(requestCode, grantResults);
        updateLocationUI();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {

        if (map == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                map.getUiSettings().setMyLocationButtonEnabled(true);
                map.setMyLocationEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

}
