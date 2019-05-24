package com.taller2.hypechatapp.adapters;

import com.taller2.hypechatapp.model.User;

public interface UserListActionListener {

    void onUserAction(User user);

    void onUserRoleChanged(User user, String selectedRole);

    void onUserSelected(User user);
}
