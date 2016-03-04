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
        //if result is true, then the user name exists otherwise not
        Boolean result = !(!(cursor.moveToFirst()) || cursor.getCount() == 0);
        Log.d("CHECK USERNAME EXISTS","result -> " + result);
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
                null, null, null
        );

        Boolean result = (cursor != null);
        cursor.close();
        db.close();

        return result;
    }

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


    // If this userName exists in database, that User is returned
    // otherwise it will return and store a new user in the database
    public User getUser( String userName ){

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
            User newUser = new User(userName);
            insertUser( newUser );
            return newUser;
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

    public UserData getUserDataFromJSON(String json){
        Log.d("CONVERT JSON TO USERDATA","Taking the string \""+json+"\"");
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