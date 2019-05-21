package com.taller2.hypechatapp.services;

import com.taller2.hypechatapp.firebase.FirebaseAuthService;
import com.taller2.hypechatapp.network.ApiClient;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.FirebaseApi;
import com.taller2.hypechatapp.network.NetworkCallback;
import com.taller2.hypechatapp.network.model.TokenResponse;

public class FirebaseApiService extends RestService {

    private FirebaseApi firebaseApi;

    static final String SERVICE_TAG = "FIREBASEAPISERVICE";

    public FirebaseApiService() {
        this.firebaseApi = ApiClient.getInstance().getFirebaseApiClient();
    }

    public void updateFCMtoken(String token, final Client client) {
        TokenResponse data = new TokenResponse();
        data.setToken(token);
        firebaseApi.updateFCMtoken(FirebaseAuthService.getCurrentUserToken(), data)
                .enqueue(new NetworkCallback<Void>(SERVICE_TAG, client));
    }

    public void deleteFCMtoken(String token, final Client client) {
        firebaseApi.deleteFCMtoken(token)
                .enqueue(new NetworkCallback<Void>(SERVICE_TAG, client));
    }
}
