package com.alr.tasks;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import java.util.Calendar;

public class IconService extends Service{
    private Context context;

    public void onCreate() {
        super.onCreate();
        context = getApplication();
        new Logger(context);
        Logger.d(Consts.LOG, "icon create");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            Logger.d(Consts.LOG, "onStartCommand iconService");
            Setup setup = new Setup(context);
            Notification nt = new Notifi(context).get(context.getString(R.string.app_name2), "", setup.showIcon, false);
            startForeground(Consts.NOTIFY_ID, nt);
        } catch (Exception e) {
            Logger.e(Consts.LOG, "iconservice: "+e.getMessage());
        }
        return START_STICKY;
    }


    public IBinder onBind(Intent intent) {
        return null;
    }

}
