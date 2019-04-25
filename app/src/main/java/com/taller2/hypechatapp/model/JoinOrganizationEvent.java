package com.taller2.hypechatapp.model;

public class JoinOrganizationEvent {
    public Organization organization;

    public JoinOrganizationEvent(Organization organization) {
        this.organization = organization;
    }
}
