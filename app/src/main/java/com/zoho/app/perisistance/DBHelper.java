package com.zoho.app.perisistance;

import android.content.Context;


import com.zoho.app.fcm.DaoMaster;
import com.zoho.app.fcm.DaoSession;
import com.zoho.app.fcm.NotificationDataModel;
import com.zoho.app.model.response.SubCategoryModel;

import org.greenrobot.greendao.database.Database;

import java.util.List;

/**
 * Created by gajanandchouhan on 25/5/17.
 */

public class DBHelper {
    private static DBHelper instance;
    private static DaoSession daoSession;

    public DBHelper(Context mContext) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(mContext, "zovee-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public static DBHelper getInstance(Context mContext) {
        if (instance == null) {
            instance = new DBHelper(mContext.getApplicationContext());
        }
        return instance;
    }

    public void insertNotificationToDb(NotificationDataModel notificationDataModel) {
        daoSession.getNotificationDataModelDao().insert(notificationDataModel);

    }

    public List<NotificationDataModel> getNotificationData() {
        return daoSession.getNotificationDataModelDao().loadAll();
    }


    public void clearNotifications() {
        daoSession.getNotificationDataModelDao().deleteAll();
    }
}
