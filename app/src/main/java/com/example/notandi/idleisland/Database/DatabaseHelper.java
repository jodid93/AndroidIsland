package com.example.notandi.idleisland.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.notandi.idleisland.Utils.Security;
import com.example.notandi.idleisland.User.User;
import com.example.notandi.idleisland.User.UserData;
import com.google.gson.Gson;

import java.io.IOException;


//TODO: Create new table because 'user' table attributes has changes.

//
// IN GENERAL ...
//  This class has access to local SQLite database


//
//  DRAWBACK =
//

//
//  = implementation
//      -   I think that is much more wiser to create
//          a class that controls the online database
//          alongside to the local database.

//
//  HOW TO USE:
//
// FIRST get a instance of the database
//
//      -  DatabaseHelper DB = DatabaseHelper.getInstance(this);
//
//
// [methods]
//      - DB.createNewUser(userName,password);
//          +   newUser has been stored in localDatabase and
//              his UserData is now on the running device.
//
//      - Boolean valid = DB.isValid( userName, password );
//          + 'true' if userName exists and the password matches
//          + 'false' otherwise
//
//
//      - DB.getUserData( userName );
//          +   If the userName exists in local database,
//              now is the UserData from user userName
//              on the running device.
//
//
//      - User oldUser = DB.getUser( userName );
//          + return a user from the database with the name userName
//
//
//      - DB.insertUserData( username, userdata );
//          + The userdata has been stored in database alongside the user
//          + SAME WORK IN....
//          - DB.insertUserData( userName, userDataJOSN );
//              - userDataJSON is a String
//
//      - Boolean isName = DB.userNameExists( userName );
//          + 'true' if userName exists in the loacaldatabase
//          + 'false' otherwise
//
//      - DB.clearTable();
//          + Removes all data from the SQLite database
//
/**
 * Created by Lenovo on 11.2.2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper sInstance;
    private static SQL SQLQuery = new SQL();

    private Security security = new Security();

    public static final int DATABASE_VERSION = 6;
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

    public DatabaseHelper(Context context) {
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
        db.execSQL(SQLQuery.getCreateUserDataTable());
    }


    public void createNewUser(String userName, String password)  {

        if( !userNameExists(userName) ){

            Long newRowId;

            //create new userData for user
            //User newUser = new User( userName );
            UserData userData = UserData.getInstance( userName );// newUser.getUserData();
            String userDataJSON = userData.toJSONString(); //userDataToJSON( userData );

            String hashPassword = security.hashPassword( password );

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(SQL.entry.USER_NAME, userName);
            values.put(SQL.entry.PASSWORD, hashPassword);
            values.put(SQL.entry.USERDATA, userDataJSON);

            SQLiteDatabase db =  this.getWritableDatabase();
            newRowId = db.insert( SQL.entry.TABLE_NAME, null, values );
            db.close();
            this.close();

            //if database has update just one row
            if( newRowId == 1 ){
                //every thing in order
            } else {
                String errMessage = "Database changed "+newRowId+" rows in the table " +
                        SQL.entry.TABLE_NAME+", when it is supposed to change only one row";
                Log.d("-ERROR-DATABASE-", errMessage);
            }
        } else {
            Log.d("ERROR: CREATE USER", "Something went wrong, ");
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( SQLQuery.getDropTable() );
        onCreate(db);
    }


    public void insertUserData(String userName, UserData userData) {

        String userDataJSON = userData.toJSONString(); //userDataToJSON(userData);
        try {
            insertUserData( userName, userDataJSON );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insertUserData( String userName, String userDataJSON ) throws IOException {

        int newRowId = 0;

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(SQL.entry.USERDATA, userDataJSON);

        //query variables
        String[] projection     =  new String[]{ SQL.entry.USER_NAME };
        String whereClause   = SQL.entry.USER_NAME +" = ?";
        String[] whereArgs    =  new String[]{ userName };


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
            Log.d("ERROR:DATABASE","User " + userName + " doesn't exists!");
            throw new IOException();
        }

        this.close();
    }



    public Boolean userNameExists( String userName ){
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
        //if result is true, then the user name exists otherwise not
        Boolean result = !(!(cursor.moveToFirst()) || cursor.getCount() == 0);
        Log.d("CHECK USERNAME EXISTS","result -> " + result);
        cursor.close();
        db.close();

        return result;
    }




    public void getUserData(String userName){

        String[] projection =  new String[]{
                SQL.entry.USERDATA
        };

        String whereClause   = SQL.entry.USER_NAME +" = ?";
        String[] whereArgs   = new String[]{ userName };


        if( userNameExists(userName) ){
            SQLiteDatabase db = this.getReadableDatabase();


            Cursor cursor = db.query(SQL.entry.TABLE_NAME,
                    projection,       // The columns to return (if null -> all columns)
                    whereClause,       // The columns for the WHERE clause
                    whereArgs,       // The values for the WHERE clause
                    null,       // don't group the rows
                    null,       // don't filter by row groups
                    null        // The sort order
            );

            String[] data = retriveDataFromCursor(cursor, projection);

            cursor.close();
            this.close();

            UserData.convertStringToUserData(data[0]);
        }else{
            Log.d("GET USERDATA", "User doen't exists, name-> "+userName);
            return;
        }
    }


    public void clearTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        Integer rowsEffected = db.delete(SQL.entry.TABLE_NAME, null, null);
        String message="clearing table \""+SQL.entry.TABLE_NAME+"\" and "+rowsEffected+" rows was effected.";
        Log.d("DATABASE CLEAR TALBE",message);
        db.close();
    }


    private String[] retriveDataFromCursor( Cursor cursor, String[] columns ){
        String[] data = new String[ columns.length ];

        //Get data from
        if (cursor.moveToFirst()){
            for(int i=0;i<columns.length;i++){
                do{
                    data[i] = cursor.getString(cursor.getColumnIndex(columns[i]));
                    Log.d("RETRIEVE DATA",data[i]);
                    // do what ever you want here
                }while(cursor.moveToNext());
            }
        }
        return data;
    }

    // If user userName exist, it will return hashed
    // password from the local database
    private String getPassword(String userName){
        SQLiteDatabase db = this.getReadableDatabase();

        //query variables
        String[] projection = new String[]{SQL.entry.PASSWORD};
        String whereClause = SQL.entry.USER_NAME + " = ?";
        String[] whereArgs = new String[]{userName};

        //Check if user exist in the table
        Cursor cursor = db.query(SQL.entry.TABLE_NAME,
                projection,       // The columns to return (if null -> all columns)
                whereClause,       // The columns for the WHERE clause
                whereArgs,       // The values for the WHERE clause
                null, null, null
        );

        String[] data = retriveDataFromCursor(cursor, projection);

        cursor.close();
        db.close();

        return data[0];
    }


    public Boolean isValid(String userName, String password){
        String pw = getPassword(userName);
        String hashedPassword = security.hashPassword( password );
        Log.i("IS VALID USER","pass-> " + pw);
        Log.i("IS VALID USER","hassPass-> " + hashedPassword);
        if( hashedPassword==null || pw==null){
            return false;
        } else{
            return (pw.equals(hashedPassword) );
        }
    }
}