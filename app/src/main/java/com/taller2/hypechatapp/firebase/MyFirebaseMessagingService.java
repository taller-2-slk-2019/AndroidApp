package com.taller2.hypechatapp.firebase;


import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.taller2.hypechatapp.model.Message;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FIREBASECM_SERVICE";

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
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    private void handleNewMessage(String data) {
        Message message = new Gson().fromJson(data, Message.class);
        EventBus.getDefault().post(message);
    }
}
