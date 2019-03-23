package com.taller2.hypechatapp.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChannelRequest {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("visibility")
    @Expose
    private String visibility;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("welcome")
    @Expose
    private String welcome;
    @SerializedName("creatorId")
    @Expose
    private Integer creatorId;
    @SerializedName("organizationId")
    @Expose
    private Integer organizationId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWelcome() {
        return welcome;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

}
