package com.zoho.app.presentor;

import android.os.Handler;

import com.zoho.app.view.SplashView;

/**
 * Created by hp on 26-05-2017.
 */

public class SplashPresentor {
    private SplashView splashView;
    private Handler mHandler;
    private static final int SPLASH_TIME_OUT = 1500;

    public SplashPresentor(SplashView view) {
        splashView = view;
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            splashView.navigateToNextActivty();
        }
    };

    public void delaySpalsh() {
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, SPLASH_TIME_OUT);
    }

    public void cancelSplash() {
        mHandler.removeCallbacks(mRunnable);
    }
}
