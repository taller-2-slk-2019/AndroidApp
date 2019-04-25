package com.taller2.hypechatapp.model;

import java.util.List;

public class JoinOrganizationEvent {
    public List<Organization> acceptedOrganizations;

    public JoinOrganizationEvent(List<Organization> acceptedOrganizations) {
        this.acceptedOrganizations = acceptedOrganizations;
    }
}
