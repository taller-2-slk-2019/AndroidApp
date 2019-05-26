package com.taller2.hypechatapp.ui.model;

import android.view.View;

import com.taller2.hypechatapp.adapters.UserListActionListener;
import com.taller2.hypechatapp.firebase.FirebaseAuthService;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.model.roles.Role;
import com.taller2.hypechatapp.model.roles.RoleFactory;

import androidx.annotation.NonNull;

public class ChannelListUserViewHolder extends ListUserViewHolder {

    public ChannelListUserViewHolder(@NonNull View itemView, UserListActionListener listener) {
        super(itemView, listener, Action.DELETE);
    }

    @Override
    protected void bindUser(User user) {
        checkUserRole();
    }

    private void checkUserRole() {
        Role role = RoleFactory.getRole(prefs.getOrganizationRole());
        boolean isCurrentUser = FirebaseAuthService.isCurrentUser(user);

        actionButton.setVisibility(role.hasChannelsPermissions() && !isCurrentUser ? View.VISIBLE : View.GONE);
    }
}
