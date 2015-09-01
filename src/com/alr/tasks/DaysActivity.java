package com.alr.tasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class DaysActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.days);
        MyTask ts = (MyTask)getIntent().getSerializableExtra("task");
        ts.setContext(getApplicationContext());
        readTs(ts);
    }
    private void readTs(MyTask ts) {
        CheckBox cb;
        cb = (CheckBox)findViewById(R.id.mondayCheckBox);
        cb.setChecked(ts.day1);
        cb = (CheckBox)findViewById(R.id.tuesdayCheckBox);
        cb.setChecked(ts.day2);
        cb = (CheckBox)findViewById(R.id.wednesdayCheckBox);
        cb.setChecked(ts.day3);
        cb = (CheckBox)findViewById(R.id.thursdayCheckBox);
        cb.setChecked(ts.day4);
        cb = (CheckBox)findViewById(R.id.fridayCheckBox);
        cb.setChecked(ts.day5);
        cb = (CheckBox)findViewById(R.id.saturdayCheckBox);
        cb.setChecked(ts.day6);
        cb = (CheckBox)findViewById(R.id.sundayCheckBox);
        cb.setChecked(ts.day7);
    }
    public void daysOkButtonClick(View v) {
        Intent intent = new Intent();
        CheckBox cb;
        cb = (CheckBox)findViewById(R.id.mondayCheckBox);
        intent.putExtra("monday", cb.isChecked());
        cb = (CheckBox)findViewById(R.id.tuesdayCheckBox);
        intent.putExtra("tuesday", cb.isChecked());
        cb = (CheckBox)findViewById(R.id.wednesdayCheckBox);
        intent.putExtra("wednesday", cb.isChecked());
        cb = (CheckBox)findViewById(R.id.thursdayCheckBox);
        intent.putExtra("thursday", cb.isChecked());
        cb = (CheckBox)findViewById(R.id.fridayCheckBox);
        intent.putExtra("friday", cb.isChecked());
        cb = (CheckBox)findViewById(R.id.saturdayCheckBox);
        intent.putExtra("saturday", cb.isChecked());
        cb = (CheckBox)findViewById(R.id.sundayCheckBox);
        intent.putExtra("sunday", cb.isChecked());
        setResult(RESULT_OK, intent);
        finish();
    }
}
