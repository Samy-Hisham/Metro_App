package com.example.samy.cairometro;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    private static final String SHARED_PREFS_NAME = "MyPrefs";
    private static SharedPreferences pref;
    private static SharedPreferencesHelper instance;

    private SharedPreferencesHelper(Context context) {
        pref = context.getApplicationContext().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPreferencesHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesHelper(context);
        }
        return instance;
    }

    public void setStartStation(String key, String value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStartStation(String key, String defaultValue) {
        return pref.getString(key, defaultValue);
    }

    public void setArrivalStation(String key, String value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getArrivalStation(String key, String defaultValue) {
        return pref.getString(key, defaultValue);
    }

    public void setArrivalPos(String key, byte value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getArrivalPos(String key, byte defaultValue) {
        return pref.getInt(key, defaultValue);
    }

    public void setStartPos(String key, byte value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getStartPos(String key, byte defaultValue) {
        return pref.getInt(key, defaultValue);
    }
    // Add more methods for different data types as needed

    public void clear() {
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }
}
