package com.taller2.hypechatapp.components;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class UserLocationService {

    private static String TAG = "User location";

    @SuppressLint("MissingPermission") // permission must be requested
    public static void getUserLocation(Activity activity, final UserLocationListener listener) {
        FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(activity);

        LocationRequest request = LocationRequest.create();
        request.setNumUpdates(1);
        request.setExpirationDuration(2000);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.d(TAG, "No location");
                    listener.userLocationError();
                } else {
                    Location location = locationResult.getLastLocation();
                    Log.d(TAG, "Latitude: " + location.getLatitude());
                    Log.d(TAG, "Longitude: " + location.getLongitude());
                    listener.userLocationReceived(location);
                }
            }
        };

        locationClient.requestLocationUpdates(request, locationCallback, null);
    }

    public interface UserLocationListener {

        void userLocationReceived(Location location);

        void userLocationError();
    }
}
