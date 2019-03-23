package com.taller2.hypechatapp.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiError {

    @SerializedName("entityName")
    @Expose
    public String entityName;
    @SerializedName("errorKey")
    @Expose
    public String errorKey;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("params")
    @Expose
    public String params;

}