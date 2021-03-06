
package com.taller2.hypechatapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.taller2.hypechatapp.model.roles.RoleFactory;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.NonNull;

public class Organization implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("welcome")
    @Expose
    private String welcome;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("users")
    @Expose
    private List<User> users = null;
    @SerializedName("channels")
    @Expose
    private List<Channel> channels = null;
    @SerializedName("userOrganizations")
    @Expose
    private UserOrganizations userOrganizations;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public UserOrganizations getUserOrganizations() {
        return userOrganizations;
    }

    public void setUserOrganizations(UserOrganizations userOrganizations) {
        this.userOrganizations = userOrganizations;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    public String getRole() {
        if (userOrganizations != null) {
            return userOrganizations.getRole();
        }
        return RoleFactory.ROLE_MEMBER;
    }
}
