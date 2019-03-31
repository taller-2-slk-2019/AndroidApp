package com.taller2.hypechatapp.network.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrganizationRequest implements Serializable
{

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("picture")
    @Expose
    public String picture;
    @SerializedName("latitude")
    @Expose
    public Double latitude;
    @SerializedName("longitude")
    @Expose
    public Double longitude;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("welcome")
    @Expose
    public String welcome;
    @SerializedName("creatorId")
    @Expose
    public Integer creatorId;
    private final static long serialVersionUID = -581573439418199621L;

}
