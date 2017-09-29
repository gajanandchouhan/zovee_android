package com.zoho.app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.zoho.app.R;
import com.zoho.app.netcom.CheckNetworkState;
import com.zoho.app.utils.Utils;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText emailEditText;
    private ProgressBar progressBar;
    private ImageView backImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        bindViews();

        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doForgotPassword();
            }
        });
    }

    private void bindViews() {
        emailEditText = (EditText) findViewById(R.id.et_email);
        progressBar = (ProgressBar) findViewById(R.id.loading);

        backImageView = (ImageView) findViewById(R.id.back_imageView);
        backImageView.setVisibility(View.VISIBLE);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void doForgotPassword() {
        if (!CheckNetworkState.isOnline(this)) {
            Utils.showToast(this, getString(R.string.no_internet));
            return;
        }
        String email = emailEditText.getText().toString().trim();
        if (email.length() == 0) {
            emailEditText.setError(getString(R.string.email_empty));
            return;
        } else if (!Utils.isValidEmail(email)) {
            emailEditText.setError(getString(R.string.invalid_email));
            return;
        }
        enableDisableView(true);
    }

    private void enableDisableView(boolean disable) {
        if (disable) {
            emailEditText.setEnabled(false);
        } else {
            emailEditText.setEnabled(true);
        }
    }


}
