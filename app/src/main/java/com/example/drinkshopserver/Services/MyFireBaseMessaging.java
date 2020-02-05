package com.example.drinkshopserver.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.drinkshopserver.Activities.OrderActivity;
import com.example.drinkshopserver.R;
import com.example.drinkshopserver.Retrofit.RetrofitClient;
import com.example.drinkshopserver.utils.NotificationHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFireBaseMessaging extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        newToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("messageid", "onMessageReceived: "+remoteMessage.getMessageId());

        if(remoteMessage.getData() != null){
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                sendNotification_API_26(remoteMessage);
            }else{
                sendNotification_API_less_26(remoteMessage);
            }
        }
    }

    private void sendNotification_API_less_26(RemoteMessage remoteMessage) {
        Map<String,String> data = remoteMessage.getData();
        String title = data.get("title");
        String message = data.get("message");

        Intent resultIntent = new Intent(this, OrderActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,1,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(resultPendingIntent);

        NotificationManager notify = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notify.notify(new Random().nextInt(), builder.build());

    }

    private void sendNotification_API_26(RemoteMessage remoteMessage) {
        Map<String,String> data = remoteMessage.getData();
        String title = data.get("title");
        String message = data.get("message");

        NotificationHelper helper;
        Notification.Builder builder;

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        helper = new NotificationHelper(this);
        builder = helper.getDrinkShopNotification(title,message,defaultSoundUri);

        helper.getManager().notify(new Random().nextInt(),builder.build());
    }

    private void newToken(String token) {
        RetrofitClient.getInstance().getApi().isertToken("server_app_01", token, "1"
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("onResponse", "onResponse: " + response.toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("onResponse", "onResponse: " + t.getMessage());
            }
        });
    }

}
