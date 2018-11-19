package com.planera.mis.planera2.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceConnector {
    private static PreferenceConnector instance;
    private SharedPreferences preferences;
    private Context context;

    public PreferenceConnector(Context context){
        this.context = context;
        preferences = context.getSharedPreferences(AppConstants.PREFRENCE_FILE_NAME, 0);
    }

    public static PreferenceConnector getInstance(Context context) {
        if (instance == null){
            instance = new PreferenceConnector(context);
        }
        return instance;
    }

    public void setString(String key, String value){
        preferences.edit().putString(key, value).apply();
    }

    public String getString(String key){
       return preferences.getString(key, null);
    }


    public void setInteger(String key, int value){
        preferences.edit().putInt(key, value).apply();
    }

    public int getInteger(String key){
        return preferences.getInt(key, 0);
    }

    public void setBoolean(String key, boolean value){
        preferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key){
        return preferences.getBoolean(key, false);
    }

    public void setFload(String key, float value){
        preferences.edit().putFloat(key, value).apply();
    }


    public float getFloat(String key){
        return preferences.getFloat(key, 0);
    }
}
