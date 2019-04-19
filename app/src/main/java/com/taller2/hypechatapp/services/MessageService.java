package com.taller2.hypechatapp.services;

import com.taller2.hypechatapp.firebase.FirebaseAuthService;
import com.taller2.hypechatapp.model.Message;
import com.taller2.hypechatapp.network.ApiClient;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.MessageApi;
import com.taller2.hypechatapp.network.model.NoResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageService extends RestService {

    private MessageApi messagesApi;

    private static final String SERVICE_TAG = "MESSAGESERVICE";

    public MessageService() {
        this.messagesApi = ApiClient.getInstance().getMessageClient();
    }

    public void getChatMessages(Integer channelId, Integer conversationId, Integer offset, final Client client) {
        messagesApi.getChannelMessages(channelId, conversationId, offset).enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                manageSuccessResponse(response, SERVICE_TAG, client);
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                manageFailure(SERVICE_TAG, t, client);
            }
        });
    }

    public void createMessage(Message message, final Client client) {
        messagesApi.createMessage(FirebaseAuthService.getCurrentUserToken(), message).enqueue(new Callback<NoResponse>() {
            @Override
            public void onResponse(Call<NoResponse> call, Response<NoResponse> response) {
                manageSuccessResponse(response, SERVICE_TAG, client);
            }

            @Override
            public void onFailure(Call<NoResponse> call, Throwable t) {
                manageFailure(SERVICE_TAG, t, client);
            }
        });
    }

}
