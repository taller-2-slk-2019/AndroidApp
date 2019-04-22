package com.taller2.hypechatapp.adapters;

import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.taller2.hypechatapp.network.model.ReceivedInvitation;

public interface InvitationClickListener {
    void onAcceptClick(String token, int adapterPosition, InvitationResponseListener listener);

    void onRejectClick(String token, int adapterPosition, InvitationResponseListener listener);
}
