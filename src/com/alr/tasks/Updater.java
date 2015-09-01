package com.alr.tasks;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class Updater {
    private final String tmpName = "app_tmp.apk";
    private String path;
    private Context context;

    Updater(Context context) {
        this.context = context;
        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        startDownload();
    }

    private void startDownload() {
        String url = "https://www.dropbox.com/s/h91r8v8nowksbma/tasks.apk?dl=1";
        new DownloadFileAsync().execute(url);
    }

    private void dialog(int progress) {
        new Notifi(context).show(context.getString(R.string.update), context.getString(R.string.downloads)+": "+progress+"%", false, false);
    }

    private void install() {
        new Notifi(context).show(context.getString(R.string.update), context.getString(R.string.install), true, false);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (new File(path + tmpName).exists() && new File(path + tmpName).length() > 0) {
            intent.setDataAndType(Uri.fromFile(new File(path + tmpName)), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    class DownloadFileAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog(0);
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;

            try {

                URL url = new URL(aurl[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                int lenghtOfFile = connection.getContentLength();
                Log.d(Consts.LOG, "file size: " + lenghtOfFile);
                if (lenghtOfFile > 0) {
                    InputStream input = new BufferedInputStream(url.openStream());
                    OutputStream output = new FileOutputStream(path + tmpName);

                    byte data[] = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                        output.write(data, 0, count);
                    }

                    output.flush();
                    output.close();
                    input.close();
                }
            } catch (Exception e) {
                Logger.d(Consts.LOG, "update "+e.getMessage());
            }
            return null;

        }
        protected void onProgressUpdate(String... progress) {
            Log.d(Consts.LOG, progress[0]);
            dialog(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            install();
        }
    }
}
