package com.yifat.finalproject.DataBase;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import com.yifat.finalproject.Place;
import java.util.ArrayList;

public class FavoritesLogic extends BaseLogic {

    public FavoritesLogic(Activity activity) {
        super(activity);
    }

    public long addFavorite(Place place) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(DB.Favorites.NAME, place.getName());
        contentValues.put(DB.Favorites.ADDRESS, place.getAddress());
        contentValues.put(DB.Favorites.URL, place.getUrl());
        contentValues.put(DB.Favorites.LATITUDE, place.getLat());
        contentValues.put(DB.Favorites.LONGITUDE, place.getLng());

        long createdId = dal.insert(DB.Favorites.TABLE_NAME, contentValues);

        return createdId;
    }

    public long updateFavorite(Place place) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(DB.Favorites.NAME, place.getName());
        contentValues.put(DB.Favorites.ADDRESS, place.getAddress());
        contentValues.put(DB.Favorites.URL, place.getUrl());
        contentValues.put(DB.Favorites.LATITUDE, place.getLat());
        contentValues.put(DB.Favorites.LONGITUDE, place.getLng());

        String where = DB.Favorites.ID + "=" + place.getId();

        long affectedRows = dal.update(DB.Favorites.TABLE_NAME, contentValues, where);

        return affectedRows;
    }

    public long deleteFavorite(Place place) {

        String where = DB.Favorites.ID + "=" + place.getId();

        long affectedRows = dal.delete(DB.Favorites.TABLE_NAME, where);

        return affectedRows;
    }

    private ArrayList<Place> getFavorites(String where) {

        ArrayList<Place> places = new ArrayList<>();

        Cursor cursor = dal.getTable(DB.Favorites.TABLE_NAME, DB.Favorites.ALL_COLUMNS, where);

        while(cursor.moveToNext()) {

            int idIndex = cursor.getColumnIndex(DB.Favorites.ID);
            long id = cursor.getLong(idIndex);
            String name = cursor.getString(cursor.getColumnIndex(DB.Favorites.NAME));
            String address = cursor.getString(cursor.getColumnIndex(DB.Favorites.ADDRESS));
            String url = cursor.getString(cursor.getColumnIndex(DB.Favorites.URL));
            double latitude = cursor.getDouble(cursor.getColumnIndex(DB.Favorites.LATITUDE));
            double longitude = cursor.getDouble(cursor.getColumnIndex(DB.Favorites.LONGITUDE));

            Place place = new Place(id, name, address, url, latitude, longitude);

            places.add(place);
        }

        cursor.close();

        return places;
    }

    public ArrayList<Place> getAllFavorites() {
        return getFavorites(null);
    }

}
