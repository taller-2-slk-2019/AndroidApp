package com.taller2.hypechatapp.network;

import com.taller2.hypechatapp.model.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MessageApi {

    @GET("/messages")
    Call<List<Message>> getChannelMessages(@Query("channelId") Integer channelId,
                                    @Query("conversationId") Integer conversationId,
                                    @Query("offset") Integer offset);

    @POST("/messages")
    Call<Void> createMessage(@Query("userToken") String userToken, @Body Message message);

}
