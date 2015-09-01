package com.alr.tasks;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class SchedActivity extends Activity {
    final private int service_tag = 1;
    final private int time_tag = 2;
    final private int days_tag = 3;
    final private int volume_tag = 4;
    final private int onoff_tag = 5;
    private MyTask ts;

    private void showTask(MyTask ts) {
        TextView t = (TextView)findViewById(R.id.serviceTextView);
        t.setText(ts.serviceToString());
        t = (TextView)findViewById(R.id.timeOnText);
        t.setText(ts.timeToString());
        t = (TextView)findViewById(R.id.daysText);
        t.setText(ts.daysToString());
        CheckBox c = (CheckBox)findViewById(R.id.shedActCheckBox);
        c.setChecked(ts.active);
        Button b = (Button)findViewById(R.id.volumeButton);
        b.setEnabled(ts.ring || ts.notify);
        b = (Button)findViewById(R.id.onOffButton);
        b.setEnabled(ts.mobile || ts.bluetooth || ts.wifi);
        t = (TextView)findViewById(R.id.onOffText);
        t.setText(ts.onoffToString());
        t = (TextView)findViewById(R.id.volumeText);
        t.setText(ts.volumeToString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_shedule);
        ts = (MyTask)getIntent().getSerializableExtra("task");
        ts.setContext(getApplicationContext());
        showTask(ts);

        final CheckBox cb = (CheckBox)findViewById(R.id.shedActCheckBox);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb.isChecked()) {
                    ts.active = cb.isChecked();
                } else {
                    Setup setup = new Setup(getApplicationContext());
                    if (setup.confirmDisable) {
                        CallBack cbYes = new CallBack() {
                            @Override
                            public void execute() {
                                ts.active = cb.isChecked();
                            }
                        };
                        CallBack cbCancel = new CallBack() {
                            @Override
                            public void execute() {
                                cb.setChecked(ts.active);
                            }
                        };
                        Resources res = getApplicationContext().getResources();
                        new MyDialogs()
                                .setTitle(res.getString(R.string.confirm_text))
                                .setMessage(res.getString(R.string.confirm_task_stop))
                                .setButtons(res.getString(R.string.ok), res.getString(R.string.cancel))
                                .setCallBack(cbYes, cbCancel, cbCancel).show(SchedActivity.this);
                    }
                }
            }
        });
    }

    public void serviceButtonClick(View v) {
        Intent intent = new Intent(SchedActivity.this, ServicesActivity.class);
        intent.putExtra("task", ts);
        startActivityForResult(intent, service_tag);
    }

    public void timeButtonClick(View v) {
        Intent intent = new Intent(SchedActivity.this, TimeActivity.class);
        intent.putExtra("task", ts);
        startActivityForResult(intent, time_tag);
    }

    public void daysButtonClick(View v) {
        Intent intent = new Intent(SchedActivity.this, DaysActivity.class);
        intent.putExtra("task", ts);
        startActivityForResult(intent, days_tag);
    }

    public void onoffButtonClick(View v) {
        Intent intent = new Intent(SchedActivity.this, OnOffActivity.class);
        intent.putExtra("task", ts);
        startActivityForResult(intent, onoff_tag);
    }

    public void volumeButtonClick(View v) {
        Intent intent = new Intent(SchedActivity.this, VolumeActivity.class);
        intent.putExtra("task", ts);
        startActivityForResult(intent, volume_tag);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        if (resultCode != RESULT_OK) {return;}
        switch (requestCode) {
            case service_tag:
                boolean md = data.getBooleanExtra("mobile", false);
                boolean bt = data.getBooleanExtra("bluetooth", false);
                boolean wf = data.getBooleanExtra("wifi", false);
                boolean rv = data.getBooleanExtra("ring", false);
                boolean nv = data.getBooleanExtra("notify", false);
                ts.mobile = md;
                ts.bluetooth = bt;
                ts.wifi = wf;
                ts.ring = rv;
                ts.notify = nv;
                break;
            case time_tag:
                int h = data.getIntExtra("hour", 0);
                int m = data.getIntExtra("min", 0);
                ts.hour = h;
                ts.minute = m;
                break;
            case days_tag:
                boolean d1 = data.getBooleanExtra("monday", false);
                boolean d2 = data.getBooleanExtra("tuesday", false);
                boolean d3 = data.getBooleanExtra("wednesday", false);
                boolean d4 = data.getBooleanExtra("thursday", false);
                boolean d5 = data.getBooleanExtra("friday", false);
                boolean d6 = data.getBooleanExtra("saturday", false);
                boolean d7 = data.getBooleanExtra("sunday", false);
                ts.day1 = d1;
                ts.day2 = d2;
                ts.day3 = d3;
                ts.day4 = d4;
                ts.day5 = d5;
                ts.day6 = d6;
                ts.day7 = d7;
                break;
            case volume_tag:
                int v = data.getIntExtra("volume", 0);
                if (ts.ring)
                    ts.ring_value = v;
                if (ts.notify)
                    ts.notify_value = v;
                ts.vibration = data.getBooleanExtra("vibro", false);
                break;
            case onoff_tag:
                ts.onOff = data.getBooleanExtra("onoff", false);
                break;
        }
        showTask(ts);
    }

    public void saveButtonClick(View v) {
        MyDb myDb = MyDb.getInstance(getApplicationContext());
        if (ts.mode == Consts.Mode.ADD_TASK)
            myDb.addTask(ts);
        else
            myDb.updateTask(ts);
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

}
