package com.taller2.hypechatapp.services;

import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.network.ApiClient;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.UserApi;
import com.taller2.hypechatapp.network.model.ConfirmationResponse;
import com.taller2.hypechatapp.network.model.UserLocationRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService extends RestService {

    private UserApi userApi;

    static final String SERVICE_TAG = "USERSERVICE";

    public UserService() {
        this.userApi = ApiClient.getInstance().getUserClient();
    }

    public void getUser(Integer userId, final Client client){
        userApi.getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                manageSuccessResponse(response,SERVICE_TAG,client);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                manageFailure(SERVICE_TAG,t,client);
            }
        });
    }

    public void updateUser(Integer userId, User user, final Client client){
        userApi.updateUser(userId, user).enqueue(new Callback<ConfirmationResponse>() {
            @Override
            public void onResponse(Call<ConfirmationResponse> call, Response<ConfirmationResponse> response) {
                manageSuccessResponse(response,SERVICE_TAG,client);

            }

            @Override
            public void onFailure(Call<ConfirmationResponse> call, Throwable t) {
                manageFailure(SERVICE_TAG,t,client);
            }
        });
    }

    public void updateUserLocation(Integer userId, UserLocationRequest userLocationRequest, final Client client){
        userApi.updateUserLocation(userId,userLocationRequest).enqueue(new Callback<ConfirmationResponse>() {
            @Override
            public void onResponse(Call<ConfirmationResponse> call, Response<ConfirmationResponse> response) {
                manageSuccessResponse(response,SERVICE_TAG,client);
            }

            @Override
            public void onFailure(Call<ConfirmationResponse> call, Throwable t) {
                manageFailure(SERVICE_TAG,t,client);
            }
        });
    }
}
