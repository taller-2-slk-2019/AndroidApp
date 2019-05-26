package com.taller2.hypechatapp.ui.model;

import android.view.View;

import com.taller2.hypechatapp.adapters.UserListActionListener;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.model.roles.Role;
import com.taller2.hypechatapp.model.roles.RoleFactory;

import androidx.annotation.NonNull;

public class ChannelListNewUserViewHolder extends ListUserViewHolder {

    public ChannelListNewUserViewHolder(@NonNull View itemView, UserListActionListener listener) {
        super(itemView, listener, Action.ADD);
    }

    @Override
    protected void bindUser(User user) {
        checkUserRole();
    }

    private void checkUserRole() {
        Role role = RoleFactory.getRole(prefs.getOrganizationRole());
        actionButton.setVisibility(role.hasChannelsPermissions() ? View.VISIBLE : View.GONE);
    }
}
