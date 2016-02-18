package com.example.notandi.idleisland;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lenovo on 11.2.2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper sInstance;
    private static SQL query;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "idlIsland.db";

    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.w w w
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public DatabaseHelper(Context context/*, String name, SQLiteDatabase.CursorFactory factory, int version*/) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    public void insert(SQLiteDatabase db){
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(SQL.entry.USER_NAME, "GOGGI");
        values.put(SQL.entry.GAMESTATE, "title");

        // Insert the new row, returning the primary key value of the new row
        long newRowId;

        newRowId = db.insert(
                SQL.entry.TABLE_NAME,
                null,
                values);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( query.getCreateTable() );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( query.getDropTable() );
        onCreate(db);
    }
}
