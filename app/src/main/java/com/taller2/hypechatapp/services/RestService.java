package com.taller2.hypechatapp.services;

import android.util.Log;

import com.taller2.hypechatapp.network.Client;

import java.io.IOException;

import retrofit2.Response;

public class RestService {


    public void manageSuccessResponse(Response response, String serviceTag, Client client) {
        if (response.code() >= 200 && response.code() < 300) {
            client.onResponseSuccess(response.body());
            if (response.body() != null) {
                Log.i(serviceTag, response.body().toString());
            }
        } else {
            try {
                Log.e(serviceTag, response.message() + ": " + response.errorBody().string());
                client.onResponseError(false, response.errorBody().string());
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
            client.onResponseError(false, "");
        }
    }

    public void manageFailure(String serviceTag, Throwable t, Client client) {
        Log.e(serviceTag, t.getMessage());
        client.onResponseError(true, "");
    }
}
