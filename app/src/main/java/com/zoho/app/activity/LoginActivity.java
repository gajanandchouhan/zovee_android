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
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.zoho.app.R;
import com.zoho.app.custom.CustomProgressDialog;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, RegisterView {


    private static final int RC_SIGN_IN = 111;
    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private RegisterPresentor presentor;
    private CustomProgressDialog progressDialog;
    EditText emailEditText, passwordEditText;
    private TextView buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindViews();
        presentor = new RegisterPresentor(this, this);
        buttonLogin = (TextView) findViewById(R.id.btn_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            //    .requestIdToken("625831639845-2v4uc03o9d98k9bld9m6rdhefoepa4gj.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        findViewById(R.id.btn_facebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();
                }
                loginWithFb();
            }
        });


        findViewById(R.id.btn_google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void loginWithFb() {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Log.v("FACEBOOK", "Suceess" + loginResult.getAccessToken().getToken());
                        setFacebookData(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.v("FACEBOOK", "Cancel");
                    }

                    @Override
                    public void onError(FacebookException exc) {
                        // App code
                        Log.v("ERROR", "ERROR" + exc.getMessage());
                    }
                });
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.v("Google", acct.getDisplayName());

            String fullame = acct.getDisplayName();
            String email = acct.getEmail();
            String id = acct.getId();
            String[] parts = fullame.split("\\s+");
            Log.d("Length-->", "" + parts.length);
            String firstname = "";
            String lastname = "";
            if (parts.length == 2) {
                firstname = parts[0];
                lastname = parts[1];
                Log.d("First-->", "" + firstname);
                Log.d("Last-->", "" + lastname);

            } else if (parts.length == 3) {
                firstname = parts[0];
                String middlename = parts[1];
                lastname = parts[2];
            }
            doSoicialSignup(firstname, lastname, email, id, "3", PrefManager.getInstance(LoginActivity.this).getString(PrefConstants.DEVICE_TOKEN));
        }

    }

    private void doSoicialSignup(String firstname, String lastname, String email, String socialId, String type, String deviceToken) {
        if (!CheckNetworkState.isOnline(this)) {
            Utils.showToast(this, getString(R.string.no_internet));
            return;
        }
        presentor.register(firstname, lastname, "", email, null, "",
                PrefManager.getInstance(this).getString(PrefConstants.DEVICE_TOKEN), socialId, type);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void setFacebookData(final LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        try {
                            Log.i("Response", response.toString());

                            String email = response.getJSONObject().getString("email");
                            String firstName = response.getJSONObject().getString("first_name");
                            String lastName = response.getJSONObject().getString("last_name");
                            String id = response.getJSONObject().getString("id");
                            doSoicialSignup(firstName, lastName, email, id, "2", PrefManager.getInstance(LoginActivity.this).getString(PrefConstants.DEVICE_TOKEN));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }


    private void bindViews() {
        passwordEditText = (EditText) findViewById(R.id.et_pass);
        emailEditText = (EditText) findViewById(R.id.et_email);
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
        }
        if (!Utils.isValidEmail(email)) {
            emailEditText.setError(getString(R.string.invalid_email));
            return;
        }
        if (password.length() == 0) {
            passwordEditText.setError(getString(R.string.password_empty));
            return;
        }
        enableDisableView(true);
        progressDialog = new CustomProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.show();
        //  startActivity(new Intent(this,MainActivity.class));
        LoginRequestModel model = new LoginRequestModel();
        model.setEmail(email);
        model.setPassword(password);
        Call<LoginResponseModel> loginResponseModelCall = ApiClient.getApiInterface().doLogin(model);
        loginResponseModelCall.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                dismisProgress();
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
                        PrefManager.getInstance(LoginActivity.this).putString(PrefConstants.LOGIN_TYPE, "1");
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
                dismisProgress();
                Utils.showToast(LoginActivity.this, getString(R.string.server_error));
            }
        });
    }

    private void dismisProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
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

    public void onSkip(View view) {
        startActivity(new Intent(this, MainActivity.class));
        ActivityCompat.finishAffinity(this);
        finish();
    }

    public void onNewUser(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    @Override
    public void showProgress() {
        progressDialog = new CustomProgressDialog(this);
        progressDialog.show();
    }

    @Override
    public void onRegister() {
        Utils.startActivity(this, MainActivity.class, null,true);
        finish();
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
