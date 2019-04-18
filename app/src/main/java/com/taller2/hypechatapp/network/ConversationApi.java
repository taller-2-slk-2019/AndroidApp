package com.taller2.hypechatapp.network;

import com.taller2.hypechatapp.model.Conversation;
import com.taller2.hypechatapp.network.model.ConversationRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ConversationApi {

    @GET("/conversations")
    Call<List<Conversation>> getConversations(@Query("organizationId") Integer organizationId,
                                         @Query("userToken") String userToken);

    @POST("/conversations")
    Call<Conversation> createConversation(@Query("userToken") String userToken, @Body ConversationRequest conversation);
}
