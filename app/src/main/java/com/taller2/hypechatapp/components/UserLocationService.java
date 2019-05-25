package com.taller2.hypechatapp.components;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class UserLocationService {

    @SuppressLint("MissingPermission") // permission must be requested
    public static void getUserLocation(Activity activity, final UserLocationListener listener) {
        FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(activity);

        locationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Logic to handle location object
                            Log.d("User location", "Latitude: " + location.getLatitude());
                            Log.d("User location", "Longitude: " + location.getLongitude());
                            listener.userLocationReceived(location);
                        } else {
                            listener.userLocationError();
                        }
                    }
                });
    }

    public interface UserLocationListener {

        void userLocationReceived(Location location);

        void userLocationError();
    }
}
