package com.taller2.hypechatapp.network;

import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.network.model.AcceptInvitationRequest;
import com.taller2.hypechatapp.network.model.OrganizationRequest;
import com.taller2.hypechatapp.network.model.UserInvitationRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrganizationApi {

    @GET("/organizations/{id}")
    Call<Organization> getOrganizationProfile(@Path("id") Integer organizationId);

    @GET("/organizations")
    Call<List<Organization>> getOrganizationsByUser(@Query("userToken") String userToken);

    @POST("/organizations/")
    Call<Organization> createOrganization(@Query("userToken") String userToken, @Body OrganizationRequest organization);

    @POST("/organizations/{organizationId}/invitations")
    Call<List<String>> inviteUsers(@Path("organizationId") Integer organizationId,
                                   @Body UserInvitationRequest userInvitationRequest,
                                   @Query("userToken") String userToken);

    @POST("/organizations/users")
    Call<Void> acceptInvitation(@Body AcceptInvitationRequest organization);
}
