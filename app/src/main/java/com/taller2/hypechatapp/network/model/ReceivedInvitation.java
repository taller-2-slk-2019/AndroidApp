package com.taller2.hypechatapp.network.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.taller2.hypechatapp.model.Organization;

public class ReceivedInvitation implements Serializable
{

    @SerializedName("token")
    @Expose
    public String token;
    @SerializedName("organization")
    @Expose
    public Organization organization;
    @SerializedName("description")
    @Expose
    public String invitedAt;
    private final static long serialVersionUID = 2759717683566975654L;

}