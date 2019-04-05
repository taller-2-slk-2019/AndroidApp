package com.taller2.hypechatapp.network;

import com.taller2.hypechatapp.model.Channel;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.network.model.ChannelRequest;
import com.taller2.hypechatapp.network.model.UserLocationRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChannelApi {

    @GET("/channels")
    Call<List<Channel>> getChannels(@Query("organizationId") Integer organizationId,
                                    @Query("userId") Integer userId);

    @POST("/channels")
    Call<Channel> createChannel(@Body ChannelRequest channel);
}
