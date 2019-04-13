package com.taller2.hypechatapp.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuccessResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
}
