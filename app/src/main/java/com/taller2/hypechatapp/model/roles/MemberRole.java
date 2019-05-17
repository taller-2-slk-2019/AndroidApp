package com.taller2.hypechatapp.model.roles;

public class MemberRole implements Role {

    @Override
    public boolean hasOrganizationPermissions() {
        return false;
    }

    @Override
    public boolean hasUsersPermissions() {
        return false;
    }

    @Override
    public boolean hasChannelsPermissions() {
        return false;
    }
}
