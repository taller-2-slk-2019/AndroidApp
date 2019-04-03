package com.taller2.hypechatapp.services;


import com.taller2.hypechatapp.model.Channel;
import com.taller2.hypechatapp.network.ApiClient;
import com.taller2.hypechatapp.network.ChannelApi;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.model.ChannelRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelService extends RestService {

    private ChannelApi channelApi;

    static final String SERVICE_TAG = "CHANNELSERVICE";

    public ChannelService() {
        this.channelApi = ApiClient.getInstance().getChannelClient();
    }

    public void getChannelsByOrganizationAndUser(Integer organizationId, Integer userId, final Client client) {
        channelApi.getChannels(organizationId, userId).enqueue(new Callback<List<Channel>>() {
            @Override
            public void onResponse(Call<List<Channel>> call, Response<List<Channel>> response) {
                manageSuccessResponse(response, SERVICE_TAG, client);
            }

            @Override
            public void onFailure(Call<List<Channel>> call, Throwable t) {
                manageFailure(SERVICE_TAG, t, client);
            }
        });
    }

    public void createChannel(ChannelRequest channelRequest, final Client client) {
        channelApi.createChannel(channelRequest).enqueue(new Callback<Channel>() {
            @Override
            public void onResponse(Call<Channel> call, Response<Channel> response) {
                manageSuccessResponse(response, SERVICE_TAG, client);
            }

            @Override
            public void onFailure(Call<Channel> call, Throwable t) {
                manageFailure(SERVICE_TAG, t, client);
            }
        });
    }
}
