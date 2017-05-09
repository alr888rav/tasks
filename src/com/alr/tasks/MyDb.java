package com.alr.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyDb extends SQLiteOpenHelper {
    static boolean needUpdate = false;
    final static String TBL = "alrScheduler";
    private final static String TBL_LOG = "alrlog";

    private SQLiteDatabase db;
    private static MyDb mInstance = null;
    private Context context;

    // Use the application context, which will ensure that you
    // don't accidentally leak an Activity's context.
    // See this article for more information: http://bit.ly/6LRzfx
    public static MyDb getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyDb(context);
        }
        return mInstance;
    }

    private MyDb(Context context) {
        super(context, Consts.DATABASE_NAME, null, Consts.DATABASE_VERSION);
        this.context = context;
        try {
            db = getWritableDatabase();
        }
        catch (SQLiteException ex){
            db = getReadableDatabase();
        }
    }

    @Override
    // Вызывается при создании базы на устройстве
    public void onCreate(SQLiteDatabase db) {
        createTblTasks(db);
        createTblLog(db);
        needUpdate = true;
    }

    @Override
    // Метод будет вызван, если изменится версия базы
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            createTblTasks(db);
            createTblLog(db);
        }
    }

    private void createTblTasks(SQLiteDatabase db) {
        String DB_QUERY = "DROP TABLE IF EXISTS " + TBL;
        db.execSQL(DB_QUERY);

        DB_QUERY =
            "CREATE TABLE IF NOT EXISTS " + TBL +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "active INTEGER NOT NULL, " +
            "mobile INTEGER, " +
            "bluetooth INTEGER, " +
            "wifi INTEGER," +
            "ring INTEGER," +
            "notify INTEGER," +
            "ring_val INTEGER," +
            "notify_val INTEGER," +
            "hour INTEGER NOT NULL, " +
            "minute INTEGER NOT NULL, " +
            "service INTEGER, " +
            "vibration INTEGER, "+
            "onoff INTEGER, " +
            "day1 INTEGER, " +
            "day2 INTEGER, " +
            "day3 INTEGER, " +
            "day4 INTEGER, " +
            "day5 INTEGER, " +
            "day6 INTEGER, " +
            "day7 INTEGER) ";
        //Используем sql-запрос для создания таблицы
        db.execSQL(DB_QUERY);
    }

    private void createTblLog(SQLiteDatabase db) {
        String DB_QUERY = "DROP TABLE IF EXISTS " + TBL_LOG;
        db.execSQL(DB_QUERY);
        DB_QUERY =
        "CREATE TABLE IF NOT EXISTS " + TBL_LOG +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "msg TEXT, " +
                "flag INTEGER" +
                ")";
        db.execSQL(DB_QUERY);
    }

    private int b2i(boolean b) {
        if (b)
            return 1;
        else
            return 0;
    }
    private boolean i2b(int i) {
        return (i!=0);
    }

    private ContentValues taskToContent(MyTask ts) {
        ContentValues cv = new ContentValues();
        cv.put("active", b2i(ts.active));
        cv.put("mobile", b2i(ts.mobile));
        cv.put("bluetooth", b2i(ts.bluetooth));
        cv.put("wifi", b2i(ts.wifi));
        cv.put("ring", b2i(ts.ring));
        cv.put("notify", b2i(ts.notify));
        cv.put("ring_val", ts.ring_value);
        cv.put("notify_val", ts.notify_value);
        cv.put("hour", ts.hour);
        cv.put("minute", ts.minute);
        cv.put("onoff", ts.onOff);
        cv.put("day1", b2i(ts.day1));
        cv.put("day2", b2i(ts.day2));
        cv.put("day3", b2i(ts.day3));
        cv.put("day4", b2i(ts.day4));
        cv.put("day5", b2i(ts.day5));
        cv.put("day6", b2i(ts.day6));
        cv.put("day7", b2i(ts.day7));
        cv.put("vibration", b2i(ts.vibration));
        return cv;
    }

    void addLog(String msg, int flag) {
        ContentValues cv = new ContentValues();
        cv.put("msg", msg);
        cv.put("flag", flag);
        db.insert(TBL_LOG, null, cv);
    }

    String getLastLog() {
        String query = "SELECT * FROM " + TBL_LOG + " where flag=1 order by _id DESC";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            String last = cursor.getString(cursor.getColumnIndex("msg"));
            cursor.close();
            return last;
        } else
            return "";
    }

    public void clearLog() {
        db.delete(TBL_LOG, null, null);
    }

    void addTask(MyTask ts) {
        ContentValues cv = taskToContent(ts);
        long rz = db.insert(TBL, null, cv);
        if (rz <= 0)
            Toast.makeText(context, "error insert", Toast.LENGTH_SHORT).show();
        needUpdate = true;
    }

    void updateTask(MyTask ts) {
        ContentValues cv = taskToContent(ts);
        db.update(TBL, cv, "_ID = ?", new String[] { String.valueOf(ts.id) });
        needUpdate = true;
    }

    void deleteTask(MyTask ts) {
        db.delete(TBL, "_ID = ?", new String[] { String.valueOf(ts.id) });
        needUpdate = true;
    }

    void deleteTask(int id) {
        db.delete(TBL, "_ID = ?", new String[] { String.valueOf(id) });
        needUpdate = true;
    }

    void deleteAllTask() {
        db.delete(TBL, null, null);
        needUpdate = true;
    }

    private MyTask cursorToTask(Cursor cursor) {
        MyTask ts = new MyTask(context, Consts.Mode.READ_TASK);
        ts.id = cursor.getInt(cursor.getColumnIndex("_id"));
        ts.hour = cursor.getInt(cursor.getColumnIndex("hour"));
        ts.minute = cursor.getInt(cursor.getColumnIndex("minute"));
        ts.mobile = i2b(cursor.getInt(cursor.getColumnIndex("mobile")));
        ts.bluetooth = i2b(cursor.getInt(cursor.getColumnIndex("bluetooth")));
        ts.wifi = i2b(cursor.getInt(cursor.getColumnIndex("wifi")));
        ts.ring = i2b(cursor.getInt(cursor.getColumnIndex("ring")));
        ts.notify = i2b(cursor.getInt(cursor.getColumnIndex("notify")));
        ts.ring_value = cursor.getInt(cursor.getColumnIndex("ring_val"));
        ts.notify_value = cursor.getInt(cursor.getColumnIndex("notify_val"));
        ts.ring_value = cursor.getInt(cursor.getColumnIndex("ring_val"));
        ts.onOff = i2b(cursor.getInt(cursor.getColumnIndex("onoff")));
        ts.active = i2b(cursor.getInt(cursor.getColumnIndex("active")));
        ts.day1 = i2b(cursor.getInt(cursor.getColumnIndex("day1")));
        ts.day2 = i2b(cursor.getInt(cursor.getColumnIndex("day2")));
        ts.day3 = i2b(cursor.getInt(cursor.getColumnIndex("day3")));
        ts.day4 = i2b(cursor.getInt(cursor.getColumnIndex("day4")));
        ts.day5 = i2b(cursor.getInt(cursor.getColumnIndex("day5")));
        ts.day6 = i2b(cursor.getInt(cursor.getColumnIndex("day6")));
        ts.day7 = i2b(cursor.getInt(cursor.getColumnIndex("day7")));
        ts.vibration = i2b(cursor.getInt(cursor.getColumnIndex("vibration")));
        return ts;
    }

    MyTask getTask(int id) {
        //Cursor cursor = db.query(TBL, new String[] { "*" }, "_ID = ? ", new String[] { Integer.toString(id)} , null, null, null);
        String query = "SELECT * FROM " + TBL + " where _id= " + Integer.toString(id);
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        return cursorToTask(cursor);
    }
    // for display
    ArrayList<Map<String, Object>> getAllTasks() {
        ArrayList<Map<String, Object>> tsList = new ArrayList<>();
        String query = "SELECT * FROM " + TBL + " ORDER BY hour, minute";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            do {
                MyTask ts = cursorToTask(cursor);
                HashMap<String, Object> m = new HashMap<>();
                m.put("_id", ts.id);
                m.put("active", ts.activeToString());
                m.put("time", ts.timeToString());
                m.put("days", ts.daysToString());
                m.put("services", ts.serviceToString());
                if (ts.mobile || ts.bluetooth || ts.wifi)
                    m.put("do", ts.onoffToString());
                else if (ts.ring || ts.notify)
                    m.put("do", ts.volumeToString());
                tsList.add(m);
            } while(cursor.moveToNext());
        }
        return tsList;
    }

    public ArrayList<Map<String, Object>> getLog() {
        ArrayList<Map<String, Object>> dataList = new ArrayList<>();
        String query = "SELECT * FROM " + TBL_LOG + " order by _id";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            do {
                HashMap<String, Object> m = new HashMap<>();
                String txt = cursor.getString(cursor.getColumnIndex("msg"));
                m.put("msg", txt);
                dataList.add(m);
            } while(cursor.moveToNext());
        }
        cursor.close();
        return dataList;
    }

    // for service
    ArrayList<MyTask> getAllTasksData() {
        ArrayList<MyTask> tsList = new ArrayList<>();
        String query = "SELECT * FROM " + TBL;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            do {
                MyTask ts = cursorToTask(cursor);
                tsList.add(ts);
            } while(cursor.moveToNext());
        }
        return tsList;
    }

    // for service
    int getTasksCount() {
        String query = "SELECT * FROM " + TBL;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            int count = cursor.getCount();
            cursor.close();
            return count;
        } else
            return 0;
    }

}
