package com.taller2.hypechatapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Conversation implements Serializable {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("users")
    @Expose
    public List<User> users;

    public String getName(){
        StringBuilder name = new StringBuilder();
        for (User user: users){
            if (name.length() > 0){
                name.append(", ");
            }
            name.append(user.getName());
        }
        return name.toString();
    }
}
