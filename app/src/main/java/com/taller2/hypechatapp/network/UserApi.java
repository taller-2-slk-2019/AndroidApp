package com.taller2.hypechatapp.network;

import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.model.ReceivedInvitation;
import com.taller2.hypechatapp.model.UserStatistics;
import com.taller2.hypechatapp.network.model.UserLocationRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {

    @GET("/users/profile")
    Call<User> getUser(@Query("userToken") String userToken);

    @GET("/users/{id}")
    Call<User> getUserProfile(@Path("id") int userId);

    @GET("/users")
    Call<List<User>> getUsersByOrganization(@Query("organizationId") Integer organizationId);

    @PUT("/users")
    Call<Void> updateUser(@Query("userToken") String userToken, @Body User user);

    @POST("/users")
    Call<User> registerUser(@Body User user);

    @PUT("/users/location")
    Call<Void> updateUserLocation(@Query("userToken") String userToken, @Body UserLocationRequest userLocationRequest);

    @GET("/users/invitations")
    Call<List<ReceivedInvitation>> getReceivedInvitations(@Query("userToken") String userToken);

    @GET("/users/statistics")
    Call<UserStatistics> getStatistics(@Query("userToken") String userToken);

    @GET("/users/{id}/statistics")
    Call<UserStatistics> getUserStatistics(@Path("id") int userId);

    @DELETE("/users/invitations/{invitationToken}")
    Call<Void> rejectInvitation(@Path("invitationToken") String invitationToken);
}
