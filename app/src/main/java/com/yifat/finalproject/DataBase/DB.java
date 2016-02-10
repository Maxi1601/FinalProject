package com.yifat.finalproject.DataBase;

public class DB {

    public static final String NAME = "PlacesDatabase.db"; // Database file name.
    public static final int VERSION = 1; // Places Database version.

    // Inner Class
    public static class Favorites {

        public static final String TABLE_NAME = "Favorites"; // Table name.
        public static final String ID = "id"; // id column name (this is the primary key)
        public static final String NAME = "name";
        public static final String ADDRESS = "Address";
        public static final String URL = "url";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "Longitude";

        public static final String[] ALL_COLUMNS = new String[] { ID, NAME, ADDRESS, URL, LATITUDE, LONGITUDE };

        // the creation table is a string.
        public static final String CREATION_STATEMENT = "CREATE TABLE " + TABLE_NAME +
                " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT, " +
                ADDRESS + " TEXT, " +
                URL + " TEXT, " +
                LATITUDE + " NUMERIC, " +
                LONGITUDE + " NUMERIC" + ")";

        public static final String DELETION_STATEMENT = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    // Inner Class
    public static class LastSearch {

        public static final String TABLE_NAME = "LastSearch"; // Table name.
        public static final String ID = "id"; // id column name (this is the primary key)
        public static final String NAME = "name";
        public static final String ADDRESS = "Address";
        public static final String URL = "url";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "Longitude";

        public static final String[] ALL_COLUMNS = new String[] { ID, NAME, ADDRESS, URL, LATITUDE, LONGITUDE };

        // the creation table is a string.
        public static final String CREATION_STATEMENT = "CREATE TABLE " + TABLE_NAME +
                " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT, " +
                ADDRESS + " TEXT, " +
                URL + " TEXT, " +
                LATITUDE + " NUMERIC, " +
                LONGITUDE + " NUMERIC" + ")";

        public static final String DELETION_STATEMENT = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}
