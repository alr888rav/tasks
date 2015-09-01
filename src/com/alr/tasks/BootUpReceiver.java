package com.alr.tasks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

// autorun (install only internal!)
public class BootUpReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        new Logger(context);
        Logger.d(Consts.LOG, "boot up");
        // load alarms for scheduled tasks
        AlarmReceiver alarm = new AlarmReceiver();
        alarm.setAlarm(context);
        // show icon
        context.startService(new Intent(context, IconService.class));
    }

}
