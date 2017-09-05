package com.zoho.app;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;

/**
 * Created by hp on 26-05-2017.
 */

public class ZohoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this, "ca-app-pub-7077554208908443~6111070604");
    }
}
