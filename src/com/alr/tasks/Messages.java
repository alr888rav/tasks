package com.alr.tasks;

import android.content.Context;
import android.content.Intent;

public class Messages {
    public static String start_msg = "START";
    public static String stop_msg = "STOP";
    public static String read_tasks = "READ";
    public static String hide_icon = "HIDE";
    public static String show_icon = "SHOW";
    public static String log = "LOG";
    private Context context;

    Messages(Context context) {
        this.context = context;
    }

    public void send(String msg) {
        Intent i = new Intent(Consts.FILTER_APP).putExtra("msg", msg);
        context.sendBroadcast(i);
    }

    public void sendLog(String msg) {
        Intent i = new Intent(Consts.FILTER_APP);
        i.putExtra("msg", Messages.log);
        i.putExtra("text", msg);
        context.sendBroadcast(i);
    }

}
