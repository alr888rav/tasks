package com.alr.tasks;

import android.content.Context;
import java.io.IOException;
import java.io.Serializable;

public class MyTask implements Serializable {

    private static final long serialVersionUID = 0L;

    public enum Result { NONE, OK, ERROR }
    public int id = -1;
    public boolean active = true;
    public boolean mobile = true;
    public boolean bluetooth = false;
    public boolean wifi = false;
    public boolean onOff = false;
    public boolean ring = false;
    public boolean notify = false;
    public int ring_value = 0;
    public int notify_value = 0;
    public boolean vibration = false;
    public boolean day1 = true;
    public boolean day2 = true;
    public boolean day3 = true;
    public boolean day4 = true;
    public boolean day5 = true;
    public boolean day6 = false;
    public boolean day7 = false;
    public int hour;
    public int minute;
    public Result result = Result.NONE;
    public Consts.Mode mode;
    private Context context;

    MyTask(Context context, Consts.Mode mode) {
        this.context = context;
        this.mode = mode;
        Time t = new Time();
        hour = t.hour;
        minute = t.minute;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeInt(id);
        out.writeInt(hour);
        out.writeInt(minute);
        out.writeBoolean(active);
        out.writeBoolean(bluetooth);
        out.writeBoolean(mobile);
        out.writeBoolean(wifi);
        out.writeBoolean(ring);
        out.writeBoolean(notify);
        out.writeBoolean(onOff);
        out.writeInt(ring_value);
        out.writeInt(notify_value);
        out.writeBoolean(day1);
        out.writeBoolean(day2);
        out.writeBoolean(day3);
        out.writeBoolean(day4);
        out.writeBoolean(day5);
        out.writeBoolean(day6);
        out.writeBoolean(day7);
        out.writeObject(mode);
        out.writeBoolean(vibration);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        id = in.readInt();
        hour = in.readInt();
        minute = in.readInt();
        active = in.readBoolean();
        bluetooth = in.readBoolean();
        mobile = in.readBoolean();
        wifi = in.readBoolean();
        ring = in.readBoolean();
        notify = in.readBoolean();
        onOff = in.readBoolean();
        ring_value = in.readInt();
        notify_value = in.readInt();
        day1 = in.readBoolean();
        day2 = in.readBoolean();
        day3 = in.readBoolean();
        day4 = in.readBoolean();
        day5 = in.readBoolean();
        day6 = in.readBoolean();
        day7 = in.readBoolean();
        mode = (Consts.Mode)in.readObject();
        vibration = in.readBoolean();
    }

    public String serviceToString() {
        StringBuilder sb = new StringBuilder();
        if (mobile)
            sb.append(context.getString(R.string.mobile_data));
        if (wifi)
            sb.append(context.getString(R.string.wifi));
        if (bluetooth)
            sb.append(context.getString(R.string.bluetooth));
        if (ring)
            sb.append(context.getString(R.string.ring));
        if (notify)
            sb.append(context.getString(R.string.notify));
        return sb.toString();
    }

    public String daysToString() {
        StringBuilder sb = new StringBuilder();
        if (day1) {
            sb.append(context.getString(R.string.monday_short));
            sb.append(" ");
        }
        if (day2) {
            sb.append(context.getString(R.string.tuesday_short));
            sb.append(" ");
        }
        if (day3) {
            sb.append(context.getString(R.string.wednesday_short));
            sb.append(" ");
        }
        if (day4) {
            sb.append(context.getString(R.string.thursday_short));
            sb.append(" ");
        }
        if (day5) {
            sb.append(context.getString(R.string.friday_short));
            sb.append(" ");
        }
        if (day6) {
            sb.append(context.getString(R.string.saturday_short));
            sb.append(" ");
        }
        if (day7) {
            sb.append(context.getString(R.string.sunday_short));
            sb.append(" ");
        }
        return sb.toString();
    }

    public String timeToString() {
        return String.format("%02d:%02d", hour, minute);
    }

    public String onoffToString() {
        if (mobile || bluetooth || wifi) {
            if (onOff)
                return (context.getString(R.string.to_on_text));
            else
                return (context.getString(R.string.to_off_text));
        }
        else
            return "";
    }

