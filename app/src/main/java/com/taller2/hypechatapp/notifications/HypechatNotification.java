package com.taller2.hypechatapp.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.ui.activities.LoginActivity;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.NotificationCompat;

public abstract class HypechatNotification {

    private NotificationCompat.Builder notif;
    protected Context context;
    private String channelName;

    public HypechatNotification(Context context){
        channelName = context.getString(R.string.app_name);
        initChannels(context);
        this.context = context;
    }

    private Bitmap getLargeIcon() {
        Drawable icon = AppCompatResources.getDrawable(context, R.mipmap.ic_launcher_round);
        return ((BitmapDrawable)icon).getBitmap();
    }

    private void setOpenActivity() {
        Intent intent = getIntent();
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        notif.setContentIntent(pendingIntent);
    }

    private void createNotification(){
        notif = new NotificationCompat.Builder(context, channelName);

        notif.setSmallIcon(R.drawable.ic_chat_white);
        notif.setLargeIcon(this.getLargeIcon());

        notif.setContentTitle(getTitle());
        String content = getContent();
        notif.setContentText(content);
        notif.setAutoCancel(true);
        notif.setStyle(new NotificationCompat.BigTextStyle().bigText(content));
        notif.setDefaults(Notification.DEFAULT_ALL);
        setOpenActivity();
    }

    public void send(){
        createNotification();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int)System.currentTimeMillis(), notif.build());
    }


    private void initChannels(Context context) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
            return;
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel(channelName, channelName, NotificationManager.IMPORTANCE_HIGH);

        channel.setDescription(channelName);
        channel.enableVibration(true);
        channel.enableLights(true);

        notificationManager.createNotificationChannel(channel);
    }

    protected abstract String getTitle();

    protected abstract String getContent();

    protected Intent getIntent(){
        return new Intent(context, LoginActivity.class);
    }

}
