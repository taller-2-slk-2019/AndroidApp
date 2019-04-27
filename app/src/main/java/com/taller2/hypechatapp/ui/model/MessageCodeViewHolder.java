package com.taller2.hypechatapp.ui.model;

import android.view.View;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.model.Message;

import androidx.annotation.NonNull;
import io.github.kbiakov.codeview.CodeView;

public class MessageCodeViewHolder extends MessageViewHolder {

    private CodeView messageText;

    public MessageCodeViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    protected void initialize(View view){
        messageText = view.findViewById(R.id.message_code);
    }

    @Override
    public void setMessage(Message message) {
        super.setMessage(message);
        messageText.setCode(message.data);
    }

    @Override
    protected int getViewId() {
        return R.layout.message_code;
    }
}
