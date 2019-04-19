package com.taller2.hypechatapp.adapters;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.network.model.ReceivedInvitation;
import com.taller2.hypechatapp.ui.model.ReceivedInvitationViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReceivedInvitationsAdapter extends RecyclerView.Adapter<ReceivedInvitationViewHolder> {

    private List<ReceivedInvitation> invitations;

    public ReceivedInvitationsAdapter(List<ReceivedInvitation> receivedInvitations) {
        invitations=receivedInvitations;
    }

    @NonNull
    @Override
    public ReceivedInvitationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.received_invitation, parent, false);

        ReceivedInvitationViewHolder vh=new ReceivedInvitationViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ReceivedInvitationViewHolder holder, int position) {
        StringBuilder sb=new StringBuilder();
        sb.append("La organización <b>");
        sb.append(invitations.get(holder.getAdapterPosition()).organization);
        sb.append("</b> desea que te unas a su equipo. ¿Aceptas?");

        holder.invitationTextView.setText(Html.fromHtml(sb.toString()));
        holder.acceptInvitationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Accept Invitation, delete entry from list and notify data change
            }
        });
        holder.rejectInvitationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Reject Invitation, delete entry from list and notify data change

            }
        });
    }

    @Override
    public int getItemCount() {
        return invitations.size();
    }
}
