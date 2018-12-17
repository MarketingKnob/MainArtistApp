package com.samyotech.fabartist;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.samyotech.fabartist.interfacess.Consts;
import com.samyotech.fabartist.preferences.SharedPrefrence;
import com.samyotech.fabartist.ui.activity.BaseActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    SharedPrefrence prefrence;
    int i = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        prefrence = SharedPrefrence.getInstance(this);
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());
        }


        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message data 1: " + remoteMessage.getNotification().getTitle());
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody());

            if (remoteMessage.getNotification().getTitle().equalsIgnoreCase("Chat")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(Consts.BROADCAST);
                LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
                i = prefrence.getIntValue("Value");
                i++;
                prefrence.setIntValue("Value", i);
            }
        }


    }


    private void sendNotification(String messageBody) {

        Intent intent = new Intent(this, BaseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";


        Uri defaultSoundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("FabArtist")
                .setSound(defaultSoundUri)
                /* .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))*/
                .setContentText(messageBody).setAutoCancel(true).setContentIntent(pendingIntent);
        ;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
    }


//e7q2YZRtQEA:APA91bGua6Fg6xP33iq-e0s7OEPDI_Rc19V7pYt_LR7u32OFwUuDlHCwP_sx1YVTUPywQGp4OlfPrD45QJThrH9Do2y1jgIq1yUputQLNATAah7IxHAIN9rMITHrMZI0zTi7yyyiXFWH

}


