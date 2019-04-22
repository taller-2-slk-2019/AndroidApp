package com.taller2.hypechatapp.services;

import com.taller2.hypechatapp.firebase.FirebaseAuthService;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.network.ApiClient;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.UserApi;
import com.taller2.hypechatapp.network.model.ReceivedInvitation;
import com.taller2.hypechatapp.network.model.UserLocationRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService extends RestService {

    private UserApi userApi;

    static final String SERVICE_TAG = "USERSERVICE";

    public UserService() {
        this.userApi = ApiClient.getInstance().getUserClient();
    }

    public void getUser(final Client client){
        userApi.getUser(FirebaseAuthService.getCurrentUserToken()).enqueue(new Callback<User>() {
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

    public void getUsersByOrganization(int organizationId, final Client client){
        userApi.getUsersByOrganization(organizationId).enqueue(new Callback<List<User>>() {

            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                manageSuccessResponse(response,SERVICE_TAG,client);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                manageFailure(SERVICE_TAG,t,client);
            }
        });
    }

    public void registerUser(User user, final Client client){
        userApi.registerUser(user).enqueue(new Callback<User>() {
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

    public void updateUser(User user, final Client client){
        userApi.updateUser(FirebaseAuthService.getCurrentUserToken(), user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                manageSuccessResponse(response,SERVICE_TAG,client);

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                manageFailure(SERVICE_TAG,t,client);
            }
        });
    }

    public void updateUserLocation(UserLocationRequest userLocationRequest, final Client client){
        userApi.updateUserLocation(FirebaseAuthService.getCurrentUserToken(), userLocationRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                manageSuccessResponse(response,SERVICE_TAG,client);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                manageFailure(SERVICE_TAG,t,client);
            }
        });
    }

    public void getReceivedInvitations(final Client client){
        userApi.getReceivedInvitations(FirebaseAuthService.getCurrentUserToken()).enqueue(new Callback<List<ReceivedInvitation>>() {
            @Override
            public void onResponse(Call<List<ReceivedInvitation>> call, Response<List<ReceivedInvitation>> response) {
                manageSuccessResponse(response,SERVICE_TAG,client);
            }

            @Override
            public void onFailure(Call<List<ReceivedInvitation>> call, Throwable t) {
                manageFailure(SERVICE_TAG,t,client);
            }
        });
    }

    public void rejectInvitation(String invitationToken, final Client client){
        userApi.rejectInvitation(invitationToken).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                manageSuccessResponse(response,SERVICE_TAG,client);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                manageFailure(SERVICE_TAG,t,client);
            }
        });
    }
}
