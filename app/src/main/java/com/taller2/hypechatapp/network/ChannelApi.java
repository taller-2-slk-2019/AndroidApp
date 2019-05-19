package com.taller2.hypechatapp.network;

import com.taller2.hypechatapp.model.Channel;
import com.taller2.hypechatapp.network.model.ChannelRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChannelApi {

    @GET("/channels")
    Call<List<Channel>> getChannels(@Query("organizationId") Integer organizationId,
                                    @Query("userToken") String userToken,
                                    @Query("userIsMember") Boolean userIsMember);

    @GET("/channels/{id}")
    Call<Channel> getChannelInfo(@Path("id") Integer channelId);

    @POST("/channels")
    Call<Channel> createChannel(@Query("userToken") String userToken, @Body ChannelRequest channel);

    @PUT("/channels/{id}")
    Call<Void> editChannel(@Path("id") Integer channelId,
                           @Query("userToken") String userToken,
                           @Body ChannelRequest channel);

    @POST("/channels/{channelId}/users")
    Call<Void> addUserToChannel(@Path("channelId") Integer channelId, @Query("userToken") String userToken);

    @DELETE("/channels/{channelId}/users")
    Call<Void> abandonChannel(@Path("channelId") Integer channelId, @Query("userToken") String userToken);
}
