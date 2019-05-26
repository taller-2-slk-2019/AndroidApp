package com.taller2.hypechatapp.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UserInvitationRequest implements Serializable {

    @SerializedName("userEmails")
    @Expose
    public List<String> userEmails = null;
    private final static long serialVersionUID = 1974860247318945030L;

}
