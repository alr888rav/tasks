package com.alr.tasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

public class OnOffActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onoff);
        MyTask ts = (MyTask)getIntent().getSerializableExtra("task");
        ts.setContext(getApplicationContext());
        readTs(ts);
    }
    private void readTs(MyTask ts) {
        ToggleButton tb = (ToggleButton)findViewById(R.id.toggleButton);
        tb.setChecked(ts.onOff);
    }
    public void onoffOkClick(View v) {
        Intent intent = new Intent();
        ToggleButton tb = (ToggleButton)findViewById(R.id.toggleButton);
        intent.putExtra("onoff", tb.isChecked());
        setResult(RESULT_OK, intent);
        finish();
    }

}
