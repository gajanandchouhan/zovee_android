package com.zoho.app.activity;

import android.media.audiofx.PresetReverb;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zoho.app.R;
import com.zoho.app.custom.CustomProgressDialog;
import com.zoho.app.model.request.ChangePasswordRequestModel;
import com.zoho.app.model.response.BaseResponseModel;
import com.zoho.app.model.response.VideoListModel;
import com.zoho.app.netcom.ApiClient;
import com.zoho.app.netcom.CheckNetworkState;
import com.zoho.app.perisistance.PrefConstants;
import com.zoho.app.perisistance.PrefManager;
import com.zoho.app.utils.ConstantLib;
import com.zoho.app.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hp on 02-10-2017.
 */

public class ChangePasswordActivity extends AppCompatActivity {
    private TextView titleTextView;
    private ImageView backImageView;
    private Button buttonChangePassword;
    private EditText editTextOldPass, editTextNewPass, editTextConformNewPass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        backImageView = (ImageView) findViewById(R.id.back_imageView);
        titleTextView = (TextView) findViewById(R.id.textView_title);
        editTextOldPass = (EditText) findViewById(R.id.et_pass_old);
        editTextNewPass = (EditText) findViewById(R.id.et_pass_new);
        editTextConformNewPass = (EditText) findViewById(R.id.et_pass_confirm);
        buttonChangePassword = (Button) findViewById(R.id.button_change_password);
        backImageView.setVisibility(View.VISIBLE);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView.setText(getString(R.string.change_password));
        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        if (!CheckNetworkState.isOnline(this)) {
            Utils.showToast(this, getString(R.string.no_internet));
            return;
        }
        String oldPass = editTextOldPass.getText().toString().trim();
        String newPass = editTextNewPass.getText().toString().trim();
        String confirmNewPass = editTextConformNewPass.getText().toString().trim();
        if (oldPass.length() == 0) {
            editTextOldPass.setError(getString(R.string.enter_old_pass));
            return;
        }
        if (newPass.length() == 0) {
            editTextNewPass.setError(getString(R.string.enter_new_pass));
            return;
        }
        if (confirmNewPass.length() == 0) {
            editTextConformNewPass.setError(getString(R.string.enter_confirm_new_pass));
            return;
        }
        if (!(newPass.equalsIgnoreCase(confirmNewPass))) {
            editTextConformNewPass.setError(getString(R.string.password_not_match));
            return;
        }
        final CustomProgressDialog dialog = new CustomProgressDialog(this);
        dialog.setCancelable(false);
        dialog.show();
        ChangePasswordRequestModel changePasswordRequestModel = new ChangePasswordRequestModel();
        changePasswordRequestModel.setUserId(PrefManager.getInstance(this).getInt(PrefConstants.U_ID));
        changePasswordRequestModel.setCurrentPassword(oldPass);
        changePasswordRequestModel.setNewPassword(newPass);
        Call<BaseResponseModel> baseResponseModelCall = ApiClient.getApiInterface().changePassword(changePasswordRequestModel);
        baseResponseModelCall.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                dialog.dismiss();
                if (response.body() != null) {
                    if (response.body().getResponseCode().equalsIgnoreCase(ConstantLib.RESPONSE_SUCCESS)) {
                        finish();
                    }
                    Utils.showToast(ChangePasswordActivity.this, response.body().getResponseMessage());
                } else {

                }
                Utils.showToast(ChangePasswordActivity.this, getString(R.string.server_error));
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                dialog.dismiss();
                Utils.showToast(ChangePasswordActivity.this, getString(R.string.server_error));
            }
        });

    }
}
