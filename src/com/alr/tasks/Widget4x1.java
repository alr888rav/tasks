package com.alr.tasks;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

public class Widget4x1 extends AppWidgetProvider {

    private static int rqId = 1001;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int id : appWidgetIds) {
            updateWidget(context, appWidgetManager, id);
        }
        Logger.d(Consts.LOG, "widget onUpdate ");
    }

    static void updateWidget(Context context, AppWidgetManager appWidgetManager, int widgetID) {
        Phone phone = new Phone(context);
        try {
            RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget4x1);

            if (phone.isMobileDataEnabled())
                widgetView.setImageViewResource(R.id.mobileStatus, R.drawable.service_on);
            else
                widgetView.setImageViewResource(R.id.mobileStatus, R.drawable.service_off);

            if (phone.isBluetoothEnabled())
                widgetView.setImageViewResource(R.id.bluetoothStatus, R.drawable.service_on);
            else
                widgetView.setImageViewResource(R.id.bluetoothStatus, R.drawable.service_off);

            if (phone.isWifiEnabled())
                widgetView.setImageViewResource(R.id.wifiStatus, R.drawable.service_on);
            else
                widgetView.setImageViewResource(R.id.wifiStatus, R.drawable.service_off);

            int ring = (int) ((float) phone.getRingVolume() / phone.getMaxRingVolume() * 100);
            widgetView.setTextViewText(R.id.ringStatus, Integer.toString(ring) + "%");

            int notify = (int) ((float) phone.getNotifyVolume() / phone.getMaxNotifyVolume() * 100);
            widgetView.setTextViewText(R.id.notifyStatus, Integer.toString(notify) + "%");

            Setup setup = new Setup(context);
            if (setup.active)
                widgetView.setTextViewText(R.id.serviceStatus, context.getString(R.string.on_text_short));
            else
                widgetView.setTextViewText(R.id.serviceStatus, context.getString(R.string.off_text_short));

            Intent intent = new Intent(context, MainActivity.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
            PendingIntent pIntent = PendingIntent.getActivity(context, widgetID, intent, 0);
            widgetView.setOnClickPendingIntent(R.id.widgetLayout, pIntent);

            Intent configIntent = new Intent(context, SetupActivity.class);
            configIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
            PendingIntent pIntent2 = PendingIntent.getActivity(context, widgetID, configIntent, 0);
            widgetView.setOnClickPendingIntent(R.id.configImageView, pIntent2);

            appWidgetManager.updateAppWidget(widgetID, widgetView);
        } catch (Exception e) {
            Logger.e(Consts.LOG, e.getMessage());
        }
    }

    @Override  // create first
    public void onEnabled(Context context) {
        super.onEnabled(context);
        setAlarm(context);
        Logger.d(Consts.LOG, "widget onEnabled");
    }

    private void setAlarm(Context context) {
        // update every 1 minute
        Intent intent = new Intent(context, Widget4x1.class);
        intent.setAction(Consts.UPDATE_ALL_WIDGETS);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, rqId, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 60000, pIntent);
    }

    private void cancelAlarm(Context context) {
        Intent intent = new Intent(context, Widget4x1.class);
        intent.setAction(Consts.UPDATE_ALL_WIDGETS);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, rqId, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pIntent);
    }

    @Override  // delete one
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Logger.d(Consts.LOG, "widget onDeleted ");
    }

    @Override  // delete last
    public void onDisabled(Context context) {
        super.onDisabled(context);
        cancelAlarm(context);
        Logger.d(Consts.LOG, "widget onDisabled");
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        Logger.d(Consts.LOG, "widget onReceive");
        if (intent.getAction().equalsIgnoreCase(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            Logger.d(Consts.LOG, "widget onReceive update1");
            int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
            Bundle extras = intent.getExtras();
            if (extras != null) {
                mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            }
            if (mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                updateWidget(context, AppWidgetManager.getInstance(context), mAppWidgetId);
            }
        }
        // update by alarm
        else if (intent.getAction().equalsIgnoreCase(Consts.UPDATE_ALL_WIDGETS)) {
            Logger.d(Consts.LOG, "widget onReceive update2");
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
            for (int appWidgetID : ids) {
                updateWidget(context, appWidgetManager, appWidgetID);
            }
        }
    }
}
