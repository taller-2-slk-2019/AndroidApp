package com.taller2.hypechatapp.network;

import com.taller2.hypechatapp.model.Conversation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ConversationApi {

    @GET("/conversations")
    Call<List<Conversation>> getConversations(@Query("organizationId") Integer organizationId,
                                         @Query("userToken") String userToken);
}
