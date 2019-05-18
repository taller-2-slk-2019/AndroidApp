package com.taller2.hypechatapp.firebase;


import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.taller2.hypechatapp.model.Channel;
import com.taller2.hypechatapp.model.Conversation;
import com.taller2.hypechatapp.model.Message;
import com.taller2.hypechatapp.model.Organization;
import com.taller2.hypechatapp.model.User;
import com.taller2.hypechatapp.notifications.NewChannelInvitationNotification;
import com.taller2.hypechatapp.notifications.NewConversationInvitationNotification;
import com.taller2.hypechatapp.notifications.NewOrganizationInvitationNotification;
import com.taller2.hypechatapp.notifications.UserMentionedNotification;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FIREBASECM_SERVICE";

    private Gson gson = new Gson();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            Map<String, String> data = remoteMessage.getData();
            String type = data.get("type");

            switch(type){
                case "new_message":
                    Log.d(TAG, "handling new message");
                    handleNewMessage(data.get("message"));
                    break;
                case "mention":
                    Log.d(TAG, "handling mentioned user");
                    handleMentionedUser(data.get("message"), data.get("channel"), data.get("sender"));
                    break;
                case "invitation":
                    Log.d(TAG, "handling new invitation");
                    handleInvitation(data.get("organization"));
                    break;
                case "channel_invitation":
                    Log.d(TAG, "handling new channel invitation");
                    handleChannelInvitation(data.get("channel"));
                    break;
                case "conversation_invitation":
                    Log.d(TAG, "handling new conversation invitation");
                    handleConversationInvitation(data.get("conversation"), data.get("user"));
                    break;
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    private void handleNewMessage(String data) {
        Message message = gson.fromJson(data, Message.class);
        EventBus.getDefault().post(message);
    }

    private void handleMentionedUser(String messageData, String channelData, String senderData) {
        Message message = gson.fromJson(messageData, Message.class);
        Channel channel = gson.fromJson(channelData, Channel.class);
        User sender = gson.fromJson(senderData, User.class);
        new UserMentionedNotification(this, channel, sender).send();
    }

    private void handleInvitation(String data) {
        Organization organization = gson.fromJson(data, Organization.class);
        new NewOrganizationInvitationNotification(this, organization).send();
    }

    private void handleChannelInvitation(String data) {
        Channel channel = gson.fromJson(data, Channel.class);
        new NewChannelInvitationNotification(this, channel).send();
        EventBus.getDefault().post(channel);
    }

    private void handleConversationInvitation(String conversationData, String userData) {
        Conversation conversation = gson.fromJson(conversationData, Conversation.class);
        User user = gson.fromJson(userData, User.class);
        new NewConversationInvitationNotification(this, conversation, user).send();
        EventBus.getDefault().post(conversation);
    }
}
