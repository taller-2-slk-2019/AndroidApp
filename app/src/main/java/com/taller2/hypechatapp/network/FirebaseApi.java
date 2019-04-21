package com.taller2.hypechatapp.network;

import com.taller2.hypechatapp.network.model.NoResponse;
import com.taller2.hypechatapp.network.model.TokenResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FirebaseApi {

    @POST("/firebase/fcm/tokens")
    Call<NoResponse> updateFCMtoken(@Query("userToken") String userToken, @Body TokenResponse token);

    @DELETE("/firebase/fcm/tokens/{fcmToken}")
    Call<NoResponse> deleteFCMtoken(@Path("fcmToken") String fcmToken);
}