    public String activeToString() {
        if (active)
            return (context.getString(R.string.on_text_short));
        else
            return (context.getString(R.string.off_text_short));
    }

    public String volumeToString() {
        if (ring || notify) {
            if (ring) {
                String s = Integer.toString(ring_value) + "%";
                if (vibration)
                    s += " "+context.getString(R.string.vibro)+" "+context.getString(R.string.on_text_short);
                return s;
            }else
                return Integer.toString((notify_value))+"%";
        }
        return "";
    }

    private void showNotify(String title, String text, boolean playSoundVibration) {
        Setup setup = new Setup(context);
        if (setup.showNotify)
            new Notifi(context).show(title, text, setup.showIcon, playSoundVibration);
    }

    private String b2text(boolean b) {
        if (b)
            return context.getString(R.string.on_text);
        else
            return context.getString(R.string.off_text);
    }

    public void execTask() {
        int max;
        Logger.d(Consts.LOG, "execTask");
        Setup setup = new Setup(context);
        Phone phone = new Phone(context);
        Time t = new Time();
        String st = String.format("%d.%02d.%d %02d:%02d", t.day,t.month,t.year,t.hour,t.minute);
        final Messages msg = new Messages(context);
        try {
            if (mobile) {
                Logger.d(Consts.LOG, "mobile " + onOff);
                if (onOff != phone.isMobileDataEnabled()) {
                    phone.setMobileData(onOff);
                    showNotify(context.getString(R.string.app_name2), context.getString(R.string.mobile_data) + " " + b2text(onOff), setup.notifySound);
                    msg.sendLog(st + " " + context.getString(R.string.mobile_data) + " " + b2text(onOff));
                }
            }
            else if (bluetooth) {
                Logger.d(Consts.LOG, "bt " + onOff);
                if (onOff != phone.isBluetoothEnabled()) {
                    phone.setBluetooth(onOff);
                    showNotify(context.getString(R.string.app_name2), context.getString(R.string.bluetooth) + " " + b2text(onOff), setup.notifySound);
                    msg.sendLog(st + " " + context.getString(R.string.bluetooth) + " " + b2text(onOff));
                }
            }
            else if (wifi) {
                Logger.d(Consts.LOG, "wifi " + onOff);
                if (onOff != phone.isWifiEnabled()) {
                    phone.setWifi(onOff);
                    showNotify(context.getString(R.string.app_name2), context.getString(R.string.wifi) + " " + b2text(onOff), setup.notifySound);
                    msg.sendLog(st + " " + context.getString(R.string.wifi) + " " + b2text(onOff));
                }
            }
            else if (ring) {
                Logger.d(Consts.LOG, "ring " + Integer.toString(ring_value) + "%");
                max = phone.getMaxRingVolume();
                int newVolume = Math.round((float) ring_value / 100*max);
                if (phone.getRingVolume() != newVolume) {
                    phone.setRingVolume(newVolume, vibration);
                    showNotify(context.getString(R.string.app_name2), context.getString(R.string.ring_vol) + " " + Integer.toString(ring_value), setup.notifySound);
                    msg.sendLog(st + " " + context.getString(R.string.ring_vol) + " " + Integer.toString(ring_value));
                }
            }
            else if (notify) {
                Logger.d(Consts.LOG, "notify " + Integer.toString(notify_value) + "%");
                max = phone.getMaxNotifyVolume();
                int newVolume = Math.round((float)ring_value/100*max);
                if (phone.getNotifyVolume() != newVolume) {
                    phone.setNotifyVolume(newVolume, vibration);
                    showNotify(context.getString(R.string.app_name2), context.getString(R.string.notify_vol) + " " + Integer.toString(notify_value), setup.notifySound);
                    msg.sendLog(st + " " + context.getString(R.string.notify_vol) + " " + Integer.toString(notify_value));
                }
            } else {
                Logger.d(Consts.LOG, "unknown service");
            }
            result = MyTask.Result.OK;
        } catch (Exception e) {
            Logger.e(Consts.LOG, "exec task: "+e.getMessage());
            result = MyTask.Result.ERROR;
        }
    }


}
