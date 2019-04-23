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

public class ReceivedInvitationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements InvitationResponseListener {

    private static final int VIEW_TYPE_EMPTY=0;
    private static final int VIEW_TYPE_NOT_EMPTY=1;
    private List<ReceivedInvitation> invitations;
    private InvitationClickListener invitationClickListener;
    private Context context;

    public ReceivedInvitationsAdapter(List<ReceivedInvitation> receivedInvitations, InvitationClickListener invitationClickListener, Context context) {
        this.invitations=receivedInvitations;
        this.invitationClickListener = invitationClickListener;
        this.context=context;
    }

    @Override
    public int getItemViewType(int position) {
        if(invitations.size()==0)
            return VIEW_TYPE_EMPTY;
        else
            return VIEW_TYPE_NOT_EMPTY;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==VIEW_TYPE_EMPTY){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.received_invitation_empty, parent, false);

            return new EmptyViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.received_invitation, parent, false);

            ReceivedInvitationViewHolder vh=new ReceivedInvitationViewHolder(v);
            return vh;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);

        if (viewType==VIEW_TYPE_NOT_EMPTY){
            final ReceivedInvitationViewHolder vh = (ReceivedInvitationViewHolder) holder;
            Resources res = context.getResources();
            String text = String.format(res.getString(R.string.received_invitation_text), invitations.get(vh.getAdapterPosition()).organization);
            vh.invitationTextView.setText(Html.fromHtml(text));
            vh.acceptInvitationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    invitationClickListener.onAcceptClick(invitations.get(vh.getAdapterPosition()).token, vh.getAdapterPosition(),ReceivedInvitationsAdapter.this);
                }
            });
            vh.rejectInvitationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    invitationClickListener.onRejectClick(invitations.get(vh.getAdapterPosition()).token, vh.getAdapterPosition(),ReceivedInvitationsAdapter.this);

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        //Hack to show the empty view
        if(invitations.size() == 0){
            return 1;
        }else {
            return invitations.size();
        }
    }


    @Override
    public void onInvitationResponse(int adapterPosition) {
        invitations.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder{

        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
