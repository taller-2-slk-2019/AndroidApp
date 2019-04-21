package com.taller2.hypechatapp.adapters;

import android.content.Context;
import android.content.res.Resources;
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

public class ReceivedInvitationsAdapter extends RecyclerView.Adapter<ReceivedInvitationViewHolder> implements InvitationResponseListener {

    private List<ReceivedInvitation> invitations;
    private InvitationClickListener invitationClickListener;
    private Context context;

    public ReceivedInvitationsAdapter(List<ReceivedInvitation> receivedInvitations, InvitationClickListener invitationClickListener, Context context) {
        this.invitations=receivedInvitations;
        this.invitationClickListener = invitationClickListener;
        this.context=context;
    }

    @NonNull
    @Override
    public ReceivedInvitationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.received_invitation, parent, false);

        ReceivedInvitationViewHolder vh=new ReceivedInvitationViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ReceivedInvitationViewHolder holder, int position) {
        Resources res = context.getResources();
        String text = String.format(res.getString(R.string.received_invitation_text), invitations.get(holder.getAdapterPosition()).organization);
        holder.invitationTextView.setText(Html.fromHtml(text));
        holder.acceptInvitationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invitationClickListener.onAcceptClick(invitations.get(holder.getAdapterPosition()).token, holder.getAdapterPosition(),ReceivedInvitationsAdapter.this);
            }
        });
        holder.rejectInvitationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invitationClickListener.onRejectClick(invitations.get(holder.getAdapterPosition()).token, holder.getAdapterPosition(),ReceivedInvitationsAdapter.this);

            }
        });
    }

    @Override
    public int getItemCount() {
        return invitations.size();
    }


    @Override
    public void onInvitationResponse(int adapterPosition) {
        invitations.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }


}
