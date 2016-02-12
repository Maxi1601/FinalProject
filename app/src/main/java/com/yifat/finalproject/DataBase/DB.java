package com.yifat.finalproject.DataBase;

public class DB {

    public static final String NAME = "MyPlaces.db"; // Database file name.
    public static final int VERSION = 1; // Places Database version.

    // Inner Class
    public static class Favorites {

        public static final String TABLE_NAME = "Favorites"; // Table name.
        public static final String ID = "id"; // id column name (this is the primary key)
        public static final String NAME = "name";
        public static final String ADDRESS = "address";
        public static final String DISTANCE = "distance";
        public static final String URL = "url";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";

        public static final String[] ALL_COLUMNS = new String[] { ID, NAME, ADDRESS, DISTANCE, URL, LATITUDE, LONGITUDE };

        // the creation table is a string.
        public static final String CREATION_STATEMENT = "CREATE TABLE " + TABLE_NAME +
                " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT, " +
                ADDRESS + " TEXT, " +
                DISTANCE + " NUMERIC, " +
                URL + " TEXT, " +
                LATITUDE + " NUMERIC, " +
                LONGITUDE + " NUMERIC" + ")";

        //TODO: make sure that the place is unique (by name+lat+lng is probably good enough).

        public static final String DELETION_STATEMENT = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}
