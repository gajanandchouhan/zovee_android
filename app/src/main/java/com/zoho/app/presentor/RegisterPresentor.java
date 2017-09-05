package com.zoho.app.presentor;

import android.content.Context;
import android.util.Log;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.zoho.app.R;
import com.zoho.app.netcom.CheckNetworkState;
import com.zoho.app.perisistance.PrefConstants;
import com.zoho.app.perisistance.PrefManager;
import com.zoho.app.utils.MailConstant;
import com.zoho.app.utils.Utils;
import com.zoho.app.view.RegisterView;

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

    public void registerBySendingMail(final String name, final String lastName, final String companyName, final String email) {
        if (!CheckNetworkState.isOnline(mContext)) {
            Utils.showToast(mContext, mContext.getString(R.string.no_internet));
            return;
        }
        //   registerView.showProgress();
        PrefManager.getInstance(mContext).putString(PrefConstants.NAME, name);
        PrefManager.getInstance(mContext).putString(PrefConstants.LASTNAME, lastName);
        PrefManager.getInstance(mContext).putString(PrefConstants.COMPANY, companyName);
        PrefManager.getInstance(mContext).putString(PrefConstants.EMAIL, email);
        registerView.onRegister();
        BackgroundMail.newBuilder(mContext)
                .withUsername(MailConstant.USER_ID)
                .withPassword(MailConstant.PASSWORD)
                .withMailto(MailConstant.TO_ID)
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject(MailConstant.SUBJECT + name + " " + lastName)
                .withBody(getBody(name, lastName, companyName, email))
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
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
                .send();
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
