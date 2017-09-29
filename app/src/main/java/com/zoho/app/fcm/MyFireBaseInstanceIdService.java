package com.zoho.app.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.zoho.app.perisistance.PrefConstants;
import com.zoho.app.perisistance.PrefManager;

/**
 * Created by hp on 29-09-2017.
 */

public class MyFireBaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "TOKEN";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        PrefManager.getInstance(this).putString(PrefConstants.DEVICE_TOKEN,refreshedToken);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken);
    }
}
