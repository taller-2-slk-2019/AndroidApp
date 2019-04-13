package com.taller2.hypechatapp.ui.model;

import android.view.View;
import android.widget.TextView;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.components.DateHelper;
import com.taller2.hypechatapp.model.Message;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    private TextView username;
    private TextView date;
    private TextView messageData;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);

        username = itemView.findViewById(R.id.message_username);
        date = itemView.findViewById(R.id.message_date);
        messageData = itemView.findViewById(R.id.message_text);
    }

    public void setMessage(Message message) {
        username.setText(message.getSender().getName());
        date.setText(DateHelper.serverToLocalString(message.getCreatedAt()));
        messageData.setText(message.getData());
    }
}
