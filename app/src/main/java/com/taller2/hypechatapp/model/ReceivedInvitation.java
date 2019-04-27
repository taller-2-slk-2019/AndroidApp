package com.taller2.hypechatapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReceivedInvitation implements Serializable
{

    @SerializedName("token")
    @Expose
    public String token;
    @SerializedName("organization")
    @Expose
    public Organization organization;
    @SerializedName("invitedAt")
    @Expose
    public String invitedAt;
    private final static long serialVersionUID = 2759717683566975654L;

}