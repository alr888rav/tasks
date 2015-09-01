package com.alr.tasks;

import java.util.Calendar;

public class Time {
    public int hour;
    public int minute;
    public int day;
    public int month;
    public int year;

    Time() {
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH)+1;  // 1..12
        year = calendar.get(Calendar.YEAR);
    }
}

