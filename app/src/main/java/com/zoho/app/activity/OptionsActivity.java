package com.zoho.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
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
import com.zoho.app.netcom.CheckNetworkState;
import com.zoho.app.perisistance.PrefConstants;
import com.zoho.app.perisistance.PrefManager;
import com.zoho.app.presentor.RegisterPresentor;
import com.zoho.app.utils.Utils;
import com.zoho.app.view.RegisterView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class OptionsActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, RegisterView {


    private static final int RC_SIGN_IN = 111;
    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private RegisterPresentor presentor;
    private CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        presentor = new RegisterPresentor(this, this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        findViewById(R.id.btn_skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OptionsActivity.this, MainActivity.class));
                finish();
            }
        });

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

        findViewById(R.id.btn_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OptionsActivity.this, LoginActivity.class));
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
            doSoicialSignup(firstname, lastname, email, id, "3", PrefManager.getInstance(OptionsActivity.this).getString(PrefConstants.DEVICE_TOKEN));
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
                            doSoicialSignup(firstName, lastName, email, id, "2", PrefManager.getInstance(OptionsActivity.this).getString(PrefConstants.DEVICE_TOKEN));

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

    @Override
    public void showProgress() {
        progressDialog = new CustomProgressDialog(this);
        progressDialog.show();
    }

    @Override
    public void onRegister() {
        Utils.startActivity(this, MainActivity.class, null);
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}


