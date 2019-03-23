package com.taller2.hypechatapp.network;

import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.network.model.OrganizationRequest;
import com.taller2.hypechatapp.network.model.TokenResponse;
import com.taller2.hypechatapp.network.model.UserInvitationRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.*;

public class OrganizationApiTest {

    OrganizationApi organizationApi;

    @Before
    public void setUp() throws Exception {
        organizationApi=ApiClient.getInstance().getOrganizationClient();
    }

    @Test
    public void getOrganization() {
        Integer idToTest=1;
        try {
            Response<Organization> response=organizationApi.getOrganization(idToTest).execute();
            Assert.assertEquals(idToTest,response.body().getId());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void createOrganization(){

        OrganizationRequest organizationRequest=new OrganizationRequest();
        organizationRequest.setName("Orga Prueba");
        organizationRequest.setLatitude(new Double(10));
        organizationRequest.setLongitude(new Double(20));
        organizationRequest.setPicture("foto.jpg");
        organizationRequest.setDescription("Esta es la descripcion");
        organizationRequest.setWelcome("Mensaje de bienvenida");
        organizationRequest.setCreatorId(new Integer(1));

        try {
            Response<Organization> response=organizationApi.createOrganization(organizationRequest).execute();
            Assert.assertEquals("Orga Prueba",response.body().getName());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void inviteUser(){
        Integer organizationId=1;
        UserInvitationRequest userInvitationRequest=new UserInvitationRequest();
        userInvitationRequest.setUserEmail("otro@mail.com");
        try {
            Response<TokenResponse> response = organizationApi.inviteUser(organizationId, userInvitationRequest).execute();

            Assert.assertTrue(response.isSuccessful());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }
}