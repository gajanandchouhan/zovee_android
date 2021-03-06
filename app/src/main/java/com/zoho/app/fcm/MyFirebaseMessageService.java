package com.zoho.app.fcm;

import android.app.ActivityManager;
import android.app.NotificationChannel;
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
import com.zoho.app.activity.NotificationActivity;
import com.zoho.app.perisistance.DBHelper;
import com.zoho.app.perisistance.PrefConstants;
import com.zoho.app.perisistance.PrefManager;

import java.util.Map;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

/**
 * Created by hp on 29-09-2017.
 */

public class MyFirebaseMessageService extends FirebaseMessagingService {

    private static final String TAG = "FCMMESSAGE";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (PrefManager.getInstance(this).getInt(PrefConstants.U_ID) != 0) {
            if (!foregrounded()) {
                Log.d(TAG, "From: " + remoteMessage.getFrom());
                Log.d(TAG, "Notification Message Body: " + remoteMessage.getData().toString());
                //Calling method to generate notification
                sendNotification(remoteMessage.getData());
            }        //It is optional
        }

    }

    private void sendNotification(Map<String, String> messageBody) {
        String title = messageBody.get("title");
        String message = messageBody.get("message");
        int subCategoryId = Integer.parseInt(messageBody.get("subCategoryId"));
        String subCategoryName = messageBody.get("subCategoryName");
        String subCategoryDescription = messageBody.get("subCategoryDescription");
        String subCategoryThumbUrl = messageBody.get("subCategoryThumbURL");
        NotificationDataModel model = new NotificationDataModel();
        model.setTitle(title);
        model.setMessage(message);
        model.setSubCategoryId(subCategoryId);
        model.setSubCategoryDescription(subCategoryDescription);
        model.setSubcategoryName(subCategoryName);
        model.setSubCategoryThumbUrl(subCategoryThumbUrl);
        DBHelper.getInstance(this).insertNotificationToDb(model);
        NotificationActivity.FROM_NOTFICATION = true;
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"zovee_channel_01");
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(getColor(R.color.text_color1));
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        }
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(message);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

      /* Create or update. */
            NotificationChannel channel = new NotificationChannel("zovee_channel_01",
                    "Video",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notificationBuilder.build());
    }

    public boolean foregrounded() {
        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);
        return (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE);
    }
}
