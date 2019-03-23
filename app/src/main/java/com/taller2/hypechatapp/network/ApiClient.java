package com.taller2.hypechatapp.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private final static String API_BASE_URL = "https://slack-taller2.herokuapp.com/";

    private static ApiClient instance;
    private Retrofit retrofit;

    private ApiClient() {
        buildRetrofit();
    }

    public synchronized static ApiClient getInstance() {
        if (instance == null)
            instance = new ApiClient();
        return instance;
    }

    private void buildRetrofit() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());

        retrofit = builder
                .client(httpClient.build())
                .build();
    }

    public OrganizationApi getOrganizationClient() {
        return retrofit.create(OrganizationApi.class);
    }

    public UserApi getUserClient() {
        return retrofit.create(UserApi.class);
    }

    public ChannelApi getChannelClient() {
        return retrofit.create(ChannelApi.class);
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}