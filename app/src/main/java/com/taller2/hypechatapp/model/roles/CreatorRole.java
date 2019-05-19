package com.taller2.hypechatapp.model.roles;

public class CreatorRole implements Role {

    @Override
    public boolean hasOrganizationPermissions() {
        return true;
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
