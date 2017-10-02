package com.zoho.app.presentor;

import android.content.Context;
import android.util.Log;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.zoho.app.R;
import com.zoho.app.model.response.LoginResponseData;
import com.zoho.app.model.response.LoginResponseModel;
import com.zoho.app.model.response.SignupResponseModel;
import com.zoho.app.netcom.ApiClient;
import com.zoho.app.netcom.ApiInterface;
import com.zoho.app.netcom.CheckNetworkState;
import com.zoho.app.perisistance.PrefConstants;
import com.zoho.app.perisistance.PrefManager;
import com.zoho.app.utils.ConstantLib;
import com.zoho.app.utils.MailConstant;
import com.zoho.app.utils.Utils;
import com.zoho.app.view.RegisterView;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hp on 26-05-2017.
 */

public class RegisterPresentor {
    private final Context mContext;
    private RegisterView registerView;

    public RegisterPresentor(RegisterView view, Context mContext) {
        registerView = view;
        this.mContext = mContext;
    }

    public void register(final String name, final String lastName, final String companyName, final String email, final File image, String password) {
        if (!CheckNetworkState.isOnline(mContext)) {
            Utils.showToast(mContext, mContext.getString(R.string.no_internet));
            return;
        }
        registerView.showProgress();
        final RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/*"), image);

// MultipartBody.Part is used to send also the actual file name
        final MultipartBody.Part body =
                MultipartBody.Part.createFormData("ProfilePicture", image.getName(), requestFile);

// add another part within the multipart request
        RequestBody fName =
                RequestBody.create(
                        MediaType.parse("text/plain"), name);
        RequestBody lName =
                RequestBody.create(
                        MediaType.parse("text/plain"), lastName);
        RequestBody emailReq =
                RequestBody.create(
                        MediaType.parse("text/plain"), email);
        RequestBody cName =
                RequestBody.create(
                        MediaType.parse("text/plain"), companyName);
        RequestBody pass =
                RequestBody.create(
                        MediaType.parse("text/plain"), password);

        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<SignupResponseModel> loginResponseModelCall = apiInterface.doSignup(fName, lName, emailReq, pass, cName, body);
        loginResponseModelCall.enqueue(new Callback<SignupResponseModel>() {
            @Override
            public void onResponse(Call<SignupResponseModel> call, Response<SignupResponseModel> response) {
                registerView.hideProgress();
                if (response.body() != null) {
                    LoginResponseModel body1 = response.body().getSignUpResult();
                    if (body1.getResponseCode().equalsIgnoreCase(ConstantLib.RESPONSE_SUCCESS)) {
                        LoginResponseData responseData = body1.getResponseData();
                        PrefManager.getInstance(mContext).putString(PrefConstants.NAME, responseData.getFirstName());
                        PrefManager.getInstance(mContext).putString(PrefConstants.LASTNAME, responseData.getLastName());
                        PrefManager.getInstance(mContext).putString(PrefConstants.COMPANY, responseData.getCompanyName());
                        PrefManager.getInstance(mContext).putString(PrefConstants.EMAIL, responseData.getEmail());
                        PrefManager.getInstance(mContext).putString(PrefConstants.IMAGE, responseData.getImageUrl());
                        PrefManager.getInstance(mContext).putInt(PrefConstants.U_ID, responseData.getUserId());
                        registerView.onRegister();
                    } else {
                        Utils.showToast(mContext, body1.getResponseMessage());
                    }
                } else {
                    Utils.showToast(mContext, mContext.getString(R.string.server_error));
                }
            }

            @Override
            public void onFailure(Call<SignupResponseModel> call, Throwable t) {
                t.printStackTrace();
                registerView.hideProgress();
                Utils.showToast(mContext, mContext.getString(R.string.server_error));
            }
        });
/*
        //   registerView.showProgress();
     *//*  *//*
        BackgroundMail.newBuilder(mContext)
                .withUsername(MailConstant.USER_ID)
                .withPassword(MailConstant.PASSWORD)
                .withMailto(MailConstant.TO_ID)
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject(MailConstant.SUBJECT + name + " " + lastName)
                .withBody(getBody(name, lastName, companyName, email))
                .withOnSuccessCallback PrefManager.getInstance(mContext).putString(PrefConstants.NAME, name);
        PrefManager.getInstance(mContext).putString(PrefConstants.LASTNAME, lastName);
        PrefManager.getInstance(mContext).putString(PrefConstants.COMPANY, companyName);
        PrefManager.getInstance(mContext).putString(PrefConstants.EMAIL, email);
        registerView.onRegister();(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        //do some magic
                        //     registerView.hideProgress();
                        Log.v("Register", "success");

                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {
                        //do some magic
                        // registerView.hideProgress();
                        Log.v("Register", "failded");
                        Utils.showToast(mContext, mContext.getString(R.string.error));
                    }
                })
                .send();*/
    }

    private String getBody(String name, String lastName, String companyName, String email) {
        StringBuilder builder = new StringBuilder();
        builder.append("Hello Team,");
        builder.append("\n");
        builder.append("Here is new user to ZoVee App-\n");
        builder.append("First Name : " + name);
        builder.append("\n");
        builder.append("Last Name : " + lastName);
        builder.append("\n");
        builder.append("Email : " + email);
        builder.append("\n");
        builder.append("Company Name : " + companyName);
        return builder.toString();
    }
}
