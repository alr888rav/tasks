package com.alr.tasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class ServicesActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services);
        MyTask ts = (MyTask)getIntent().getSerializableExtra("task");
        ts.setContext(getApplicationContext());
        readTs(ts);
    }
    public void checkServices(View v) {
        clearCheck();
        ((RadioButton)v).setChecked(true);
    }
    private void clearCheck() {
        RadioButton cb;
        cb = (RadioButton) findViewById(R.id.mobileRadio);
        cb.setChecked(false);
        cb = (RadioButton) findViewById(R.id.btRadio);
        cb.setChecked(false);
        cb = (RadioButton) findViewById(R.id.wiFiRadio);
        cb.setChecked(false);
        cb = (RadioButton) findViewById(R.id.ringValRadio);
        cb.setChecked(false);
        cb = (RadioButton) findViewById(R.id.notifyValRadio);
        cb.setChecked(false);

    }
    private void readTs(MyTask ts) {
        RadioButton cb;
        clearCheck();
        if (ts.mobile) {
            cb = (RadioButton) findViewById(R.id.mobileRadio);
            cb.setChecked(ts.mobile);
        } else if (ts.bluetooth) {
            cb = (RadioButton) findViewById(R.id.btRadio);
            cb.setChecked(ts.bluetooth);
        } else if (ts.wifi) {
            cb = (RadioButton) findViewById(R.id.wiFiRadio);
            cb.setChecked(ts.wifi);
        } else if (ts.ring) {
            cb = (RadioButton) findViewById(R.id.ringValRadio);
            cb.setChecked(ts.ring);
        } else if (ts.notify) {
            cb = (RadioButton) findViewById(R.id.notifyValRadio);
            cb.setChecked(ts.notify);
        }
    }
    public void okButtonClick(View v) {
        Intent intent = new Intent();
        RadioButton cb;
        cb = (RadioButton)findViewById(R.id.mobileRadio);
        intent.putExtra("mobile", cb.isChecked());
        cb = (RadioButton)findViewById(R.id.btRadio);
        intent.putExtra("bluetooth", cb.isChecked());
        cb = (RadioButton)findViewById(R.id.wiFiRadio);
        intent.putExtra("wifi", cb.isChecked());
        cb = (RadioButton)findViewById(R.id.ringValRadio);
        intent.putExtra("ring", cb.isChecked());
        cb = (RadioButton)findViewById(R.id.notifyValRadio);
        intent.putExtra("notify", cb.isChecked());
        setResult(RESULT_OK, intent);
        finish();
    }
}
