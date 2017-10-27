package com.zoho.app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zoho.app.R;
import com.zoho.app.netcom.ApiClient;
import com.zoho.app.netcom.CheckNetworkState;
import com.zoho.app.perisistance.PrefConstants;
import com.zoho.app.perisistance.PrefManager;
import com.zoho.app.utils.Utils;
import com.zoho.app.presentor.SplashPresentor;
import com.zoho.app.view.SplashView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity implements SplashView {
    private SplashPresentor splashPresentor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashPresentor = new SplashPresentor(this);
        handshake();
        splashPresentor.delaySpalsh();
        Utils.printHashKey(this);
    }

    private void handshake() {
      if (CheckNetworkState.isOnline(this)){
          Call<Object> handshake = ApiClient.getApiInterface().handshake();
          handshake.enqueue(new Callback<Object>() {
              @Override
              public void onResponse(Call<Object> call, Response<Object> response) {
                  Log.v("Splash", response.body().toString());
              }

              @Override
              public void onFailure(Call call, Throwable t) {
                 t.printStackTrace();
              }
          });
      }
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
