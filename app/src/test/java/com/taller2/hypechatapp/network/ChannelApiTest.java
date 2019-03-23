package com.taller2.hypechatapp.network;

import com.taller2.hypechatapp.model.Channel;
import com.taller2.hypechatapp.network.model.ChannelRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import retrofit2.Response;

import static org.junit.Assert.*;

public class ChannelApiTest {

    ChannelApi channelApi;

    @Before
    public void setUp() throws Exception {
        channelApi=ApiClient.getInstance().getChannelClient();
    }

    @Test
    public void createChannel() {
        ChannelRequest channelRequest=new ChannelRequest();
        channelRequest.setName("Nuevo Canal");
        channelRequest.setCreatorId(1);
        channelRequest.setDescription("Descripcion Canal");
        channelRequest.setOrganizationId(1);
        channelRequest.setVisibility("public");
        channelRequest.setWelcome("Mensaje Bienvenida");
        try {
            Response<Channel> response=channelApi.createChannel(channelRequest).execute();
            Assert.assertEquals("Nuevo Canal",response.body().getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}