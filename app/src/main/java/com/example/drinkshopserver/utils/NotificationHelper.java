package com.example.drinkshopserver.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.example.drinkshopserver.Activities.OrderActivity;
import com.example.drinkshopserver.R;

public class NotificationHelper extends ContextWrapper {

    private static final String BLUETREE_CHANNEL_ID = "com.example.drinkshopserver";
    private static final String BLUETREE_CHANNEL_NAME = "DRINK SHOP";

    private NotificationManager notificationManager;


    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            creatChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void creatChannel() {
        NotificationChannel blueTreeChannel = new NotificationChannel(BLUETREE_CHANNEL_ID,BLUETREE_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT);

        blueTreeChannel.enableLights(false);
        blueTreeChannel.enableVibration(true);
        blueTreeChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(blueTreeChannel);
    }

    public NotificationManager getManager() {
        if(notificationManager == null)
            notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        return  notificationManager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getDrinkShopNotification(String title, String message, Uri soundUri){
        Intent resultIntent = new Intent(this, OrderActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,1,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        return  new Notification.Builder(getApplicationContext(),BLUETREE_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setSound(soundUri)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent);
    }

}
