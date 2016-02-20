package com.yifat.finalproject.DataBase;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.yifat.finalproject.Place;

import java.util.ArrayList;

public class FavoritesLogic extends BaseLogic {

    public FavoritesLogic(Activity activity) {
        super(activity);
    }

    // Add a place to the favorites database. If the place is already in the DB - return -1. Otherwise return the id of the added "favorite"
    public long addFavorite(Place place) {

        if (isFavorite(place)) {
            return -1;
        }

        ContentValues contentValues = new ContentValues();

        contentValues.put(DB.Favorites.NAME, place.getName());
        contentValues.put(DB.Favorites.ADDRESS, place.getAddress());
        contentValues.put(DB.Favorites.DISTANCE, place.getDistance());
        contentValues.put(DB.Favorites.URL, place.getUrl());
        contentValues.put(DB.Favorites.LATITUDE, place.getLat());
        contentValues.put(DB.Favorites.LONGITUDE, place.getLng());

        long createdId = dal.insert(DB.Favorites.TABLE_NAME, contentValues);

        return createdId;
    }

    // Deleting one place from the favorites database
    public long deleteFavorite(Place place) {

        String where = DB.Favorites.NAME + "=" + "\"" + place.getName() + "\"" + " AND " + DB.Favorites.LATITUDE + "=" + place.getLat() + " AND " + DB.Favorites.LONGITUDE + "=" + place.getLng();

        long affectedRows = dal.delete(DB.Favorites.TABLE_NAME, where);

        return affectedRows;
    }

    // Deleting all places from the favorites database
    public long deleteAllFavorites(String where) {

        long affectedRows = dal.delete(DB.Favorites.TABLE_NAME, where);

        return affectedRows;

    }

    // Private method for getting one favorite or more according to condition
    private ArrayList<Place> getFavorites(String where) {

        ArrayList<Place> places = new ArrayList<>();

        Cursor cursor = dal.getTable(DB.Favorites.TABLE_NAME, DB.Favorites.ALL_COLUMNS, where);

        while (cursor.moveToNext()) {

            Log.d("FavoritesLogic", cursor.toString());

            int idIndex = cursor.getColumnIndex(DB.Favorites.ID);
            long id = cursor.getLong(idIndex);
            String name = cursor.getString(cursor.getColumnIndex(DB.Favorites.NAME));
            String address = cursor.getString(cursor.getColumnIndex(DB.Favorites.ADDRESS));
            double distance = cursor.getDouble(cursor.getColumnIndex(DB.Favorites.DISTANCE));
            String url = cursor.getString(cursor.getColumnIndex(DB.Favorites.URL));
            double latitude = cursor.getDouble(cursor.getColumnIndex(DB.Favorites.LATITUDE));
            double longitude = cursor.getDouble(cursor.getColumnIndex(DB.Favorites.LONGITUDE));

            Place place = new Place(id, name, address, distance, url, latitude, longitude);

            places.add(place);
        }

        cursor.close();

        return places;
    }

    // Getting all the places in the favorites database
    public ArrayList<Place> getAllFavorites() {
        return getFavorites(null);
    }

    // Checking if a place is already in the favorites database
    public boolean isFavorite(Place place) {
        String where = DB.Favorites.NAME + "=" + "\"" + place.getName() + "\"" + " AND " + DB.Favorites.LATITUDE + "=" + place.getLat() + " AND " + DB.Favorites.LONGITUDE + "=" + place.getLng();
        Log.d("FavoritesLogic", where);
        Cursor cursor = dal.getTable(DB.Favorites.TABLE_NAME, DB.Favorites.ALL_COLUMNS, where);
        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

}
