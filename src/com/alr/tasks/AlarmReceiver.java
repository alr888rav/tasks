package com.alr.tasks;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmReceiver extends WakefulBroadcastReceiver {
    private AlarmManager alarmMgr;
    private static ArrayList<PendingIntent> alarmIntent = new ArrayList<>();
  
    @Override
    public void onReceive(Context context, Intent intent) {   
        try {
            new Logger(context);
            Logger.d(Consts.LOG, "alarm onReceive");
            int taskId = intent.getIntExtra("id", -1);
            Tasks tasks = new Tasks(context);
            tasks.someTask(new Time(), taskId);
            completeWakefulIntent(intent);
        } catch (Exception e) {
            Logger.e(Consts.LOG, "onreceive "+e.getMessage());
        }
    }

    // Sets a repeating alarm
    public void setAlarm(Context context) {
        Logger.d(Consts.LOG, "set alarm");
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        MyDb myDb = MyDb.getInstance(context);
        ArrayList<MyTask> data = myDb.getAllTasksData();

        // cancel old
        cancelAlarm(context);
        // one task - one alarm
        alarmIntent.clear();
        for (MyTask ts : data) {

            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("id", ts.id);
            PendingIntent pi = PendingIntent.getBroadcast(context, ts.id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmIntent.add(pi);
            // Set the alarm trigger time to current time
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, ts.hour);
            calendar.set(Calendar.MINUTE, ts.minute);
            // 1 days
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
            Logger.d(Consts.LOG, "set " + ts.id + " " + ts.hour + ":" + ts.minute);
        }
        // Enable {@code SampleBootReceiver} to automatically restart the alarm when the
        // device is rebooted.
        ComponentName receiver = new ComponentName(context, BootUpReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void cancelAlarm(Context context) {
        // If the alarm has been set, cancel it.
        Logger.d(Consts.LOG, "cancel ts");
        if (alarmMgr!= null) {
            for (PendingIntent intent : alarmIntent) {
                alarmMgr.cancel(intent);
            }
        }
        // Disable {@code SampleBootReceiver} so that it doesn't automatically restart the 
        // alarm when the device is rebooted.
        ComponentName receiver = new ComponentName(context, BootUpReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
