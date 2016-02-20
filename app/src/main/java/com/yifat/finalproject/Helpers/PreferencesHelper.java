package com.yifat.finalproject.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PreferencesHelper {

    public static void saveLatitude(Context context, String key, double value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.LATITUDE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, Double.doubleToLongBits(value));
        editor.commit();
    }

    public static double loadLatitude(Context context, String key) {
        if (context == null) {
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.LATITUDE, context.MODE_PRIVATE);
        if (sharedPreferences  == null) {
        }
        Long l = sharedPreferences.getLong(key, 0);
        if (l == null) {
        }
        return Double.longBitsToDouble(l);
    }

    public static void saveLongitude(Context context, String key, double value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.LONGITUDE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, Double.doubleToLongBits(value));
        editor.commit();
    }

    public static double loadLongitude(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.LONGITUDE, context.MODE_PRIVATE);
        return Double.longBitsToDouble(sharedPreferences.getLong(key, 0));
    }

    public static void savePlacesJson (Context context, String key, String value) {
        Log.d("preferences1.1", "savePlacesJson1.1");
        if (context == null) {
            Log.d("preferences2.1", "savePlacesJson2.2");
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.JSON, context.MODE_PRIVATE);
        Log.d("preferences3.1", "savePlacesJson3.2");
        if (sharedPreferences == null) {
            Log.d("preferences4.1", "savePlacesJson4.2");
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,(value));
        editor.commit();
    }

    public static String loadPlacesJson (Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.JSON, context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static void saveFavorites (Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.FAVORITES, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,(value));
        editor.commit();
    }

    public static String loadFavorites (Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.FAVORITES, context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

}
