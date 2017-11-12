package com.zoho.app.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.zoho.app.R;
import com.zoho.app.activity.MainActivity;
import com.zoho.app.perisistance.PrefConstants;
import com.zoho.app.perisistance.PrefManager;
import com.zoho.app.utils.ConstantLib;

/**
 * Created by hp on 29-09-2017.
 */

public class MyFirebaseMessageService extends FirebaseMessagingService {

    private static final String TAG = "FCMMESSAGE";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (PrefManager.getInstance(this).getInt(PrefConstants.U_ID) != 0) {
            Log.d(TAG, "From: " + remoteMessage.getFrom());
            Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

            //Calling method to generate notification
            sendNotification(remoteMessage.getNotification().getBody());
        }        //It is optional

    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(getColor(R.color.text_color1));
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        }
        notificationBuilder.setContentTitle("Firebase Push Notification");
        notificationBuilder.setContentText(messageBody);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
