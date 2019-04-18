package com.taller2.hypechatapp.ui.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.adapters.IMenuItemsClick;
import com.taller2.hypechatapp.model.Conversation;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MenuConversationItemViewHolder extends RecyclerView.ViewHolder {

    private final IMenuItemsClick listener;
    private TextView displayName;
    private Conversation conversation;

    public MenuConversationItemViewHolder(@NonNull View itemView, IMenuItemsClick listener) {
        super(itemView);
        displayName = itemView.findViewById(R.id.channel_name);
        ImageView icon = itemView.findViewById(R.id.img_channel_privacy);
        icon.setImageResource(R.drawable.ic_chat_menu);
        this.listener = listener;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onHolderClick();
            }
        });
    }

    public void setConversation(Conversation conversation) {
        displayName.setText(conversation.getName());
        this.conversation = conversation;
    }

    private void onHolderClick() {
    }
}
