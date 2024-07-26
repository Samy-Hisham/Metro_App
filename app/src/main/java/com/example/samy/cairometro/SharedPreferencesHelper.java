package com.example.samy.cairometro;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesHelper {

    private static final String SHARED_PREFS_NAME = "MyPrefs";
    private static SharedPreferences pref;
    private static SharedPreferencesHelper instance;
    private static final String LIST_KEY = "my_list";
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

    public void saveList(List<InfoStation> list) {
        SharedPreferences.Editor editor = pref.edit();
        StringBuilder sb = new StringBuilder();
        for (InfoStation obj : list) {
            sb.append(obj.toString()).append(";");
        }
        editor.putString(LIST_KEY, sb.toString());
        editor.apply();
    }

    public List<InfoStation> getList() {
        String listString = pref.getString(LIST_KEY, null);
        List<InfoStation> list = new ArrayList<>();
        if (listString != null) {
            String[] items = listString.split(";");
            for (String item : items) {
                if (!item.isEmpty()) {
                    list.add(InfoStation.fromString(item));
                }
            }
        }
        return list;
    }
    // Add more methods for different data types as needed

//    public void setInfoStation(String key, String value) {
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putString(key, value);
//        editor.apply();
//    }
//
//    public String getInfoStation(String key, String defaultValue) {
//        return pref.getString(key, defaultValue);
//    }

//    public void setCustomObject(InfoStation infoStation) {
//        try {
//            byte[] serializedObject = InfoStation.serializeObject(infoStation);
//            SharedPreferences.Editor editor = pref.edit();
//            editor.putString(KEY_CUSTOM_OBJECT, Base64.encodeToString(serializedObject, Base64.DEFAULT));
//            editor.apply();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public InfoStation getCustomObject() {
//        String serializedObject = pref.getString(KEY_CUSTOM_OBJECT, null);
//        if (serializedObject != null) {
//            try {
//                byte[] data = Base64.decode(serializedObject, Base64.DEFAULT);
//                return InfoStation.deserializeObject(data);
//            } catch (IOException | ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }

    public void clear() {
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }
}
