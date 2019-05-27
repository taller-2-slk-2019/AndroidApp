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
import com.google.maps.android.ui.IconGenerator;
import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.preferences.UserManagerPreferences;
import com.taller2.hypechatapp.services.UserService;
import com.taller2.hypechatapp.R;

import java.util.List;

public class UsersLocationActivity extends LocationActivity {

    private UserService userService;
    private UserManagerPreferences preferences;
    private List<User> users;

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
        IconGenerator iconFactory = new IconGenerator(this);
        iconFactory.setTextAppearance(R.style.UsersLocationTextStyle);
        iconFactory.setContentPadding(5,0,5,0);

        //Add markers for every user
        for (User user : users) {
            if(user.latitude!=null && user.longitude!=null){
                LatLng userLocation=new LatLng(user.latitude,user.longitude);
                addIcon(iconFactory, user.name, userLocation);
                builder.include(userLocation);
            }
        }

        //Add markers for the organization
        Organization organization = (Organization) getIntent().getSerializableExtra("organization");
        LatLng organizationLocation=new LatLng(organization.getLatitude(),organization.getLongitude());
        iconFactory.setColor(getResources().getColor(R.color.Blue200));
        addIcon(iconFactory, organization.getName(), organizationLocation);

        builder.include(organizationLocation);

        //Set up the camera zoom and position
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);
        map.moveCamera(cu);
    }

    private void addIcon(IconGenerator iconFactory, CharSequence text, LatLng position) {
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).
                position(position).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        map.addMarker(markerOptions);
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
