package com.yifat.finalproject.DataBase;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DAL extends SQLiteOpenHelper {

    // An object which can access the database
    private SQLiteDatabase database; // Data Member

    // Constructor:
    public DAL(Activity activity) {
        super(activity, DB.NAME, null, DB.VERSION);
    }

    // Will be called when the app is first running
    public void onCreate(SQLiteDatabase db) {
        String path = db.getPath();
        Log.d("DAL", "db.path is " + path);
        db.execSQL(DB.Favorites.CREATION_STATEMENT); // Execute sql statement
        Cursor cursor = db.rawQuery("SELECT * FROM Favorites", null);
        Object names = cursor.getColumnNames();
        Log.d("DAL", names.toString());
    }

    // Will be called when the app is being upgraded if our version will be different
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DB.Favorites.DELETION_STATEMENT);
        db.execSQL(DB.Favorites.CREATION_STATEMENT);
    }

    // Opening the database for any action
    public void open() {
        Log.d("DAL", "Opening the database");
        database = getWritableDatabase(); // This is the open method
    }

    // Closing the database
    public void close() {
        Log.d("DAL", "Closing the database");
        super.close(); // This is the close method
    }

    // Adding a new row to a table
    public long insert(String tableName, ContentValues values) {
        Log.d("DAL", "Inserting to the database " + tableName + values.toString());
        long createdId = database.insert(tableName, null, values);
        return createdId;
    }

    // Getting back specific records (rows) from the given table
    public Cursor getTable(String tableName, String[] columns, String where) {
        Log.d("DAL", "Cursor get table on query where: " + where);
        Cursor c1 = database.query(tableName, columns, where, null, null, null, null);
        Log.d("DAL", c1.toString());
        return c1;
    }

    // Deleting an existing records
    public long delete(String tableName, String where) {
        return database.delete(tableName, where, null);
    }

}
