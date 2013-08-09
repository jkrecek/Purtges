package com.frca.purtges.gcm;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmBroadcastReceiver extends BroadcastReceiver {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;

    Context ctx;

    @Override
    public void onReceive(Context context, Intent intent) {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        ctx = context;
        String messageType = gcm.getMessageType(intent);
        if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
            sendNotification("Send error: " + intent.getExtras().toString());
        } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
            sendNotification("Deleted messages on server: " +
                intent.getExtras().toString());
        } else
            sendNotification("Received: " + intent.getExtras().toString());

        setResultCode(Activity.RESULT_OK);
    }

    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(ctx)
                .setContentTitle("GCM Notification")
                .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(msg))
                .setContentText(msg);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
