package com.taller2.hypechatapp.model.roles;

public interface Role {

    boolean hasOrganizationPermissions();

    boolean hasUsersPermissions();

    boolean hasChannelsPermissions();
}
