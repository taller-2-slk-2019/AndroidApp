package com.taller2.hypechatapp.adapters;

import com.taller2.hypechatapp.model.ReceivedInvitation;

public interface InvitationClickListener {
    void onAcceptClick(ReceivedInvitation receivedInvitation, int adapterPosition, InvitationResponseListener listener);

    void onRejectClick(String token, int adapterPosition, InvitationResponseListener listener);
}
