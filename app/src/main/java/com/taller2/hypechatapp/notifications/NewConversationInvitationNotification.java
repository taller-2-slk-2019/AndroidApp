package com.taller2.hypechatapp.notifications;

import android.content.Context;
import android.content.Intent;

import com.taller2.hypechatapp.model.Conversation;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.ui.activities.ChatActivity;

public class NewConversationInvitationNotification extends HypechatNotification {
    private Conversation conversation;
    private User user;

    public NewConversationInvitationNotification(Context context, Conversation conversation, User user) {
        super(context);
        this.conversation = conversation;
        this.user = user;
    }

    @Override
    protected String getTitle() {
        return "Nueva conversación";
    }

    @Override
    protected String getContent() {
        return user.getName() + " ha iniciado una conversación con vos";
    }

    @Override
    protected Intent getIntent() {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra("organizationId", conversation.organizationId);
        intent.putExtra("conversationId", conversation.id);
        return intent;
    }
}
