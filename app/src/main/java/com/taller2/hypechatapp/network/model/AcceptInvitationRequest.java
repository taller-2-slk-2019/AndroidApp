package com.taller2.hypechatapp.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AcceptInvitationRequest implements Serializable {

    @SerializedName("token")
    @Expose
    public String token;
    private final static long serialVersionUID = 2382299825198942922L;

}