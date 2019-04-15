package com.taller2.hypechatapp.ui.model;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.components.DateHelper;
import com.taller2.hypechatapp.model.Message;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class MessageViewHolder extends RecyclerView.ViewHolder {

    private TextView username;
    private TextView date;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);

        username = itemView.findViewById(R.id.message_username);
        date = itemView.findViewById(R.id.message_date);

        LinearLayout container = itemView.findViewById(R.id.message_container);
        View child = LayoutInflater.from(itemView.getContext())
                .inflate(getViewId(), container, true);
        initialize(child);

    }

    public void setMessage(Message message) {
        username.setText(message.sender.getName());
        date.setText(DateHelper.serverToLocalString(message.createdAt));
    }

    protected abstract int getViewId();
    protected abstract void initialize(View view);

}
