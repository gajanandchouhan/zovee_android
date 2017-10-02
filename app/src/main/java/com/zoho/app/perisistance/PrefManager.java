package com.zoho.app.perisistance;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zoho.app.model.response.CategoryModel;

import java.lang.reflect.Type;
import java.util.List;

import static android.content.Context.CAMERA_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by hp on 30-05-2017.
 */

public class PrefManager implements PrefConstants {
    private static final String SELECTED_LIST = "selected_cat";
    private static PrefManager instance;
    SharedPreferences sharedPreferences;

    public PrefManager(Context mContext) {
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
    }

    public static PrefManager getInstance(Context mContext) {
        if (instance == null) {
            instance = new PrefManager(mContext);
        }
        return instance;
    }

    public void putString(String key, String value) {
        sharedPreferences.edit().putString(key, value).commit();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public List<CategoryModel> getSelectedCategoryList() {
        List<CategoryModel> categoryModels = null;
        String string = getString(SELECTED_LIST);
        if (string != null && string.length() > 0) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<CategoryModel>>() {
            }.getType();
            categoryModels = gson.fromJson(string, type);
        }
        return categoryModels;

    }

    public void saveSelectedList(List<CategoryModel> categoryModelList) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<CategoryModel>>() {
        }.getType();
        String s = gson.toJson(categoryModelList, type);
        putString(SELECTED_LIST, s);
    }

    public void putInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).commit();
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }
}
