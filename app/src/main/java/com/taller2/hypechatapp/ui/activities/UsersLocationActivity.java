package com.taller2.hypechatapp.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.preferences.UserManagerPreferences;
import com.taller2.hypechatapp.services.UserService;

import java.util.List;

public class UsersLocationActivity extends LocationActivity {

    private UserService userService;
    private UserManagerPreferences preferences;
    private List<User> users;
    private LatLng organizationLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userService = new UserService();
        preferences = new UserManagerPreferences(this);

        setUpUI();

        getUsers();
    }

    private void getUsers() {

        userService.getUsersByOrganization(preferences.getSelectedOrganization(), new Client<List<User>>() {
            @Override
            public void onResponseSuccess(List<User> responseBody) {
               users=responseBody;
               setUpMarkers();
            }

            @Override
            public void onResponseError(boolean connectionError, String errorMessage) {
                String textToShow = "No se pudo obtener los usuarios de la organización";
                Toast.makeText(getContext(), textToShow, Toast.LENGTH_LONG).show();
            }

            @Override
            public Context getContext() {
                return UsersLocationActivity.this;
            }
        });
    }

    private void setUpUI() {
        chooseLocationButton.setVisibility(View.INVISIBLE);
    }

    private void setUpMarkers() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        Organization organization = (Organization) getIntent().getSerializableExtra("organization");

        LatLng organizationLocation=new LatLng(organization.getLatitude(),organization.getLongitude());
        map.addMarker(new MarkerOptions().position(organizationLocation).title(organization.getName())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        builder.include(organizationLocation);

        for (User user : users) {
            LatLng userLocation=new LatLng(user.latitude,user.longitude);
            map.addMarker(new MarkerOptions().position(userLocation).title(user.name));
            builder.include(userLocation);
        }

        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);
        map.moveCamera(cu);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);

        try {
            map.getUiSettings().setMyLocationButtonEnabled(false);
            map.setMyLocationEnabled(false);
        } catch (SecurityException e){
            Log.e("Exception: %s", e.getMessage());
        }

    }

    @Override
    public void onMapClick(LatLng latLng) {
       //Do nothing
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        map.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        return true;
    }
}
