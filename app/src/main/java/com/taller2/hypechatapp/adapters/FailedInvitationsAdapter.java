package com.taller2.hypechatapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.ui.model.FailedInvitationViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FailedInvitationsAdapter extends RecyclerView.Adapter<FailedInvitationViewHolder> {

    private List<String> failedInvitations;

    public FailedInvitationsAdapter(List<String> failedInvitations) {
        this.failedInvitations = failedInvitations;
    }

    @NonNull
    @Override
    public FailedInvitationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.failed_invitation, parent, false);
        FailedInvitationViewHolder vh = new FailedInvitationViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull FailedInvitationViewHolder holder, int position) {
        holder.emailTextView.setText(failedInvitations.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return failedInvitations.size();
    }
}
