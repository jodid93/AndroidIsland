package com.example.notandi.idleisland;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.sql.PreparedStatement;


//TODO: Create new table because 'user' table attributes has changes.

////////
// API//
////////
// FIRST get database
//
//      -  DatabaseHelper DB = DatabaseHelper.getInstance(this);
//
//
// [methods]
//      - User newUser = DB.createNewUser(userName,password);
//          + newUser has been stored in localDatabase
//
//      - Boolean valid = DB.isValid( userName, password );
//          + 'true' if userName exists and the password matches
//          + 'false' otherwise
//
//      - DB.insertUserData( user );
//
//      - User oldUser = DB.getUser( userName );
//          + return a user from the database with the name userName
//
//      - User oldUser = DB.getUser( userName, password );
//          + return a user from the database if userName and password exists
//
//      - DB.insertUserData( user );
//          + The UserData from the user has been stored in database alongside the user
//          + ALSO....
//          - DB.insertUserData( userName, userData );
//          - DB.insertUserData( userName, userDataJOSN );
//
//      - Boolean isName = DB.userNameExists( userName );
//          + 'true' if userName exists in the loacaldatabase
//          + 'false' otherwise
//
//
//
/**
 * Created by Lenovo on 11.2.2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper sInstance;
    private static SQL SQLQuery = new SQL();

    private Security security = new Security();

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
        db.execSQL(SQLQuery.getCreateUserDataTable());
    }


    public User createNewUser(String userName, String password) throws IOException {

        if( !userNameExists(userName) ){

            Long newRowId;

            //create new userData for user
            User newUser = new User( userName );
            UserData userData = newUser.getUserData();
            String userDataJSON = userDataToJSON( userData );

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
            return newUser;
        } else {
            String errorMessage =
                    "The error is probobly " +
                            "With the username!\n" +

                    "";
            Log.d("ERROR: CREATE USER", "Something went wrong, ");
            throw new IOException();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( SQLQuery.getDropTable() );
        onCreate(db);
    }


    public void insertUserData(String userName, UserData userData) throws IOException {
        String userDataJSON = userDataToJSON(userData);
        insertUserData( userName, userDataJSON );
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

    public void insertUserData(User user) throws IOException {
        String userName = user.getUserName();
        UserData userData = user.getUserData();
        insertUserData(userName, userData);
    }

    //return false if another user has same name (ég veit þetta er local)
    //return true if user dosen't exists
    /*public Boolean insertUser( User user ){

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
    }*/


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
        //if result is true, then the user name exists otherwise not
        Boolean result = !(!(cursor.moveToFirst()) || cursor.getCount() == 0);
        Log.d("CHECK USERNAME EXISTS","result -> " + result);
        cursor.close();
        db.close();

        return result;
    }


    //TODO: thing this is useless
    public UserData getUserData(User user){

        String[] projection =  new String[]{
                SQL.entry.USERDATA
        };

        String whereClause   = SQL.entry.USER_NAME +" = ?";
        String[] whereArgs   = new String[]{ user.getUserName() };


        if( userNameExists(user.getUserName()) ){
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

            return getUserDataFromJSON(data[0]);
        }else{
            Log.d("GET USERDATA", "User doen't exists, name-> "+user.getUserName());
            return new UserData("Villumadurinn");
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
                    Log.d("RETRIEVE DATA FROM CURSOR",data[i]);
                    // do what ever you want here
                }while(cursor.moveToNext());
            }
        }
        return data;
    }


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
        return (pw == hashedPassword);
    }

    //precondition: the user have to exists.
    //Recommended to user 'isValid(userName,password)' to check if user exists
    public User getUser( String userName, String password ){
        SQLiteDatabase db = this.getReadableDatabase();

        //query variables
        String[] projection = new String[]{SQL.entry.USER_NAME, SQL.entry.USERDATA};
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

        return createUser( data );
    }

    // If this userName exists in database, that User is returned
    // otherwise it will return and store a new user in the database
    public User getUser( String userName ) throws IOException {

        if( userNameExists(userName) ) {
            SQLiteDatabase db = this.getReadableDatabase();

            //query variables
            String[] projection = new String[]{SQL.entry.USER_NAME, SQL.entry.USERDATA};
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

            return createUser( data );
        } else {
            Log.d("ERROR: DATABASE","The user "+userName+" doesn't exists!");
            throw new IOException();
        }
    }

    // data contains user name and UserData
    private User createUser(String[] data){
        Log.d("CREATE USER","data[0]->"+data[0]+" and data[1]->"+data[1]);
        return new User(data[0], data[1]);
    }


    // Converts UserData userD to JSON String
    // and returns it
    public String userDataToJSON(UserData userData){
        Log.d("-- DFSJKLDSFKJL---",userData.getUserName());
        return this.gson.toJson(userData);
    }

    public UserData getUserDataFromJSON(String json) {
        Log.d("CONVERT JSON TO USERDATA","Taking the string \""+json+"\"");
        UserData newData = this.gson.fromJson(json , UserData.class);
        return newData;
    }
}