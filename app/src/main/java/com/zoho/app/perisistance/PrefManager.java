package com.zoho.app.perisistance;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by hp on 30-05-2017.
 */

public class PrefManager implements PrefConstants {
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


}
