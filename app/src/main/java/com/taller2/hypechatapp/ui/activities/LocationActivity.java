package com.taller2.hypechatapp.ui.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.components.PermissionsRequester;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

public abstract class LocationActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    GoogleMap map;
    private boolean mLocationPermissionGranted;
    protected View chooseLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);

        setUpUi();
    }

    private void setUpUi() {
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        chooseLocationButton = findViewById(R.id.choose_location_button);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapClickListener(this);
        map.setOnMarkerClickListener(this);

        getLocationPermission();
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
            PermissionsRequester.requestPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
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
