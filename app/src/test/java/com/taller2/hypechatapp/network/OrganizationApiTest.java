package com.taller2.hypechatapp.network;

import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.network.model.OrganizationRequest;
import com.taller2.hypechatapp.network.model.SuccessResponse;
import com.taller2.hypechatapp.network.model.TokenResponse;
import com.taller2.hypechatapp.network.model.UserInvitationRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
            Response<Organization> response=organizationApi.getOrganizationProfile(idToTest).execute();
            Assert.assertEquals(idToTest,response.body().getId());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getOrganizationsByUser() {
        Integer userIdToTest=1;
        /*try {
            Response<List<Organization>> response=organizationApi.getOrganizationsByUser(userIdToTest).execute();
            Assert.assertTrue(response.body().size()>0);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }*/
    }

    @Test
    public void createOrganization(){

        OrganizationRequest organizationRequest=new OrganizationRequest();
        organizationRequest.name="Orga Prueba";
        organizationRequest.latitude=new Double(10);
        organizationRequest.longitude=new Double(20);
        organizationRequest.picture="foto.jpg";
        organizationRequest.description="Esta es la descripcion";
        organizationRequest.welcome="Mensaje de bienvenida";

        /*try {
            Response<Organization> response=organizationApi.createOrganization(organizationRequest).execute();
            Assert.assertEquals("Orga Prueba",response.body().getName());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }*/
    }

    @Test
    public void inviteUser(){
        Integer organizationId=3;
        UserInvitationRequest userInvitationRequest=new UserInvitationRequest();
        List<String> userList=new ArrayList<>();
        userList.add("test@test.com");
        userInvitationRequest.userEmails=userList;
        try {
            Response<SuccessResponse> response = organizationApi.inviteUsers(organizationId, userInvitationRequest).execute();

            Assert.assertTrue(response.body().success);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }
}