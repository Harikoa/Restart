package com.meyvn.restart_mobile.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.meyvn.restart_mobile.R;

import java.time.LocalDate;


public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        NotificationChannel nc = new NotificationChannel("MessageNotif","MessageNotif", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager nmc = getApplicationContext().getSystemService(NotificationManager.class);
        nmc.createNotificationChannel(nc);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"MessageNotif")
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("body"))
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                ;
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(1821,builder.build());
    }
}
