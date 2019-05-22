package com.taller2.hypechatapp.services;

import com.taller2.hypechatapp.firebase.FirebaseAuthService;
import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.network.ApiClient;
import com.taller2.hypechatapp.network.Client;
import com.taller2.hypechatapp.network.NetworkCallback;
import com.taller2.hypechatapp.network.OrganizationApi;
import com.taller2.hypechatapp.network.model.AcceptInvitationRequest;
import com.taller2.hypechatapp.network.model.OrganizationRequest;
import com.taller2.hypechatapp.network.model.RoleRequest;
import com.taller2.hypechatapp.network.model.UserInvitationRequest;

import java.util.List;

public class OrganizationService extends RestService {

    private OrganizationApi organizationApi;

    static final String SERVICE_TAG = "ORGANIZATIONSERVICE";

    public OrganizationService() {
        this.organizationApi = ApiClient.getInstance().getOrganizationClient();
    }

    public void getOrganizationProfile(Integer organizationId, final Client client) {
        organizationApi.getOrganizationProfile(organizationId)
                .enqueue(new NetworkCallback<Organization>(SERVICE_TAG, client));
    }

    public void createOrganization(OrganizationRequest organizationRequest, final Client client) {
        organizationApi.createOrganization(FirebaseAuthService.getCurrentUserToken(), organizationRequest)
                .enqueue(new NetworkCallback<Organization>(SERVICE_TAG, client));
    }

    public void getOrganizationsByUser(final Client client) {
        String userToken = FirebaseAuthService.getCurrentUserToken();
        organizationApi.getOrganizationsByUser(userToken)
                .enqueue(new NetworkCallback<List<Organization>>(SERVICE_TAG, client));
    }

    public void inviteUsers(Integer organizationId, UserInvitationRequest userInvitationRequest, final Client client) {
        organizationApi.inviteUsers(organizationId, userInvitationRequest, FirebaseAuthService.getCurrentUserToken())
                .enqueue(new NetworkCallback<List<String>>(SERVICE_TAG, client));
    }

    public void acceptInvitation(AcceptInvitationRequest acceptInvitationRequest, final Client client) {
        organizationApi.acceptInvitation(acceptInvitationRequest)
                .enqueue(new NetworkCallback<Void>(SERVICE_TAG, client));
    }

    public void updateOrganization(int organizationId, Organization organization, final Client client) {
        organizationApi.updateOrganization(organizationId, FirebaseAuthService.getCurrentUserToken(), organization)
                .enqueue(new NetworkCallback<Void>(SERVICE_TAG, client));
    }

    public void updateUserRole(int organizationId, int userId, String role, final Client client) {
        RoleRequest roleRequest = new RoleRequest(role);
        organizationApi.updateUserRole(organizationId, userId, FirebaseAuthService.getCurrentUserToken(), roleRequest)
                .enqueue(new NetworkCallback<Void>(SERVICE_TAG, client));
    }

    public void removeUser(int organizationId, int userId, final Client client) {
        organizationApi.removeUser(organizationId, userId, FirebaseAuthService.getCurrentUserToken())
                .enqueue(new NetworkCallback<Void>(SERVICE_TAG, client));
    }
}
