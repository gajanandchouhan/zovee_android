package com.zoho.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zoho.app.R;
import com.zoho.app.perisistance.PrefConstants;
import com.zoho.app.perisistance.PrefManager;
import com.zoho.app.presentor.SplashPresentor;
import com.zoho.app.utils.Utils;
import com.zoho.app.view.SplashView;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        findViewById(R.id.textView_skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, MainActivity.class));
                finish();
            }
        });


        findViewById(R.id.textView_signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, LoginActivity.class));;
            }
        });
    }


}
