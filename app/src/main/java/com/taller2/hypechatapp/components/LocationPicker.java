package com.taller2.hypechatapp.components;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.taller2.hypechatapp.ui.activities.ChooseLocationActivity;

public class LocationPicker implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    public static final int LOCATION_PICKER_REQUEST_CODE = 2;
    public static final int LOCATION_PICKER_RESULT_CODE = 400;
    private static final int DEFAULT_ZOOM = 14;

    private LatLng latLng;

    private Activity activity;
    private MapFragment mapFragment;
    private GoogleMap map;
    private MaterialButton chooseLocationButton;

    public LocationPicker(final Activity activity, MapFragment mapFragment,
                          MaterialButton chooseLocationButton) {
        create(activity, mapFragment, chooseLocationButton);
    }

    public LocationPicker(final Activity activity, MapFragment mapFragment,
                          MaterialButton chooseLocationButton, double latitude, double longitude) {
        latLng = new LatLng(latitude, longitude);
        create(activity, mapFragment, chooseLocationButton);
    }

    private void create(final Activity activity, MapFragment mapFragment,
                        MaterialButton chooseLocationButton) {
        this.activity = activity;
        this.mapFragment = mapFragment;
        this.chooseLocationButton = chooseLocationButton;

        mapFragment.getMapAsync(this);
        mapFragment.getView().setVisibility(latLng != null ? View.VISIBLE : View.GONE);

        chooseLocationButton.setVisibility(latLng != null ? View.GONE : View.VISIBLE);
        chooseLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMapClick(latLng);
            }
        });
    }

    public LatLng analyzeResults(Intent data) {
        latLng = data.getParcelableExtra("selectedLocation");
        setMap();
        return latLng;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMapToolbarEnabled(false);
        map.setOnMapClickListener(this);
        if (latLng != null) {
            setMap();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Intent intent = new Intent(activity, ChooseLocationActivity.class);
        if (this.latLng != null) {
            intent.putExtra("startLocation", this.latLng);
        }
        activity.startActivityForResult(intent, LOCATION_PICKER_REQUEST_CODE);
    }

    private void setMap() {
        map.clear();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
        map.addMarker(new MarkerOptions().position(latLng));

        mapFragment.getView().setVisibility(View.VISIBLE);
        chooseLocationButton.setVisibility(View.INVISIBLE);
    }
}
