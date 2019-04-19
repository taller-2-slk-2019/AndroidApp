package com.taller2.hypechatapp.network;

import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.network.model.UserLocationRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import retrofit2.Response;

import static org.junit.Assert.*;

public class UserApiTest {

    UserApi userApi;

    @Before
    public void setUp() throws Exception {
        this.userApi=ApiClient.getInstance().getUserClient();
    }

    @Test
    public void getUser() {
        Integer userId=1;
        try {
            Response<User> response=userApi.getUser(userId).execute();
            Assert.assertEquals(userId,response.body().getId());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void updateUser() {
        Integer userId=1;
        User user=new User();
        user.setName("Usuario");
        user.setSurname("Prueba");
        user.setEmail("lalala@mail.com");
        user.setPicture("foto.jpg");
        try {
            Response<ConfirmationResponse> response=userApi.updateUser(userId,user).execute();
            Assert.assertTrue(response.body().getSuccess());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void registerUser() {
        User user=new User();
        user.setName("Usuario");
        user.setSurname("Prueba");
        user.setEmail("otromas@mail.com");
        user.setPicture("foto.jpg");
        try {
            Response<User> response=userApi.registerUser(user).execute();
            Assert.assertEquals("Usuario",response.body().getName());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void updateUserLocation() {
        Integer userId=1;
        UserLocationRequest userLocationRequest=new UserLocationRequest();
        userLocationRequest.setLatitude(new Double(40));
        userLocationRequest.setLongitude(new Double(10));
        try {
            Response<ConfirmationResponse> response=userApi.updateUserLocation(userId,userLocationRequest).execute();
            Assert.assertTrue(response.body().getSuccess());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    /*@Test
    public void acceptInvitation() {
        try {
            Response<ConfirmationResponse> response=userApi.acceptInvitation("131728915272fa1aa6367175b5b60303f7d9fec0").execute();
            Assert.assertFalse(response.body().getSuccess());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }*/
}