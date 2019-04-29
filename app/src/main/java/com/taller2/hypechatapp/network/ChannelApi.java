package com.taller2.hypechatapp.network;

import com.taller2.hypechatapp.model.Channel;
import com.taller2.hypechatapp.network.model.ChannelRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ChannelApi {

    @GET("/channels")
    Call<List<Channel>> getChannels(@Query("organizationId") Integer organizationId,
                                    @Query("userToken") String userToken,
                                    @Query("userIsMember") Boolean userIsMember);

    @POST("/channels")
    Call<Channel> createChannel(@Query("userToken") String userToken, @Body ChannelRequest channel);
}
