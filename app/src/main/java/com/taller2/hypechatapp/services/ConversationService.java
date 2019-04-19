package com.taller2.hypechatapp.services;

import com.taller2.hypechatapp.firebase.FirebaseAuthService;
import com.taller2.hypechatapp.model.Conversation;
import com.taller2.hypechatapp.network.ApiClient;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.ConversationApi;
import com.taller2.hypechatapp.network.model.ConversationRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversationService extends RestService {

    private ConversationApi conversationApi;

    static final String SERVICE_TAG = "CONVERSATIONSERVICE";

    public ConversationService() {
        this.conversationApi = ApiClient.getInstance().getConversationClient();
    }

    public void getConversationsByOrganizationAndUser(Integer organizationId, final Client client) {
        conversationApi.getConversations(organizationId, FirebaseAuthService.getCurrentUserToken()).enqueue(new Callback<List<Conversation>>() {
            @Override
            public void onResponse(Call<List<Conversation>> call, Response<List<Conversation>> response) {
                manageSuccessResponse(response, SERVICE_TAG, client);
            }

            @Override
            public void onFailure(Call<List<Conversation>> call, Throwable t) {
                manageFailure(SERVICE_TAG, t, client);
            }
        });
    }

    public void createConversation(ConversationRequest conversation, final Client client) {
        conversationApi.createConversation(FirebaseAuthService.getCurrentUserToken(), conversation).enqueue(new Callback<Conversation>() {
            @Override
            public void onResponse(Call<Conversation> call, Response<Conversation> response) {
                manageSuccessResponse(response, SERVICE_TAG, client);
            }

            @Override
            public void onFailure(Call<Conversation> call, Throwable t) {
                manageFailure(SERVICE_TAG, t, client);
            }
        });
    }
}
