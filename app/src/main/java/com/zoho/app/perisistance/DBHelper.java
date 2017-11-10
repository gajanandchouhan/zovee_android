package com.zoho.app.perisistance;

import android.content.Context;


import com.zoho.app.model.response.DaoMaster;
import com.zoho.app.model.response.DaoSession;
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

    public void insertOrReplaceSubCaetgoryModel(SubCategoryModel subCategoryModel) {
      /*  daoSession.getSu.deleteAll();
        int count = 0;
        for (ContactResponseModel contactResponseModel : contactResponseModels) {
            String mobile = "243545455" + count;
            contactResponseModel.getPhoneData().setMobile(mobile);
            long insertId = daoSession.getPhoneModelDao().insert(contactResponseModel.getPhoneData());
            count++;
            contactResponseModel.setPhone_id(insertId);
            daoSession.getContactResponseModelDao().insert(contactResponseModel);
        }*/
        daoSession.getSubCategoryModelDao().insertOrReplace(subCategoryModel);

    }

    public List<SubCategoryModel> getSubcategoryList() {
        return daoSession.getSubCategoryModelDao().loadAll();
    }


}
