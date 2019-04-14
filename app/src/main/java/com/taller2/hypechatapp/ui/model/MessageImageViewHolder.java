package com.taller2.hypechatapp.ui.model;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.components.PicassoLoader;
import com.taller2.hypechatapp.model.Message;

import androidx.annotation.NonNull;

public class MessageImageViewHolder extends MessageViewHolder {

    private ImageView messageImage;
    private Context context;

    public MessageImageViewHolder(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
    }

    @Override
    protected void initialize(View view){
        messageImage = view.findViewById(R.id.message_image);
    }

    @Override
    public void setMessage(Message message) {
        super.setMessage(message);
        PicassoLoader.load(context, message.data, messageImage);
    }

    @Override
    protected int getViewId() {
        return R.layout.message_image;
    }
}
