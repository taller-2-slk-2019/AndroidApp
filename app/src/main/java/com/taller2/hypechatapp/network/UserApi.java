package com.taller2.hypechatapp.network;

import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.network.model.ConfirmationResponse;
import com.taller2.hypechatapp.network.model.UserLocationRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {

    @GET("/users/{id}")
    Call<User> getUser(@Path("id") Integer userId);

    @PUT("/users/{id}")
    Call<ConfirmationResponse> updateUser(@Path("id") Integer userId, @Body User user);

    @POST("/users")
    Call<User> registerUser(@Body User user);

    @PUT("/users/{userId}/location")
    Call<ConfirmationResponse> updateUserLocation(@Path("userId") Integer userId, @Body UserLocationRequest userLocationRequest);
}
