package com.taller2.hypechatapp.services;

import android.content.Context;

import com.taller2.hypechatapp.firebase.FirebaseAuthService;
import com.taller2.hypechatapp.model.Channel;
import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.network.ApiClient;
import com.taller2.hypechatapp.network.ChannelApi;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.OrganizationApi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigationMenuService extends RestService {

    private ChannelApi channelApi;

    public NavigationMenuService() {
        this.channelApi = ApiClient.getInstance().getChannelClient(true);
    }

    public void getNavigationMenuData(Integer organizationId, final Client client) {
        String userToken = FirebaseAuthService.getCurrentUserToken();
        Call<List<Channel>> channels = channelApi.getChannels(organizationId, userToken);

        List<Call> apiCalls = new ArrayList<>();
        apiCalls.add(channels);

        executeAndWaitAll(apiCalls, client);
    }

    private void executeAndWaitAll(List<Call> calls, final Client client) {
        final CountDownLatch apiCalls = new CountDownLatch(calls.size());
        final List<Comparable> result = new ArrayList<>();
        for (Call<List<Comparable>> call: calls) {
            call.enqueue(new Callback<List<Comparable>>() {
                @Override
                public void onResponse(Call<List<Comparable>> call, Response<List<Comparable>> response) {
                    manageSuccessResponse(response, this.getClass().getSimpleName(), new Client<List<Comparable>>() {
                        @Override
                        public void onResponseSuccess(List<Comparable> responseBody) {
                            result.addAll(responseBody);
                            apiCalls.countDown();
                        }

                        @Override
                        public void onResponseError(String errorMessage) {
                            apiCalls.countDown();
                        }

                        @Override
                        public Context getContext() {
                            return null;
                        }
                    });
                }

                @Override
                public void onFailure(Call<List<Comparable>> call, Throwable t) {
                    apiCalls.countDown();
                    client.onResponseError(t.getMessage());
                }
            });
        }

        try {
            apiCalls.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        client.onResponseSuccess(result);

    }
}
