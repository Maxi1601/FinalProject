package com.yifat.finalproject.DataBase;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DAL extends SQLiteOpenHelper {

    // An object which can access the database:
    private SQLiteDatabase database; // Data Member

    // Constructor:
    public DAL(Activity activity) {
        super(activity, DB.NAME, null, DB.VERSION);
    }

    // Will be called when the app is first running:
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB.Favorites.CREATION_STATEMENT); // Execute sql statement
    }

    // Will be called when the app is being upgraded if our version will be different:
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Add columns to existing table: http://stackoverflow.com/questions/4253804/insert-new-column-into-table-in-sqlite

        db.execSQL(DB.Favorites.DELETION_STATEMENT);
        db.execSQL(DB.Favorites.CREATION_STATEMENT);
    }

    // Opening the database for any action:
    public void open() {
        database = getWritableDatabase(); // This is the open method.
    }

    // Closing the database:
    public void close() {
        super.close(); // This is the close method.
    }

    // Add a new row to a table:
    // ContentValues is a key-value collection.
    // each item has a key = column name, and a value = cell value.
    // example: key = "firstName", value = "Moishe"
    public long insert(String tableName, ContentValues values) {
        long createdId = database.insert(tableName, null, values);
        return createdId;
    }

    // Get back all records (rows) from the given table.
    // Cursor = an object which points on one row at a time and enables to read the row's data.
    // We then can advanced the cursor to the next line
    public Cursor getTable(String tableName, String[] columns) {
        return database.query(tableName, columns, null, null, null, null, null);
    }

    // Get back specific records (rows) from the given table.
    public Cursor getTable(String tableName, String[] columns, String where) {
        return database.query(tableName, columns, where, null, null, null, null);
    }

    // Update an existing records:
    // The update will return the number of rows affected.
    public long update(String tableName, ContentValues values, String where) {
        long affectedRows = database.update(tableName, values, where, null);
        return affectedRows;
    }

    // Deleting an existing records:
    // The delete will return the number of rows affected.
    public long delete(String tableName, String where) {
        return database.delete(tableName, where, null);
    }

}
