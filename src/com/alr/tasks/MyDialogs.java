package com.alr.tasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;


public class MyDialogs extends Activity{
    private String title = "";
    private String message = "";
    private CallBack yesCallBack = null;
    private CallBack noCallBack = null;
    private CallBack cancelCallBack = null;
    private String button1String = "Ok";
    private String button2String = "Cancel";

/*  sample usage
    public void testDialog() {
        CallBack cb1 = new CallBack() {
            @Override
            public void execute() {
                rz[0] = 1;
            }
        };
        CallBack cb2 = new CallBack() {
            @Override
            public void execute() {
                rz[0] = 2;
            }
        };
        MyDialogs d1 = new MyDialogs("title", "msg", "Yes", "No", cb1, cb2, null);
        d1.show(getApplicationContext());
        //
        new MyDialogs()
            .setTitle("title")
            .setMessage("message")
            .setButtons("yes", "no")
            .setCallBack(cb1, cb2, null)
            .show(getApplicationContext());

    }
*/

    public MyDialogs setTitle(String title) {
        this.title = title;
        return this;
    }

    public MyDialogs setMessage(String message) {
        this.message = message;
        return this;
    }

    public MyDialogs setButtons(String button1String, String button2String) {
        this.button1String = button1String;
        this.button2String = button2String;
        return this;
    }

    public MyDialogs setCallBack(CallBack yesCallBack, CallBack noCallBack, CallBack cancelCallback) {
        this.yesCallBack = yesCallBack;
        this.noCallBack = noCallBack;
        this.cancelCallBack = cancelCallback;
        return this;
    }

    // default parameters
    MyDialogs() {
    }

    public void show(final Context context) {
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setTitle(title);
        ad.setMessage(message);
        ad.setPositiveButton(button1String, new OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                if (yesCallBack != null)
                    yesCallBack.execute();
            }
        });
        ad.setNegativeButton(button2String, new OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                if (noCallBack != null)
                    noCallBack.execute();
            }
        });
        ad.setCancelable(true);
        ad.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                if (cancelCallBack != null)
                    cancelCallBack.execute();
            }
        });
        ad.create();
        ad.show();
    }
}
