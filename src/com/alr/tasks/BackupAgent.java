package com.alr.tasks;

import android.app.backup.*;
import android.content.Context;
import android.os.ParcelFileDescriptor;

import java.io.IOException;

public class BackupAgent extends BackupAgentHelper {

    // The name of the SharedPreferences file
    static final String PREFS = "PREFS";

    // A key to uniquely identify the set of backup data
    static final String PREFS_BACKUP_KEY = "My_PREFS";
    static final String DB_BACKUP_KEY = "My_DB";
    // Object for intrinsic lock
    static final Object sDataLock = new Object();

    @Override
    public void onCreate() {

        SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(this, PREFS);
        addHelper(PREFS_BACKUP_KEY, helper);

        addHelper(DB_BACKUP_KEY, new DbBackupHelper(this, MyDb.TBL));
    }

    // метод для запроса бэкапа. Следует вызывать
    // метод всякий раз, когда данные изменились.
    public static void requestBackup(Context context) {
        BackupManager backupManager = new BackupManager(context);
        backupManager.dataChanged();
    }

    @Override
    public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data,
                         ParcelFileDescriptor newState) throws IOException {
        // Hold the lock while the FileBackupHelper performs backup
        synchronized (sDataLock) {
            super.onBackup(oldState, data, newState);
        }
    }

    @Override
    public void onRestore(BackupDataInput data, int appVersionCode,
                          ParcelFileDescriptor newState) throws IOException {
        // Hold the lock while the FileBackupHelper restores the file
        synchronized (sDataLock) {
            super.onRestore(data, appVersionCode, newState);
        }
    }
}