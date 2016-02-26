package com.yifat.finalproject.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {

    // Saving last results
    public static void saveLastResult(Context context, String key, String value) {
        if (context == null) {
            return;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.JSON, context.MODE_PRIVATE);
        if (sharedPreferences == null) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, (value));
        editor.commit();
    }

    // Loading last results (in case there's no internet)
    public static String loadLastResult(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.JSON, context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

}
