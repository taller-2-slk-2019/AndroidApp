package com.taller2.hypechatapp.notifications;

import android.content.Context;
import android.content.Intent;

import com.taller2.hypechatapp.model.Channel;
import com.taller2.hypechatapp.ui.activities.ChatActivity;

public class NewChannelInvitationNotification extends HypechatNotification {
    private Channel channel;

    public NewChannelInvitationNotification(Context context, Channel channel) {
        super(context);
        this.channel = channel;
    }

    @Override
    protected String getTitle() {
        return "Nuevo canal";
    }

    @Override
    protected String getContent() {
        return "Ahora eres miembro del canal " + channel.getName();
    }

    @Override
    protected Intent getIntent(){
        Intent intent = new Intent(context, ChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra("organizationId", channel.organizationId);
        intent.putExtra("channelId", channel.getId());
        return intent;
    }
}
