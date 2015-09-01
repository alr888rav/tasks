package com.alr.tasks;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import java.util.ArrayList;
import java.util.Map;

import static android.view.MotionEvent.*;

public class MainActivity extends Activity {
    private final int MENU_TASK_DEL = 1;
    private final int MENU_TASK_RUN = 2;

    private ArrayList<Map<String, Object>> data;
    private float x, dx;
    private int taskViewPosition;
    private MyDb myDb;
    private BroadcastReceiver mReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        View menu = findViewById(R.id.mainMenu);
        menu.setVisibility(View.GONE);

        new Logger(getApplicationContext());
        myDb = MyDb.getInstance(getApplicationContext());

        showAllTasks();
        ListView lv = (ListView)findViewById(R.id.taskView);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isMainMenuVisible())
                    viewTask(position);
            }
        });
        // task context menu
        registerForContextMenu(lv);
        receiver();
        log("");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.taskView) {
            taskViewPosition = ((AdapterView.AdapterContextMenuInfo)menuInfo).position;
            menu.add(0, MENU_TASK_RUN, 0, getString(R.string.confirm_task_run));
            menu.add(0, MENU_TASK_DEL, 0, getString(R.string.confirm_task_delete));
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_TASK_DEL:
                deleteTask(taskViewPosition);
                break;
            case MENU_TASK_RUN:
                runTask(taskViewPosition);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void log(String msg) {
        TextView v = (TextView)findViewById(R.id.logText);
        v.setText(msg);
        LinearLayout l = (LinearLayout)findViewById(R.id.logLayout);
        if (msg.length() != 0) {
            l.setVisibility(View.VISIBLE);
            Logger.db(msg);
        } else
            l.setVisibility(View.GONE);
    }

    private void lastlog() {
        String last = myDb.getLastLog();
        log(last);
    }

    // internal messages
    private void receiver() {
        IntentFilter intentFilter = new IntentFilter(Consts.FILTER_APP);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //extract our message from intent
                String msg_for_me = intent.getStringExtra("msg");
                if (msg_for_me != null) {
                    if (msg_for_me.equals(Messages.start_msg))
                        startAlarm();
                    else if (msg_for_me.equals(Messages.stop_msg))
                        stopAlarm();
                    else if (msg_for_me.equals(Messages.read_tasks))
                        showAllTasks();
                    else if (msg_for_me.equals(Messages.show_icon))
                        showIcon();
                    else if (msg_for_me.equals(Messages.hide_icon))
                        hideIcon();
                    else if (msg_for_me.equals(Messages.log)) {
                        String s = intent.getStringExtra("text");
                        log(s);
                    }
                }
            }
        };
        this.registerReceiver(mReceiver, intentFilter);
    }

    private void showAllTasks() {
        SimpleAdapter sAdapter;
        data = myDb.getAllTasks();
        // массив имен атрибутов, из которых будут читаться данные
        String[] from = { "time", "days", "services", "do", "active" };
        // массив ID View-компонентов, в которые будут вставлять данные
        int[] to = { R.id.rowTime, R.id.rowDays, R.id.rowServices, R.id.rowActive, R.id.rowTaskActive };
        // создаем адаптер
        sAdapter = new SimpleAdapter(this, data, R.layout.task_row, from, to);
        // определяем список и присваиваем ему адаптер
        ListView lv = (ListView)findViewById(R.id.taskView);
        lv.setAdapter(sAdapter);
        registerForContextMenu(lv);
    }

    private boolean isMainMenuVisible() {
        View menu = findViewById(R.id.mainMenu);
        return (menu.getVisibility()==View.VISIBLE);
    }

    private void bgEnable(boolean mode) {
        LinearLayout la = (LinearLayout)findViewById(R.id.mainLayout);
        la.setEnabled(mode);
        ImageButton ib = (ImageButton)findViewById(R.id.fab_image_button);
        if (mode) {
            ib.setVisibility(View.VISIBLE);
        } else {
            ib.setVisibility(View.INVISIBLE);
        }
    }

    private void showMainMenu() {
        View menu = findViewById(R.id.mainMenu);
        menu.setTop(0);
        menu.setVisibility(View.VISIBLE);
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.menu_slide_right);
        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ImageView iv = (ImageView)findViewById(R.id.menuRightSide);
                iv.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ImageView iv = (ImageView)findViewById(R.id.menuRightSide);
                iv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        menu.startAnimation(slide);
        bgEnable(false);
        // slide menu off screen
        menu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return myTouch(motionEvent);
            }
        });

    }

    private void hideMainMenu() {
        if (isMainMenuVisible()) {
            View menu = findViewById(R.id.mainMenu);
            Animation slide = AnimationUtils.loadAnimation(this, R.anim.menu_slide_left);
            slide.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    ImageView iv = (ImageView)findViewById(R.id.menuRightSide);
                    iv.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    ImageView iv = (ImageView)findViewById(R.id.menuRightSide);
                    iv.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            menu.setOnTouchListener(null);  // off
            bgEnable(true);
            menu.startAnimation(slide);
            menu.setVisibility(View.INVISIBLE);
        }
    }

    public void addTaskButtonClick(View v) {
        hideMainMenu();
        MyTask ts = new MyTask(getApplicationContext(), Consts.Mode.ADD_TASK);
        Intent intent = new Intent(MainActivity.this, SchedActivity.class);
        intent.putExtra("task", ts);
        startActivityForResult(intent, Consts.ADD_TAG);
    }

    private void viewTask(int position) {
        hideMainMenu();
        int id = (int)data.get(position).get("_id");
        MyTask ts = myDb.getTask(id);
        if (ts != null) {
            Intent intent = new Intent(MainActivity.this, SchedActivity.class);
            intent.putExtra("task", ts);
            startActivityForResult(intent, Consts.MODIFY_TAG);
        }
    }

    private void deleteTask(int position) {
        int id = (int) data.get(position).get("_id");
        myDb.deleteTask(id);
        showAllTasks();
    }

    private void runTask(int position) {
        int id = (int) data.get(position).get("_id");
        MyTask ts = myDb.getTask(id);
        ts.execTask();
        showAllTasks();
    }

    public void mainMenuButtonClick(View v) {
        if (!isMainMenuVisible())
            showMainMenu();
        else
            hideMainMenu();
    }

    public void titleMenuClick(View v) {
        hideMainMenu();
    }

    public void setupMenuClick(View v) {
        hideMainMenu();
        startActivity(new Intent(MainActivity.this, SetupActivity.class));

    }
    public void aboutMenuClick(View v) {
        hideMainMenu();
        startActivity(new Intent(MainActivity.this, AboutActivity.class));
    }

    public void updateMenuClick(View v) {
        hideMainMenu();
        new Updater(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Consts.ADD_TAG:
            case Consts.MODIFY_TAG:
                BackupAgent.requestBackup(this);
                reReadTasks();
                showAllTasks();
                MyDb.needUpdate = true;
                break;
        }
    }

    public boolean myTouch(MotionEvent event) {
        switch (event.getAction()) {
            case ACTION_DOWN: // нажатие
                x = event.getX();
                break;
            case ACTION_MOVE: // движение
                dx = x - event.getX();
                break;
            case ACTION_UP: // отпускание
            case ACTION_CANCEL:
                if (dx > 10)
                    hideMainMenu();
                break;
        }
        x = event.getX();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        reReadTasks();
        showIcon();
        lastlog();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(this.mReceiver);
    }

    private void reReadTasks() {
        startAlarm();
    }

    private void startAlarm() {
        AlarmReceiver alarm = new AlarmReceiver();
        alarm.setAlarm(this);
        Logger.d(Consts.LOG, "start alarm");
    }
    private void stopAlarm() {
        Logger.d(Consts.LOG, "stop alarm");
    }

    private void hideIcon() {
        stopService(new Intent(this, IconService.class));
        startService(new Intent(this, IconService.class));
    }

    private void showIcon() {
        startService(new Intent(this, IconService.class));
    }

    public void hideLogClick(View v) {
        log("");
    }
}
