package com.taller2.hypechatapp.services;

import android.util.Log;

import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.model.NoResponse;

import java.io.IOException;

import retrofit2.Response;

public class RestService {


    public void manageSuccessResponse(Response response, String serviceTag, Client client) {
        if (response.code() >= 200 && response.code() < 300) {
            if (response.body() != null) {
                Log.i(serviceTag, response.body().toString());
                client.onResponseSuccess(response.body());
            } else {
                client.onResponseSuccess(new NoResponse());
            }
        } else {
            if (response.body() != null) {
                Log.e(serviceTag, response.body().toString());
            } else {
                try {
                    Log.e(serviceTag, response.message() + ": " + response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            client.onResponseError(null);
        }
    }

    public void manageFailure(String serviceTag, Throwable t, Client client) {
        Log.e(serviceTag, t.getMessage());
        client.onResponseError(null);
    }
}
