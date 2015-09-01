package com.alr.tasks;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Tasks {

    private static int day = -1;
    private static int month = -1;
    private static ArrayList<MyTask> data;
    private Context context;

    Tasks(Context context) {
        this.context = context;
        if (day < 0) {
            Time date = new Time();
            day = date.day;
            month = date.month;
        }
    }

    private void readTasks() {
        MyDb myDb = MyDb.getInstance(context);
        data = myDb.getAllTasksData();
        MyDb.needUpdate = false;
    }

    public void update() {
        readTasks();
    }

    public int getDayOfWeek() {
        Calendar c = Calendar.getInstance();
        int dow = c.get(Calendar.DAY_OF_WEEK);
        if (dow == Calendar.SUNDAY)
            dow = 7;
        else
            dow--;
        return dow;
    }

    private MyTask getTask(ArrayList<MyTask> data, int id) {
        for (MyTask ts : data) {
            if (ts.id == id)
                return ts;
        }
        return null;
    }

    public void someTask(final Time t, final int taskId) {
        new Thread(new Runnable() {
            public void run() {
                String dt = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
                Logger.d(Consts.LOG, "some task = " + dt + " id=" + taskId);

                Setup setup = new Setup(context);
                if (!setup.active)
                    return;

                if (data == null || MyDb.needUpdate)
                    update();

                // new day
                if (t.day != day || t.month != month ) {
                    Logger.d(Consts.LOG, "new day = " + dt);
                    for (MyTask ts : data)
                        ts.result = MyTask.Result.NONE;
                    day = t.day;
                    month = t.month;
                }


                int d = getDayOfWeek();
                Logger.d(Consts.LOG, "now= " + dt + " day=" + d + " time=" + t.hour + ":" + t.minute);
                //for (MyTask ts : data) {
                MyTask ts = getTask(data, taskId);
                if (ts != null) {
                    boolean dayOk = false;
                    switch (d) {
                        case 1:
                            dayOk = ts.day1;
                            break;
                        case 2:
                            dayOk = ts.day2;
                            break;
                        case 3:
                            dayOk = ts.day3;
                            break;
                        case 4:
                            dayOk = ts.day4;
                            break;
                        case 5:
                            dayOk = ts.day5;
                            break;
                        case 6:
                            dayOk = ts.day6;
                            break;
                        case 7:
                            dayOk = ts.day7;
                            break;
                    }
                    Logger.d(Consts.LOG, "id=" + ts.id + " dayok=" + dayOk + " h=" + ts.hour + " m=" + ts.minute + " act=" + ts.active + " res=" + ts.result);
                    if (dayOk && ts.active && ts.result != MyTask.Result.OK && ts.hour == t.hour && t.minute >= ts.minute && t.minute <= ts.minute+5) {
                        ts.execTask();
                    }
                }
                //}
            }
        }).start();
    }

}
