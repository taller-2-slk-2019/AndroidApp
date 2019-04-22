package com.taller2.hypechatapp.notifications;

import android.content.Context;
import android.content.Intent;

import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.ui.activities.ReceivedInvitationsActivity;

public class NewOrganizationInvitationNotification extends HypechatNotification {

    private Organization organization;

    public NewOrganizationInvitationNotification(Context context, Organization organization) {
        super(context);
        this.organization = organization;
    }

    @Override
    protected String getTitle() {
        return "Te han invitado a una organización";
    }

    @Override
    protected String getContent() {
        return "Fuiste invitado a la organización " + organization.getName();
    }

    @Override
    protected Intent getIntent(){
        return new Intent(context, ReceivedInvitationsActivity.class);
    }
}
