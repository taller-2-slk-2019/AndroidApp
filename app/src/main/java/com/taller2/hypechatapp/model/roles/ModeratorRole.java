package com.taller2.hypechatapp.model.roles;

public class ModeratorRole implements Role {

    @Override
    public boolean hasOrganizationPermissions() {
        return false;
    }

    @Override
    public boolean hasUsersPermissions() {
        return true;
    }

    @Override
    public boolean hasChannelsPermissions() {
        return true;
    }
}
