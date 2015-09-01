package com.alr.tasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;

public class VolumeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.volume);
        SeekBar sb = (SeekBar)findViewById(R.id.seekBar);
        sb.setMax(100);
        MyTask ts = (MyTask)getIntent().getSerializableExtra("task");
        ts.setContext(getApplicationContext());
        readTs(ts);
        CheckBox cb = (CheckBox)findViewById(R.id.vibroCheckBox);
        cb.setChecked(ts.vibration);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int result = Math.round(progress / 14) * 14;
                seekBar.setProgress(result);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void readTs(MyTask ts) {
        SeekBar sb = (SeekBar)findViewById(R.id.seekBar);
        if (ts.ring)
            sb.setProgress(ts.ring_value);
        else
            sb.setProgress(ts.notify_value);
        CheckBox cb = (CheckBox)findViewById(R.id.vibroCheckBox);
        cb.setChecked(ts.vibration);
    }

    public void volumeOkClick(View v) {
        Intent intent = new Intent();
        SeekBar sb = (SeekBar)findViewById(R.id.seekBar);
        int progress = sb.getProgress();
        int result = Math.round(progress/14)*14;
        if (result > 95)
            result = 100;
        intent.putExtra("volume", result);
        CheckBox cb = (CheckBox)findViewById(R.id.vibroCheckBox);
        intent.putExtra("vibro", cb.isChecked());
        setResult(RESULT_OK, intent);
        finish();
    }
}
