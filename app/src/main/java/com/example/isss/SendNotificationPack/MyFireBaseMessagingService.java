package com.example.isss.SendNotificationPack;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.isss.R;
import com.example.isss.SI.Side_SI;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFireBaseMessagingService extends FirebaseMessagingService {
    String id ="chanel_1";
    String description ="123";
    int importance = NotificationManager.IMPORTANCE_HIGH;
    String title,message;
    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    final long[] VIBRATE_PATTERN  = {0, 5000};

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);
        title=remoteMessage.getData().get("Title");
        message=remoteMessage.getData().get("Message");
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, Side_SI.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(id, "123", importance);//Generating channel
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setVibrationPattern(new long[]{5000, 5000, 5000, 5000, 5000});
            channel.setLightColor(Color.RED);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), id)
                .setSmallIcon(R.drawable.status_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true);


//        Intent notificationIntent = new Intent(this.getApplicationContext(), MapsActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, notificationIntent, 0);
//        notification.contentIntent = contentIntent;

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);
        manager.notify(1,mBuilder.build());
    }

}
