package com.jackmiddlebrook.dailyselfie;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.text.DateFormat;
import java.util.Date;

/**
 * Alarm Receiver
 *
 */
public class AlarmNotificationReceiver extends BroadcastReceiver {

    // Notification ID to allow for future updates
    private static final int MY_NOTIFICATION_ID = 1;
    private static final String TAG = "AlarmNotificationReceiver";

    // Notification Text Elements
    private final CharSequence tickerText = "Time to take another selfie!";
    private final CharSequence contentTitle = "Selfie Reminder";
    private final CharSequence contentText = "Get that selfie taken!!";

    // Notification Action Elements
    private Intent mNotificationIntent;
    private PendingIntent mContentIntent;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {

        // The Intent to be used when the user clicks on the Notification View
        mNotificationIntent = new Intent(context, SelfieViewActivity.class);

        // The PendingIntent that wraps the underlying Intent
        mContentIntent = PendingIntent.getActivity(context, 0,
                mNotificationIntent, 0);

        // Build the Notification
        Notification.Builder notificationBuilder = new Notification.Builder(
                context).setTicker(tickerText)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setContentIntent(mContentIntent);

        // Get the NotificationManager
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        // Pass the Notification to the NotificationManager:
        mNotificationManager.notify(MY_NOTIFICATION_ID,
                notificationBuilder.build());

        // Log occurence of notify() call
        Log.i(TAG, "Sending notification at:"
                + DateFormat.getDateTimeInstance().format(new Date()));

    }
}
