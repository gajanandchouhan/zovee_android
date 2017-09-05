package com.zoho.app.utils;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hp on 20-06-2017.
 */

public class DownloadService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private class ProgressBack extends AsyncTask<String, String, Boolean> {
        ProgressDialog PD;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            return downloadFile("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4", "Sample.mp4");
        }

        protected void onPostExecute(Boolean result) {
            PD.dismiss();
            if (result==true){
                Log.v("Dowload Success","true");
            }
            else{
                Log.v("Dowload Success","false");
            }
        }

    }


    private boolean downloadFile(String fileURL, String fileName) {
        try {
            String rootDir = Environment.getExternalStorageDirectory()
                    + File.separator + "Video";
            File rootFile = new File(rootDir);
            if (!rootFile.exists()) {
                rootFile.mkdir();
            }
            URL url = new URL(fileURL);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            FileOutputStream f = new FileOutputStream(new File(rootFile,
                    fileName));
            InputStream in = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = in.read(buffer)) > 0) {
                f.write(buffer, 0, len1);
            }
            f.close();
        } catch (IOException e) {
            Log.d("Error....", e.toString());
            return false;
        }
        return true;
    }
}
