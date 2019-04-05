package com.taller2.hypechatapp.network;

import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private final static String API_BASE_URL = "http://10.0.2.2:3000/";

    private static ApiClient instance;

    private Retrofit retrofit;
    private Retrofit retrofitExecutorCallback;

    private ApiClient() {
        buildRetrofit();
    }

    public synchronized static ApiClient getInstance() {
        if (instance == null)
            instance = new ApiClient();
        return instance;
    }

    private void buildRetrofit() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());

        retrofit = builder
                .client(httpClient.build())
                .build();


        builder = new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .callbackExecutor(Executors.newSingleThreadExecutor())
                        .addConverterFactory(GsonConverterFactory.create());

        retrofitExecutorCallback = builder
                .client(httpClient.build())
                .build();

    }


    public OrganizationApi getOrganizationClient() {
        return getOrganizationClient(false);
    }

    public OrganizationApi getOrganizationClient(boolean withCallbackExecutor) {
        if (withCallbackExecutor) {
            return retrofitExecutorCallback.create(OrganizationApi.class);
        }
        return retrofit.create(OrganizationApi.class);
    }

    public UserApi getUserClient() {
        return retrofit.create(UserApi.class);
    }

    public ChannelApi getChannelClient() {
        return getChannelClient(false);
    }

    public ChannelApi getChannelClient(boolean withCallbackExecutor) {
        if (withCallbackExecutor) {
            return retrofitExecutorCallback.create(ChannelApi.class);
        }
        return retrofit.create(ChannelApi.class);
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}