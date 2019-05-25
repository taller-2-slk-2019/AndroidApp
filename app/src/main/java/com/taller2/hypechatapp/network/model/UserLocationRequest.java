package com.taller2.hypechatapp.network.model;

import android.location.Location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLocationRequest {

    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;

    public UserLocationRequest(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
