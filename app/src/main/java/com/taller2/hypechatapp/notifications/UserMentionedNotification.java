package com.taller2.hypechatapp.notifications;

import android.content.Context;
import android.content.Intent;

import com.taller2.hypechatapp.model.Channel;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.ui.activities.ChatActivity;

public class UserMentionedNotification extends HypechatNotification {

    private Channel channel;
    private User sender;

    public UserMentionedNotification(Context context, Channel channel, User sender) {
        super(context);
        this.channel = channel;
        this.sender = sender;
    }

    @Override
    protected String getTitle() {
        return "Te han mencionado en un mensaje";
    }

    @Override
    protected String getContent() {
        return sender.getName() + " te mencionó en el canal " + channel.getName();
    }

    @Override
    protected Intent getIntent() {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra("organizationId", channel.organizationId);
        intent.putExtra("channelId", channel.getId());
        return intent;
    }
}
