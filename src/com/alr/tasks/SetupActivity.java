package com.alr.tasks;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import java.io.File;

public class SetupActivity extends Activity {
    private String path;
    private MyDb myDb;
    private Setup setup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        setup = new Setup(getApplicationContext());
        myDb = MyDb.getInstance(getApplicationContext());
        path = Environment.getExternalStorageDirectory().getPath() + "/backup/"+getApplicationContext().getPackageName()+"/";

        final Messages msg = new Messages(getApplicationContext());
        final CheckBox cb = (CheckBox)findViewById(R.id.shedActiveCheckBox);
        cb.setChecked(setup.active);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb.isChecked()) {
                    setup.active = cb.isChecked();
                    setup.write(getApplicationContext());
                    Toast.makeText(getApplicationContext(), "Start service", Toast.LENGTH_LONG).show();
                    msg.send(Messages.show_icon);
                    msg.send(Messages.start_msg);
                } else {
                    if (setup.confirmStop) {
                        CallBack cbYes = new CallBack() {
                            @Override
                            public void execute() {
                                Toast.makeText(getApplicationContext(), "Stop service", Toast.LENGTH_LONG).show();
                                setup.active = cb.isChecked();
                                msg.send(Messages.hide_icon);
                            }
                        };
                        CallBack cbCancel = new CallBack() {
                            @Override
                            public void execute() {
                                cb.setChecked(setup.active);
                            }
                        };
                        new MyDialogs()
                                .setTitle(getString(R.string.confirm_text))
                                .setMessage(getString(R.string.confirm_service_stop))
                            .setButtons(getString(R.string.ok), getString(R.string.cancel))
                            .setCallBack(cbYes, cbCancel, cbCancel).show(SetupActivity.this);
                    }
                }
            }
        });
        final CheckBox cb2 = (CheckBox)findViewById(R.id.confirmDelCheckBox);
        cb2.setChecked(setup.confirmDelete);
        cb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setup.confirmDelete = cb2.isChecked();
            }
        });

        final CheckBox cb3 = (CheckBox)findViewById(R.id.confirmDisCheckBox);
        cb3.setChecked(setup.confirmDisable);
        cb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setup.confirmDisable = cb3.isChecked();
            }
        });

        final CheckBox cb5 = (CheckBox)findViewById(R.id.showNotifyCheckBox);
        cb5.setChecked(setup.showNotify);
        cb5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setup.showNotify = cb5.isChecked();
            }
        });

        final CheckBox cb7 = (CheckBox)findViewById(R.id.showIconCheckBox);
        cb7.setChecked(setup.showIcon);
        cb7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setup.showIcon = cb7.isChecked();
                setup.write(getApplicationContext());
                if (setup.showIcon)
                    msg.send(Messages.show_icon);
                else
                    msg.send(Messages.hide_icon);
            }
        });

        final CheckBox cb8 = (CheckBox)findViewById(R.id.notifySoundCheckBox);
        cb8.setChecked(setup.notifySound);
        cb8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setup.notifySound = cb8.isChecked();
            }
        });

        final CheckBox cb9 = (CheckBox)findViewById(R.id.notifyWithRingCheckBox);
        cb9.setChecked(setup.notifyWithRing);
        cb9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setup.notifyWithRing = cb9.isChecked();
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        setup.write(getApplicationContext());
    }

    public void backupClick(View v) {
        if (new File(path).exists()) {
            writeBackup();
        } else {
            if (new File(path).mkdirs())
                writeBackup();
            else
                Toast.makeText(getApplicationContext(), getString(R.string.not_saved), Toast.LENGTH_LONG).show();
        }
    }

    private void writeBackup() {
        XMLio x = new XMLio(getApplicationContext(), myDb);
        x.write(path + Consts.BASKUP_FILE);
        Toast.makeText(getApplicationContext(), getString(R.string.saved), Toast.LENGTH_LONG).show();
    }

    private void loadBackup() {
        XMLio x = new XMLio(getApplicationContext(), myDb);
        x.read(path + Consts.BASKUP_FILE);
        MyDb.needUpdate = true;
        Messages msg = new Messages(getApplicationContext());
        msg.send(Messages.read_tasks);
        Toast.makeText(getApplicationContext(), getString(R.string.restored), Toast.LENGTH_LONG).show();
    }

    public void restoreClick(View v) {

        if (new File(path + Consts.BASKUP_FILE).exists()) {
            if (myDb.getTasksCount() != 0) {
                CallBack cbYes = new CallBack() {
                    @Override
                    public void execute() {
                        myDb.deleteAllTask();
                        loadBackup();
                    }
                };
                CallBack cbNo = new CallBack() {
                    @Override
                    public void execute() {
                        loadBackup();
                    }
                };
                new MyDialogs()
                        .setTitle(getString(R.string.confirm_text))
                        .setMessage(getString(R.string.confirm_delete_all))
                        .setButtons(getString(R.string.yes), getString(R.string.no))
                        .setCallBack(cbYes, cbNo, cbNo).show(SetupActivity.this);

            } else {
                loadBackup();
            }

        } else
            Toast.makeText(getApplicationContext(), getString(R.string.backup_not_found), Toast.LENGTH_LONG).show();
    }

}
