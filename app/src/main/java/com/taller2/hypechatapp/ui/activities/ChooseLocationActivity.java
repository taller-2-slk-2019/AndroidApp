package com.taller2.hypechatapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class ChooseLocationActivity extends LocationActivity {

    private LatLng startLocation;
    private static final int DEFAULT_ZOOM = 14;
    public static final int STEP_CODE = 400;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startLocation = getIntent().getParcelableExtra("startLocation");
        if (startLocation == null) {
            startLocation = new LatLng(-34.6131500, -58.3772300);
        }

        setUpUI();
    }

    private void setUpUI() {
        chooseLocationButton.setOnClickListener(new View.OnClickListener() {
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
        super.onMapReady(googleMap);
        marker = map.addMarker(new MarkerOptions().position(startLocation));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                startLocation, DEFAULT_ZOOM));
    }

    @Override
    public void onMapClick(LatLng point) {
        map.clear();
        marker = map.addMarker(new MarkerOptions().position(point));
        marker.hideInfoWindow();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return true;
    }
}
