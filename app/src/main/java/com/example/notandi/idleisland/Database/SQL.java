package com.example.notandi.idleisland.Database;

import android.content.ContentValues;
import android.provider.BaseColumns;

/**
 * Created by Lenovo on 18.2.2016.
 */
public class SQL {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public SQL() {}

    /* Inner class that defines the table contents */
    public static abstract class entry implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String USER_NAME = "username";
        public static final String PASSWORD = "password";
        public static final String USERDATA = "gamestate";
    }

    private static final String TEXT_TYPE = " TEXT ";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + entry.TABLE_NAME + " ( " +
                    entry.USER_NAME + TEXT_TYPE+" PRIMARY KEY, " +
                    entry.PASSWORD + TEXT_TYPE + COMMA_SEP +
                    entry.USERDATA + TEXT_TYPE +
            " );";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + entry.TABLE_NAME;


    private static final String SQL_GET_USERNAME = "SELECT * FROM " + entry.TABLE_NAME;

    public String getCreateUserDataTable(){ return SQL_CREATE_ENTRIES; }

    public String getDropTable(){
        return  SQL_DELETE_ENTRIES;
    }

    public String getUserName() {
        return SQL_GET_USERNAME;
    }

    public void insertValueIn(String col,String val,ContentValues cont){

    }
}
