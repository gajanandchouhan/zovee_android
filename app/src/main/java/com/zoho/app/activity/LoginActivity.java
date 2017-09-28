package com.zoho.app.activity;

import android.Manifest;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.zoho.app.R;
import com.zoho.app.netcom.CheckNetworkState;
import com.zoho.app.presentor.RegisterPresentor;
import com.zoho.app.utils.ConstantLib;
import com.zoho.app.utils.Utils;
import com.zoho.app.view.RegisterView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class LoginActivity extends AppCompatActivity {
    ProgressBar progressBar;
    EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindViews();
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void bindViews() {
        passwordEditText = (EditText) findViewById(R.id.et_pass);
        emailEditText = (EditText) findViewById(R.id.et_email);
        progressBar = (ProgressBar) findViewById(R.id.loading);
    }

    public void login() {
        if (!CheckNetworkState.isOnline(this)) {
            Utils.showToast(this, getString(R.string.no_internet));
            return;
        }
        String password = passwordEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        if (email.length() == 0) {
            emailEditText.setError(getString(R.string.email_empty));
            return;
        } else if (password.length() == 0) {
            passwordEditText.setError(getString(R.string.password_empty));
            return;
        }
        //enableDisableView(true);
          startActivity(new Intent(this,MainActivity.class));
    }

    private void enableDisableView(boolean disable) {
        if (disable) {
            emailEditText.setEnabled(false);
            passwordEditText.setEnabled(false);
        } else {
            emailEditText.setEnabled(true);
            passwordEditText.setEnabled(true);
        }
    }

    public void onForgot(View view) {

    }

    public void onNewUser(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
