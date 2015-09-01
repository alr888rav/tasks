package com.alr.tasks;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        String versionName = "";
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Exception e) {
            Logger.d(Consts.LOG, e.getMessage());
        }
        TextView v = (TextView)findViewById(R.id.versionText);
        v.setText(getString(R.string.version)+" "+versionName);

    }
}
