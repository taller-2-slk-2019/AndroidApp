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
    public Integer id;
    @SerializedName("channelId")
    @Expose
    public Integer channelId;
    @SerializedName("conversationId")
    @Expose
    public Integer conversationId;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("data")
    @Expose
    public String data;
    @SerializedName("createdAt")
    @Expose
    public String createdAt;
    @SerializedName("sender")
    @Expose
    public User sender;
    @SerializedName("bot")
    @Expose
    public String bot;

    public String getSenderName() {
        if (sender != null) {
            return sender.getName();
        }
        return "Bot " + bot;
    }
}
