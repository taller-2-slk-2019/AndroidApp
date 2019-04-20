package com.taller2.hypechatapp.services;

import com.taller2.hypechatapp.firebase.FirebaseAuthService;
import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.network.ApiClient;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.OrganizationApi;
import com.taller2.hypechatapp.network.model.AcceptInvitationRequest;
import com.taller2.hypechatapp.network.model.OrganizationRequest;
import com.taller2.hypechatapp.network.model.SuccessResponse;
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

    public void inviteUsers(Integer organizationId, UserInvitationRequest userInvitationRequest, final Client client){
        organizationApi.inviteUsers(organizationId,userInvitationRequest).enqueue(new Callback<SuccessResponse>(){

            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                manageSuccessResponse(response,SERVICE_TAG,client);
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                manageFailure(SERVICE_TAG,t,client);
            }
        });
    }

    public void acceptInvitation(AcceptInvitationRequest acceptInvitationRequest, final Client client){
        organizationApi.acceptInvitation(acceptInvitationRequest).enqueue(new Callback<SuccessResponse>(){

            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                manageSuccessResponse(response,SERVICE_TAG,client);
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                manageFailure(SERVICE_TAG,t,client);
            }
        });
    }
}
