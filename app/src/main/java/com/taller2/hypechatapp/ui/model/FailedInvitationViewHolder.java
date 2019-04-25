package com.taller2.hypechatapp.ui.model;

import android.view.View;
import android.widget.TextView;

import com.taller2.hypechatapp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FailedInvitationViewHolder extends RecyclerView.ViewHolder {

    public TextView emailTextView;

    public FailedInvitationViewHolder(@NonNull View itemView) {
        super(itemView);

        emailTextView=itemView.findViewById(R.id.failed_invitation_text_view);
    }
}
