package com.taller2.hypechatapp.ui.model;

import android.view.View;
import android.widget.TextView;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.model.Message;

import androidx.annotation.NonNull;

public class MessageTextViewHolder extends MessageViewHolder {

    private TextView messageText;

    public MessageTextViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    protected void initialize(View view){
        messageText = view.findViewById(R.id.message_text);
    }

    @Override
    public void setMessage(Message message) {
        super.setMessage(message);
        messageText.setText(message.data);
    }

    @Override
    protected int getViewId() {
        return R.layout.message_text;
    }
}
