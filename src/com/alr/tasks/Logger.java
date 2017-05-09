package com.alr.tasks;

import android.content.Context;
import android.util.Log;

public class Logger {
    private static Context context;
    private static boolean toDB = false;
    public static boolean enabled = true;

    Logger(Context context) {
        Logger.context = context;
    }

    @SuppressWarnings("unused")
    public void setToDB(boolean toDB) {
        Logger.toDB = toDB;
    }

    @SuppressWarnings("unused")
    public static void i(String tag, String msg) {
        if (enabled) {
            Log.i(tag, msg);
            Time t = new Time();
            String st = String.format("%d.%02d.%d %02d:%02d", t.day,t.month,t.year,t.hour,t.minute);
            db(st+" "+msg, 0);
        }
    }

    public static void e(String tag, String msg) {
        if (enabled) {
            Log.d(tag, msg);
            Time t = new Time();
            String st = String.format("%d.%02d.%d %02d:%02d", t.day,t.month,t.year,t.hour,t.minute);
            db(st + " " + msg, 0);
        }
    }

    public static void d(String tag, String msg) {
        if (enabled) {
            Log.d(tag, msg);
            Time t = new Time();
            String st = String.format("%d.%02d.%d %02d:%02d", t.day, t.month, t.year, t.hour, t.minute);
            db(st + " " + msg, 0);
        }
    }

    public static void db(String msg, int flag) {
        try {
            if (toDB) {
                MyDb myDb = MyDb.getInstance(context);
                myDb.addLog(msg, flag);
            }
        } catch (Exception e) {
            Log.e(Consts.LOG, e.getMessage());
        }
    }

    public static void db(String msg) {
        toDB = true;
        db(msg, 1);
        toDB = false;
    }

}
