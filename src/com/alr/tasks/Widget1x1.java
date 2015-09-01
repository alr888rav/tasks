package com.alr.tasks;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

public class Widget1x1 extends AppWidgetProvider {

    @Override  // create first
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Logger.d(Consts.LOG, "widget onEnabled");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        // Обновляем виджет
        for (int id : appWidgetIds) {
            updateWidget(context, appWidgetManager, id);
        }
        Logger.d(Consts.LOG, "widget onUpdate ");
    }

    static void updateWidget(Context context, AppWidgetManager appWidgetManager, int widgetID) {
        try {
            // Настраиваем внешний вид виджета
            RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget1x1);
            Setup setup = new Setup(context);
            boolean act = setup.active;
            if (act) {
                widgetView.setTextViewText(R.id.statusText, context.getString(R.string.widget_status_on));
                widgetView.setInt(R.id.statusText, "setBackgroundColor", R.color.green);
            } else {
                widgetView.setTextViewText(R.id.statusText, context.getString(R.string.widget_status_off));
                widgetView.setInt(R.id.statusText, "setBackgroundColor", R.color.gray);
            }
            //
            Intent configIntent = new Intent(context, MainActivity.class);
            configIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
            PendingIntent pIntent = PendingIntent.getActivity(context, widgetID, configIntent, 0);
            widgetView.setOnClickPendingIntent(R.id.widgetIimage, pIntent);

            appWidgetManager.updateAppWidget(widgetID, widgetView);
        } catch (Exception e) {
            Logger.e(Consts.LOG, e.getMessage());
        }
    }

    @Override  // delete one
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Logger.d(Consts.LOG, "widget onDeleted ");
    }

    @Override  // delete last
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Logger.d(Consts.LOG, "widget onDisabled");
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equalsIgnoreCase(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
            Bundle extras = intent.getExtras();
            if (extras != null) {
                mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            }
            if (mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                updateWidget(context, AppWidgetManager.getInstance(context), mAppWidgetId);
            }
        }
    }
}
