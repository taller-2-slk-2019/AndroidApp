package com.taller2.hypechatapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Message implements Serializable {

    public static final String TYPE_TEXT = "text";
    public static final String TYPE_CODE = "code";
    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_FILE = "file";

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("channelId")
    @Expose
    private Integer channelId;
    @SerializedName("conversationId")
    @Expose
    private Integer conversationId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("data")
    @Expose
    private String data;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("sender")
    @Expose
    private User sender;

    public Integer getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getSender() {
        return sender;
    }

    public void setChannel(int channelId) {
        this.channelId = channelId;
    }
}
