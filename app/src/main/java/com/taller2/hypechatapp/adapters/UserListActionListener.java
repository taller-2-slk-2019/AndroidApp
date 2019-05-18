package com.taller2.hypechatapp.adapters;

import com.taller2.hypechatapp.model.User;

public interface UserListActionListener {

    void onUserDeleted(User user);

    void onUserRoleChanged(User user, String selectedRole);
}
