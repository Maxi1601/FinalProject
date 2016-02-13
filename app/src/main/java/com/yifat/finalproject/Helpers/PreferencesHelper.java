package com.yifat.finalproject.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.yifat.finalproject.Constants;

public class PreferencesHelper {

    public static void saveLatitude(Context context, String key, double value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.LATITUDE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, Double.doubleToLongBits(value));
        editor.commit();
    }

    public static void saveLongitude(Context context, String key, double value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.LONGITUDE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, Double.doubleToLongBits(value));
        editor.commit();
    }

    public static double loadLatitude(Context context, String key) {
        Log.d("Avner","H1");
        if (context == null) {
            Log.d("Avner","H2");
        }
        Log.d("Avner","H3");
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.LATITUDE, context.MODE_PRIVATE);
        Log.d("Avner","H4");
        if (sharedPreferences  == null) {
            Log.d("Avner","H5");
        }
        Log.d("Avner","H6");
        Long l = sharedPreferences.getLong(key, 0);
        Log.d("Avner","H7");
        if (l == null) {
            Log.d("Avner","H8");
        }
        Log.d("Avner","H9");
        return Double.longBitsToDouble(l);
    }

    public static double loadLongitude(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.LONGITUDE, context.MODE_PRIVATE);
        return Double.longBitsToDouble(sharedPreferences.getLong(key, 0));
    }

    public static void savePlacesJson (Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.JSON, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,(value));
        editor.commit();
    }

    public static String loadPlacesJson (Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.JSON, context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

}
