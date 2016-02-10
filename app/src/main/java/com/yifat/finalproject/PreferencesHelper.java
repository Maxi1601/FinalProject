package com.yifat.finalproject;

import android.content.Context;
import android.content.SharedPreferences;

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
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.LATITUDE, context.MODE_PRIVATE);
        return Double.longBitsToDouble(sharedPreferences.getLong(key, 0));
    }

    public static double loadLongitude(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.LONGITUDE, context.MODE_PRIVATE);
        return Double.longBitsToDouble(sharedPreferences.getLong(key, 0));
    }

    public static void savePlace(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PLACE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String loadPlace(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PLACE_NAME, context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
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
