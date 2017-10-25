package com.zoho.app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zoho.app.R;
import com.zoho.app.perisistance.PrefConstants;
import com.zoho.app.perisistance.PrefManager;
import com.zoho.app.utils.Utils;
import com.zoho.app.presentor.SplashPresentor;
import com.zoho.app.view.SplashView;

public class SplashActivity extends AppCompatActivity implements SplashView {
    private SplashPresentor splashPresentor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashPresentor = new SplashPresentor(this);
        splashPresentor.delaySpalsh();
        Utils.printHashKey(this);
    }

    @Override
    public void navigateToNextActivty() {
        if (PrefManager.getInstance(this).getInt(PrefConstants.U_ID) == 0) {
            Utils.startActivity(this, StartActivity.class, null);
        } else {
            Utils.startActivity(this, MainActivity.class, null);
        }
        finish();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        splashPresentor.cancelSplash();
        finish();
    }
}
