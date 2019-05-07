package com.taller2.hypechatapp.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChannelInvitationRequest implements Serializable {


    private static final long serialVersionUID = 367325426185557490L;

    @SerializedName("userId")
    @Expose
    public String userId;

}
