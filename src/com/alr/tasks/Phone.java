package com.alr.tasks;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import java.lang.reflect.Method;

public class Phone {
    private Context context;

    Phone(Context context) {
        this.context = context;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null;
    }
    public boolean isBlueToothAvailable() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter != null;
    }
    public boolean isMobileDataEnabled() {
        try {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Method method = ConnectivityManager.class.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true); // Make the method callable
            // get the setting for "mobile data"
            return (Boolean)method.invoke(cm);
        } catch (Exception e) {
            return false;
        }
    }
    public boolean setMobileData(boolean b) {
        ConnectivityManager dataManager;
        dataManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Method dataMtd;
        try {
            dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
            dataMtd.setAccessible(true);
            dataMtd.invoke(dataManager, b);
            Thread.sleep(1000L);
            return true;
        } catch (Exception e) {
            Logger.d(Consts.LOG, e.getMessage());
            return false;
        }
    }
    public void setBluetooth(boolean b) {
        if (b)
            context.startActivity(new Intent(context, BlueToothRequestActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        else
            BluetoothAdapter.getDefaultAdapter().disable();
    }
    public boolean isBluetoothEnabled() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled();
    }
    public void setWifi(boolean b) {
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(b);
    }
    public boolean isWifiEnabled() {
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }
    // 0-max
    public void setRingVolume(int volume, boolean vibrate) {
        Setup setup = new Setup(context);
        AudioManager mgr = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        int streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_RING);
        if (volume > streamVolumeMax)
            volume = streamVolumeMax;
        if (volume < 0)
            volume = 0;
        // order by!
        // 1-set volume
        // 2-set vibration mode
        mgr.setStreamVolume(AudioManager.STREAM_RING, volume, AudioManager.FLAG_ALLOW_RINGER_MODES);
        if (setup.notifyWithRing)
            mgr.setStreamVolume(AudioManager.STREAM_NOTIFICATION, volume, AudioManager.FLAG_ALLOW_RINGER_MODES);
        if (volume != 0) {
            mgr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        } else {
            if (vibrate)
                mgr.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            else
                mgr.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }
    }
    public int getMaxRingVolume() {
        AudioManager mgr = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        return mgr.getStreamMaxVolume(AudioManager.STREAM_RING);
    }
    // 0-max
    public int getRingVolume() {
        AudioManager mgr = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        return mgr.getStreamVolume(AudioManager.STREAM_RING);
    }
    // 0-max
    public void setNotifyVolume(int volume, boolean vibrate) {
        AudioManager mgr = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        int streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
        if (volume > streamVolumeMax)
            volume = streamVolumeMax;
        if (volume < 0)
            volume = 0;
        mgr.setStreamVolume(AudioManager.STREAM_NOTIFICATION, volume, AudioManager.FLAG_ALLOW_RINGER_MODES);
        if (volume != 0) {
            mgr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        } else {
            if (vibrate)
                mgr.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            else
                mgr.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }
    }

    public int getMaxNotifyVolume() {
        AudioManager mgr = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        return mgr.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
    }

    // 0-max
    public int getNotifyVolume() {
        AudioManager mgr = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        return mgr.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
    }
}
