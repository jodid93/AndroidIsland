package com.example.notandi.idleisland;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by Lenovo on 11.2.2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper sInstance;
    private static SQL SQLQuery = new SQL();

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "idlIsland.db";
    private Gson gson = new Gson();

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
    }

    public void Read() throws android.database.SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
    }

    public void Write() throws android.database.SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
    }

    public void closeDB(){
        this.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLQuery.getCreateTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( SQLQuery.getDropTable() );
        onCreate(db);
    }


    public Integer insertUserData(User user){

        //initialize variables
        String userName = user.getUserName();
        UserData userData = user.getUserData();
        String userDataJSON = userDataToJSON(userData);
        int newRowId;

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(SQL.entry.USERDATA, userDataJSON);

        //query variables
        String[] projection     =  new String[]{ SQL.entry.USER_NAME };
        String whereClause   = SQL.entry.USER_NAME +" = ?";
        String[] whereArgs    =  new String[]{ userName };

        //start database


        if( userNameExists(userName) ){
            SQLiteDatabase db = this.getWritableDatabase();
            newRowId = db.update(SQL.entry.TABLE_NAME, values, whereClause, whereArgs);

            //if database has update just one row
            if( newRowId == 1 ){
                //every thing in order
            } else {
                String errMessage = "Database changed "+newRowId+" rows in the table " +
                        SQL.entry.TABLE_NAME+", when it is supposed to change only one row";
                Log.d("-ERROR-DATABASE-", errMessage);
            }
            db.close();
        } else {
            //If user doesn't exists
            insertUser( user );
        }

        this.close();
        return 1;
    }

    //return false if another user has same name (ég veit þetta er local)
    //return true if user dosen't exists
    public Boolean insertUser( User user ){

        if( userNameExists( user.getUserName() ) ){
            return false;
        } else {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(SQL.entry.USER_NAME, user.getUserName());
            values.put(SQL.entry.USERDATA, userDataToJSON(user.getUserData()));

            Long newRowId = db.insert(
                    SQL.entry.TABLE_NAME,
                    null,
                    values);

            db.close();
            return true;
        }
    }


    private Boolean userNameExists( String userName ){
        SQLiteDatabase db = this.getReadableDatabase();

        //query variables
        String[] projection     =  new String[]{ SQL.entry.USER_NAME };
        String whereClause   = SQL.entry.USER_NAME +" = ?";
        String[] whereArgs    =  new String[]{ userName };

        //Check if user exist in the table
        Cursor cursor = db.query(SQL.entry.TABLE_NAME,
                projection,       // The columns to return (if null -> all columns)
                whereClause,       // The columns for the WHERE clause
                whereArgs,       // The values for the WHERE clause
                null, null, null
        );

        Boolean result = (cursor != null);
        cursor.close();
        db.close();

        return result;
    }

    public Boolean userExist(String userName){
        SQLiteDatabase db = this.getReadableDatabase();

        //query variables
        String[] projection     =  new String[]{ SQL.entry.USER_NAME };
        String whereClause   = SQL.entry.USER_NAME +" = ?";
        String[] whereArgs    =  new String[]{ userName };

        //Check if user exist in the table
        Cursor cursor = db.query(SQL.entry.TABLE_NAME,
                projection,       // The columns to return (if null -> all columns)
                whereClause,       // The columns for the WHERE clause
                whereArgs,       // The values for the WHERE clause
                null,null,null
        );

        Boolean result = (cursor != null);
        cursor.close();
        db.close();

        return result;
    }

    public String[] getUserData(){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection =  new String[]{
                SQL.entry.USERDATA
        };

        Cursor cursor = db.query(SQL.entry.TABLE_NAME,
                projection,       // The columns to return (if null -> all columns)
                null,       // The columns for the WHERE clause
                null,       // The values for the WHERE clause
                null,       // don't group the rows
                null,       // don't filter by row groups
                null        // The sort order
        );

        //Get data from
        String[] data = new String[ projection.length ];
        if (cursor.moveToFirst()){
            for(int i=0;i<projection.length;i++){
                do{
                    data[i] = cursor.getString(cursor.getColumnIndex(projection[i]));
                    // do what ever you want here
                }while(cursor.moveToNext());
            }
        }

        cursor.close();
        this.close();


        Log.d("Get data from user....", data[0]);
        return data;
    }
        //

    // Converts UserData userD to JSON String
    // and returns it
    public String userDataToJSON(UserData userData){
        Log.d("-- DFSJKLDSFKJL---",userData.getUserName());
        return this.gson.toJson(userData);
    }

    public UserData getUserDataFromJSON(String json){
        UserData newData = this.gson.fromJson(json , UserData.class);
        return newData;
    }
    //EXAMPLE OF INSERTING DATA TO DATABASE
    //

    public void insert(){

        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(SQL.entry.USER_NAME, "GOGGI");
        values.put(SQL.entry.USERDATA, "title");

        // Insert the new row, returning the primary key value of the new row
        long newRowId;

        newRowId = db.insert(
                SQL.entry.TABLE_NAME,
                null,
                values);

        this.close();
    }


    //
    //EXAMPLE OF GETTING FROM DATABASE
    //

    public String getUserName(){

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                SQL.entry.USER_NAME
        };

        Cursor cursor = db.query(SQL.entry.TABLE_NAME,
                projection, // The columns to return
                null,       // The columns for the WHERE clause
                null,       // The values for the WHERE clause
                null,       // don't group the rows
                null,       // don't filter by row groups
                null        // The sort order
        );

        this.close();
        return String.valueOf(cursor.getColumnCount());
    }

}