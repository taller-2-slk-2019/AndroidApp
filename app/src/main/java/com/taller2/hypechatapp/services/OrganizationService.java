package com.taller2.hypechatapp.services;

import com.taller2.hypechatapp.firebase.FirebaseAuthService;
import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.network.ApiClient;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.OrganizationApi;
import com.taller2.hypechatapp.network.model.OrganizationRequest;
import com.taller2.hypechatapp.network.model.TokenResponse;
import com.taller2.hypechatapp.network.model.UserInvitationRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrganizationService extends RestService {

    private OrganizationApi organizationApi;

    static final String SERVICE_TAG = "ORGANIZATIONSERVICE";

    public OrganizationService() {
        this.organizationApi = ApiClient.getInstance().getOrganizationClient();
    }

    public void getOrganizationProfile(Integer organizationId, final Client client){
        organizationApi.getOrganizationProfile(organizationId).enqueue(new Callback<Organization>() {
            @Override
            public void onResponse(Call<Organization> call, Response<Organization> response) {
                manageSuccessResponse(response,SERVICE_TAG,client);
            }

            @Override
            public void onFailure(Call<Organization> call, Throwable t) {
                manageFailure(SERVICE_TAG,t,client);
            }
        });
    }

    public void createOrganization(OrganizationRequest organizationRequest, final Client client){
        organizationApi.createOrganization(FirebaseAuthService.getCurrentUserToken(), organizationRequest).enqueue(new Callback<Organization>(){

            @Override
            public void onResponse(Call<Organization> call, Response<Organization> response) {
                manageSuccessResponse(response,SERVICE_TAG,client);
            }

            @Override
            public void onFailure(Call<Organization> call, Throwable t) {
                manageFailure(SERVICE_TAG,t,client);
            }
        });
    }

    public void getOrganizationsByUser(final Client client){
        String userToken = FirebaseAuthService.getCurrentUserToken();
        organizationApi.getOrganizationsByUser(userToken).enqueue(new Callback<List<Organization>>() {
            @Override
            public void onResponse(Call<List<Organization>> call, Response<List<Organization>> response) {
                manageSuccessResponse(response, this.getClass().getSimpleName(), client);
            }

            @Override
            public void onFailure(Call<List<Organization>> call, Throwable t) {
                manageFailure(this.getClass().getSimpleName(), t, client);
            }
        });
    }

    public void inviteUser(Integer organizationId, UserInvitationRequest userInvitationRequest, final Client client){
        organizationApi.inviteUser(organizationId,userInvitationRequest).enqueue(new Callback<TokenResponse>(){

            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                manageSuccessResponse(response,SERVICE_TAG,client);
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                manageFailure(SERVICE_TAG,t,client);
            }
        });
    }
}
