package com.alr.tasks;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

public class Notifi extends Notification {

    private Context context;


    Notifi(Context context) {
        this.context = context;
    }

    @SuppressWarnings("unused")
    Notifi(Context context, String title, String text, boolean showIcon) {
        this.context = context;
    }

    public Notification get(String title, String text, boolean showIcon, boolean vibrate) {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.app_logo_small)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.app_logo))
                .setTicker(text)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(text);
        if (showIcon)
            builder.setPriority(Notification.PRIORITY_DEFAULT);
        else
            builder.setPriority(Notification.PRIORITY_MIN);   // no icon
        if (vibrate)
            builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

        return builder.build();
    }

    public void show(String title, String text, boolean showIcon, boolean vibrate) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification n = get(title, text, showIcon, vibrate);
        nm.notify(Consts.NOTIFY_ID, n);

    }

}
