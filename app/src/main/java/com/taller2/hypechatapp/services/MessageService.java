package com.taller2.hypechatapp.services;

import com.taller2.hypechatapp.firebase.FirebaseAuthService;
import com.taller2.hypechatapp.model.Message;
import com.taller2.hypechatapp.network.ApiClient;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.MessageApi;
import com.taller2.hypechatapp.network.NetworkCallback;

import java.util.List;

public class MessageService extends RestService {

    private MessageApi messagesApi;

    private static final String SERVICE_TAG = "MESSAGESERVICE";

    public MessageService() {
        this.messagesApi = ApiClient.getInstance().getMessageClient();
    }

    public void getChatMessages(Integer channelId, Integer conversationId, Integer offset, final Client client) {
        messagesApi.getChannelMessages(channelId, conversationId, offset)
                .enqueue(new NetworkCallback<List<Message>>(SERVICE_TAG, client));
    }

    public void createMessage(Message message, final Client client) {
        messagesApi.createMessage(FirebaseAuthService.getCurrentUserToken(), message)
                .enqueue(new NetworkCallback<Void>(SERVICE_TAG, client));
    }

}
