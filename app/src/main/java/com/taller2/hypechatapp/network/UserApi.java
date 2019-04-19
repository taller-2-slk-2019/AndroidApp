package com.taller2.hypechatapp.network;

import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.network.model.ConfirmationResponse;
import com.taller2.hypechatapp.network.model.UserLocationRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserApi {

    @GET("/users/profile")
    Call<User> getUser(@Query("userToken") String userToken);

    @GET("/users")
    Call<List<User>> getUsersByOrganization(@Query("organizationId") Integer organizationId);

    @PUT("/users")
    Call<ConfirmationResponse> updateUser(@Query("userToken") String userToken, @Body User user);

    @POST("/users")
    Call<User> registerUser(@Body User user);

    @PUT("/users/location")
    Call<ConfirmationResponse> updateUserLocation(@Query("userToken") String userToken, @Body UserLocationRequest userLocationRequest);
}
