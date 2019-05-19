package com.taller2.hypechatapp.services;


import com.taller2.hypechatapp.firebase.FirebaseAuthService;
import com.taller2.hypechatapp.model.Channel;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.network.ApiClient;
import com.taller2.hypechatapp.network.ChannelApi;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.NetworkCallback;
import com.taller2.hypechatapp.network.model.ChannelRequest;

import java.util.List;

public class ChannelService extends RestService {

    private ChannelApi channelApi;

    static final String SERVICE_TAG = "CHANNELSERVICE";

    public ChannelService() {
        this.channelApi = ApiClient.getInstance().getChannelClient();
    }

    public void getChannelsByOrganizationAndUser(Integer organizationId, final Client client) {
        channelApi.getChannels(organizationId, FirebaseAuthService.getCurrentUserToken(), Boolean.TRUE)
                .enqueue(new NetworkCallback<List<Channel>>(SERVICE_TAG, client));
    }

    public void getAllOrganizationChannels(Integer organizationId, final Client client) {
        channelApi.getAllOrganizationChannels(organizationId)
                .enqueue(new NetworkCallback<List<Channel>>(SERVICE_TAG, client));
    }

    public void getChannelUsers(Integer channelId, final Client client) {
        channelApi.getChannelUsers(channelId)
                .enqueue(new NetworkCallback<List<User>>(SERVICE_TAG, client));
    }

    public void getChannelInfo(Integer channelId, final Client client) {
        channelApi.getChannelInfo(channelId)
                .enqueue(new NetworkCallback<Channel>(SERVICE_TAG, client));
    }

    public void getPublicChannelsByOrganizationAndUser(Integer organizationId, final Client client) {
        channelApi.getChannels(organizationId, FirebaseAuthService.getCurrentUserToken(), Boolean.FALSE)
                .enqueue(new NetworkCallback<List<Channel>>(SERVICE_TAG, client));
    }

    public void createChannel(ChannelRequest channelRequest, final Client client) {
        channelApi.createChannel(FirebaseAuthService.getCurrentUserToken(), channelRequest)
                .enqueue(new NetworkCallback<Channel>(SERVICE_TAG, client));
    }

    public void editChannel(int channelId, ChannelRequest channelRequest, final Client client) {
        channelApi.editChannel(channelId, FirebaseAuthService.getCurrentUserToken(), channelRequest)
                .enqueue(new NetworkCallback<Void>(SERVICE_TAG, client));
    }

    public void addUserToChannel(Integer channelId, final Client client) {
        channelApi.addUserToChannel(channelId, FirebaseAuthService.getCurrentUserToken())
                .enqueue(new NetworkCallback<Void>(SERVICE_TAG, client));
    }

    public void abandonChannel(Integer channelId, final Client client) {
        channelApi.abandonChannel(channelId, FirebaseAuthService.getCurrentUserToken())
                .enqueue(new NetworkCallback<Void>(SERVICE_TAG, client));
    }

    public void removeUser(Integer channelId, Integer userId, final Client client) {
        channelApi.removeUser(channelId, userId, FirebaseAuthService.getCurrentUserToken())
                .enqueue(new NetworkCallback<Void>(SERVICE_TAG, client));
    }
}
