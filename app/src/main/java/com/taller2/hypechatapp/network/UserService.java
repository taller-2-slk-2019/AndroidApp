package com.taller2.hypechatapp.network;

import android.util.Log;

import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService {

    private UserApi userApi;

    static final String SERVICE_TAG = "USERSERVICE";

    public UserService() {
        this.userApi = ApiClient.getInstance().getUserClient();
    }

    public void getUser(Integer userId, final Client client){
        userApi.getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() > 199 && response.code() < 300) {
                    if (response.body() != null) {
                        Log.i(SERVICE_TAG, response.body().toString());
                        client.onResponseSuccess(response.body());
                    } else {
                        Log.i(SERVICE_TAG, "NO RESPONSE");
                        client.onResponseError(null);
                    }
                } else {
                    if(response.body() != null) {
                        Log.e(SERVICE_TAG, response.body().toString());
                    }else {
                        Log.e(SERVICE_TAG, "NO RESPONSE");
                    }
                    client.onResponseError(null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(SERVICE_TAG, t.getMessage());
                client.onResponseError(null);
            }
        });
    }
}
