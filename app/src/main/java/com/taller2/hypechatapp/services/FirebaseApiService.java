package com.taller2.hypechatapp.services;

import com.taller2.hypechatapp.firebase.FirebaseAuthService;
import com.taller2.hypechatapp.network.ApiClient;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.FirebaseApi;
import com.taller2.hypechatapp.network.model.NoResponse;
import com.taller2.hypechatapp.network.model.TokenResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseApiService extends RestService {

    private FirebaseApi firebaseApi;

    static final String SERVICE_TAG = "FIREBASEAPISERVICE";

    public FirebaseApiService() {
        this.firebaseApi = ApiClient.getInstance().getFirebaseApiClient();
    }

    public void updateFCMtoken(String token, final Client client) {
        TokenResponse data = new TokenResponse();
        data.setToken(token);
        firebaseApi.updateFCMtoken(FirebaseAuthService.getCurrentUserToken(), data).enqueue(new Callback<NoResponse>() {
            @Override
            public void onResponse(Call<NoResponse> call, Response<NoResponse> response) {
                manageSuccessResponse(response, SERVICE_TAG, client);
            }

            @Override
            public void onFailure(Call<NoResponse> call, Throwable t) {
                manageFailure(SERVICE_TAG, t, client);
            }
        });
    }

    public void deleteFCMtoken(String token, final Client client) {
        firebaseApi.deleteFCMtoken(token).enqueue(new Callback<NoResponse>() {
            @Override
            public void onResponse(Call<NoResponse> call, Response<NoResponse> response) {
                manageSuccessResponse(response, SERVICE_TAG, client);
            }

            @Override
            public void onFailure(Call<NoResponse> call, Throwable t) {
                manageFailure(SERVICE_TAG, t, client);
            }
        });
    }
}
