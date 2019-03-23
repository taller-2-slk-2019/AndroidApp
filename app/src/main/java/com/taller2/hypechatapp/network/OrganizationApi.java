package com.taller2.hypechatapp.network;

import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.network.model.OrganizationRequest;
import com.taller2.hypechatapp.network.model.TokenResponse;
import com.taller2.hypechatapp.network.model.UserInvitationRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrganizationApi {

    @GET("/organizations/{id}")
    Call<Organization> getOrganization(@Path("id")Integer organizationId);

    @POST("/organizations/create")
    Call<Organization> createOrganization(@Body OrganizationRequest organization);

    @PATCH("/organizations/{organizationId}/inviteUser")
    Call<TokenResponse> inviteUser(@Path("organizationId") Integer organizationId, @Body UserInvitationRequest userInvitationRequest);
}
