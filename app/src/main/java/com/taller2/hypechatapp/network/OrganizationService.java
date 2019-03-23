package com.taller2.hypechatapp.network;

import android.util.Log;

import com.taller2.hypechatapp.model.Organization;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrganizationService {

    private OrganizationApi organizationApi;

    static final String SERVICE_TAG = "ORGANIZATIONSERVICE";

    public OrganizationService() {
        this.organizationApi = ApiClient.getInstance().getOrganizationClient();
    }

    public void getOrganization(Integer organizationId, final Client client){
        organizationApi.getOrganization(organizationId).enqueue(new Callback<Organization>() {
            @Override
            public void onResponse(Call<Organization> call, Response<Organization> response) {
                if (response.code() > 199 && response.code() < 300) {
                    if (response.body() != null) {
                        Log.i(SERVICE_TAG, response.body().toString());
                        client.onResponseSuccess(response.body());
                    } else {
                        Log.i(SERVICE_TAG, "NO RESPONSE");
                        client.onResponseError(null);
                    }
                } else {
                    if(response.body() != null) {
                        Log.e(SERVICE_TAG, response.body().toString());
                    }else {
                        Log.e(SERVICE_TAG, "NO RESPONSE");
                    }
                    client.onResponseError(null);
                }
            }

            @Override
            public void onFailure(Call<Organization> call, Throwable t) {
                Log.e(SERVICE_TAG, t.getMessage());
                client.onResponseError(null);
            }
        });
    }
}
