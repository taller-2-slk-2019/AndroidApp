package com.taller2.hypechatapp.components;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class UserLocationService {

    private static String TAG = "User location";
    private static int EXPIRATION = 10000;

    @SuppressLint("MissingPermission") // permission must be requested
    public static void getUserLocation(Activity activity, final UserLocationListener listener) {
        FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(activity);

        final Handler expirationHandler = new Handler();
        final Runnable expirationAction = new Runnable() {
            @Override
            public void run() {
                listener.userLocationError();
                Log.d(TAG, "Location request expired");
            }
        };

        LocationRequest request = LocationRequest.create()
                .setNumUpdates(1)
                .setInterval(500)
                .setFastestInterval(500)
                .setExpirationDuration(EXPIRATION);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                expirationHandler.removeCallbacks(expirationAction);
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
        expirationHandler.postDelayed(expirationAction, EXPIRATION + 100);
    }

    public interface UserLocationListener {

        void userLocationReceived(Location location);

        void userLocationError();
    }
}
