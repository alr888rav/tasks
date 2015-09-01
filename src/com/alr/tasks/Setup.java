package com.alr.tasks;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Setup {
    public boolean active;
    public boolean confirmDelete;
    public boolean confirmDisable;
    public boolean confirmStop;
    public boolean showNotify;
    public boolean showIcon;
    public boolean notifySound;
    public boolean notifyWithRing;

    Setup(Context context) {
        read(context);
    }

    public void read (Context context) {
        SharedPreferences sp = context.getSharedPreferences("myPref", Activity.MODE_PRIVATE);
        active = sp.getBoolean("active", true);
        confirmDelete = sp.getBoolean("confirm_delete", true);
        confirmDisable = sp.getBoolean("confirm_disable", true);
        confirmStop = sp.getBoolean("confirm_stop", true);
        showNotify = sp.getBoolean("show_tray", true);
        showIcon = sp.getBoolean("show_icon", true);
        notifySound = sp.getBoolean("notify_sound", false);
        notifyWithRing = sp.getBoolean("notify_with_ring", true);
    }

    public void write (Context context) {
        SharedPreferences sp = context.getSharedPreferences("myPref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("active", active);
        ed.putBoolean("confirm_delete", confirmDelete);
        ed.putBoolean("confirm_disable", confirmDisable);
        ed.putBoolean("confirm_stop", confirmStop);
        ed.putBoolean("show_tray", showNotify);
        ed.putBoolean("show_icon", showIcon);
        ed.putBoolean("notify_sound", notifySound);
        ed.putBoolean("notify_with_ring", notifyWithRing);
        ed.apply();
    }

}
