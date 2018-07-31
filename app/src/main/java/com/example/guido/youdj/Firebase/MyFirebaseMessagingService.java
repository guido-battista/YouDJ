package com.example.guido.youdj.Firebase;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.guido.youdj.Vistas.ListaVotar.ListasCancionesActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.example.guido.youdj.R;
import com.example.guido.youdj.Vistas.ElegirEventoActivity;
import com.example.guido.youdj.Firebase.Constants;

/**
 * Created by Belal on 12/8/2017.
 */

//class extending FirebaseMessagingService
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        //Se limpian todas las notificaciones
        /*
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.
                        NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        */
        //------------------------------------

        String titulo;
        String descripcion;
        String topic;
        //if the message contains data payload
        //It is a map of custom keyvalues
        //we can read it easily
        if(remoteMessage.getData().size() > 0) {
            //handle the data message here
            /*
            titulo = remoteMessage.getData().get("titulo");
            descripcion = remoteMessage.getData().get("descripcion");
            */
            titulo = remoteMessage.getData().get("titulo");
            descripcion = remoteMessage.getData().get("descripcion");
            topic = remoteMessage.getData().get("topic");

            switch (topic)
            {
                case ("votar"):
                {
                    sendVotarNotification(titulo, descripcion);
                }
                break;

                case ("sonando"):
                {
                    sendSonandoNotification(titulo, descripcion);
                }
                break;
            }
        }
        else
        {
            titulo = remoteMessage.getNotification().getTitle();
            descripcion = remoteMessage.getNotification().getBody();
            topic = remoteMessage.getFrom();

            if (remoteMessage.getNotification() != null) {
                if (topic.contains("votar"))
                    sendVotarNotification(titulo, descripcion);
                if (topic.contains("sonando"))
                    sendSonandoNotification(titulo, descripcion);

                //sendVotarNotification(titulo, descripcion);
            }

        }
    }

    private void sendVotarNotification(String titulo, String descripcion) {
        Intent intent = new Intent(this, ListasCancionesActivity.class);

        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int requestCode = 0;

        //PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
        //        PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, Constants.CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(titulo)
                .setContentText(descripcion)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(resultPendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Constants.CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        int notificationId = 0;
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    //Este metodo es muy parecido al de arriba, pero por ahora lo duplico
    private void sendSonandoNotification(String titulo, String descripcion) {
        Intent intent = new Intent(this, ListasCancionesActivity.class);

        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int requestCode = 0;

        //PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
        //        PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, Constants.CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(titulo)
                .setContentText(descripcion)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_DEFAULT)
                //.setSound(defaultSoundUri)
                .setContentIntent(resultPendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Constants.CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        int notificationId = 1;
        notificationManager.notify(notificationId, notificationBuilder.build());
    }
}