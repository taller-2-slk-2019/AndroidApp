package com.taller2.hypechatapp.network;

import android.util.Log;

import com.taller2.hypechatapp.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelService {

    private ChannelApi channelApi;

    static final String SERVICE_TAG = "CHANNELSERVICE";

    public ChannelService() {
        this.channelApi = ApiClient.getInstance().getChannelClient();
    }


}
