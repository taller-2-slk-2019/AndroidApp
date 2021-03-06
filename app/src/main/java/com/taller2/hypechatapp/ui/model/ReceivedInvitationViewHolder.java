package com.taller2.hypechatapp.ui.model;

import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.taller2.hypechatapp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReceivedInvitationViewHolder extends RecyclerView.ViewHolder {

    public TextView invitationTextView;
    public MaterialButton acceptInvitationButton;
    public MaterialButton rejectInvitationButton;

    public ReceivedInvitationViewHolder(@NonNull View itemView) {
        super(itemView);

        invitationTextView = itemView.findViewById(R.id.received_invitation_text_view);
        acceptInvitationButton = itemView.findViewById(R.id.accept_invitation_button);
        rejectInvitationButton = itemView.findViewById(R.id.reject_invitation_button);
    }
}
