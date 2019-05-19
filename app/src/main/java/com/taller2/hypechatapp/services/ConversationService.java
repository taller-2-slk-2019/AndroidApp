package com.taller2.hypechatapp.services;

import com.taller2.hypechatapp.firebase.FirebaseAuthService;
import com.taller2.hypechatapp.model.Conversation;
import com.taller2.hypechatapp.network.ApiClient;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.ConversationApi;
import com.taller2.hypechatapp.network.NetworkCallback;
import com.taller2.hypechatapp.network.model.ConversationRequest;

import java.util.List;

public class ConversationService extends RestService {

    private ConversationApi conversationApi;

    static final String SERVICE_TAG = "CONVERSATIONSERVICE";

    public ConversationService() {
        this.conversationApi = ApiClient.getInstance().getConversationClient();
    }

    public void getConversationsByOrganizationAndUser(Integer organizationId, final Client client) {
        conversationApi.getConversations(organizationId, FirebaseAuthService.getCurrentUserToken())
                .enqueue(new NetworkCallback<List<Conversation>>(SERVICE_TAG, client));
    }

    public void createConversation(ConversationRequest conversation, final Client client) {
        conversationApi.createConversation(FirebaseAuthService.getCurrentUserToken(), conversation)
                .enqueue(new NetworkCallback<Conversation>(SERVICE_TAG, client));
    }
}
