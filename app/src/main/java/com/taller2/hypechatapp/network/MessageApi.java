package com.taller2.hypechatapp.network;

import com.taller2.hypechatapp.model.Message;
import com.taller2.hypechatapp.network.model.SuccessResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MessageApi {

    @GET("/messages")
    Call<List<Message>> getChannelMessages(@Query("channelId") Integer channelId,
                                    @Query("offset") Integer offset);

    @POST("/messages")
    Call<SuccessResponse> createMessage(@Query("userToken") String userToken, @Body Message message);

}
