package com.alr.tasks;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;

public class BlueToothRequestActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_request);
        tryEnable();
    }

    public void tryEnable() {
        startActivityForResult( new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 0);
    }
}
