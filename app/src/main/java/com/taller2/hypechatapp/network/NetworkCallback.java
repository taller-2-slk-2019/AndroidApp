package com.taller2.hypechatapp.network;

import com.taller2.hypechatapp.services.RestService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkCallback<T> implements Callback<T> {

    private final String tag;
    private final Client<T> client;

    public NetworkCallback(String tag, Client<T> client) {
        this.tag = tag;
        this.client = client;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        RestService.manageSuccessResponse(response, tag, client);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        RestService.manageFailure(tag, t, client);
    }
}
