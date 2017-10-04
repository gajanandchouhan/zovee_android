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
import android.widget.RelativeLayout;

import com.zoho.app.R;
import com.zoho.app.model.request.LoginRequestModel;
import com.zoho.app.model.response.LoginResponseData;
import com.zoho.app.model.response.LoginResponseModel;
import com.zoho.app.netcom.ApiClient;
import com.zoho.app.netcom.CheckNetworkState;
import com.zoho.app.perisistance.PrefConstants;
import com.zoho.app.perisistance.PrefManager;
import com.zoho.app.presentor.RegisterPresentor;
import com.zoho.app.utils.ConstantLib;
import com.zoho.app.utils.Utils;
import com.zoho.app.view.RegisterView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    ProgressBar progressBar;
    EditText emailEditText, passwordEditText;
    private RelativeLayout buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindViews();
        buttonLogin = (RelativeLayout) findViewById(R.id.btn_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
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
        enableDisableView(true);
        progressBar.setVisibility(View.VISIBLE);
        //  startActivity(new Intent(this,MainActivity.class));
        LoginRequestModel model = new LoginRequestModel();
        model.setEmail(email);
        model.setPassword(password);
        Call<LoginResponseModel> loginResponseModelCall = ApiClient.getApiInterface().doLogin(model);
        loginResponseModelCall.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                progressBar.setVisibility(View.GONE);
                enableDisableView(false);
                if (response.body() != null) {
                    LoginResponseModel body1 = response.body();
                    if (body1.getResponseCode().equalsIgnoreCase(ConstantLib.RESPONSE_SUCCESS)) {
                        LoginResponseData responseData = body1.getResponseData();
                        PrefManager.getInstance(LoginActivity.this).putString(PrefConstants.NAME, responseData.getFirstName());
                        PrefManager.getInstance(LoginActivity.this).putString(PrefConstants.LASTNAME, responseData.getLastName());
                        PrefManager.getInstance(LoginActivity.this).putString(PrefConstants.COMPANY, responseData.getCompanyName());
                        PrefManager.getInstance(LoginActivity.this).putString(PrefConstants.EMAIL, responseData.getEmail());
                        PrefManager.getInstance(LoginActivity.this).putString(PrefConstants.IMAGE, responseData.getImageUrl());
                        PrefManager.getInstance(LoginActivity.this).putInt(PrefConstants.U_ID, responseData.getUserId());
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        ActivityCompat.finishAffinity(LoginActivity.this);
                        finish();

                    } else {
                        Utils.showToast(LoginActivity.this, body1.getResponseMessage());
                    }
                } else {
                    Utils.showToast(LoginActivity.this, getString(R.string.server_error));
                }

            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                t.printStackTrace();
                enableDisableView(false);
                progressBar.setVisibility(View.GONE);
                Utils.showToast(LoginActivity.this, getString(R.string.server_error));
            }
        });
    }

    private void enableDisableView(boolean disable) {
        if (disable) {
            emailEditText.setEnabled(false);
            passwordEditText.setEnabled(false);
            buttonLogin.setEnabled(false);
        } else {
            emailEditText.setEnabled(true);
            passwordEditText.setEnabled(true);
            buttonLogin.setEnabled(true);
        }
    }

    public void onForgot(View view) {
        startActivity(new Intent(this, ForgotPasswordActivity.class));
    }

    public void onNewUser(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
