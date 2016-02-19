package com.example.notandi.idleisland;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

/**
 * Created by thorgeir on 18.2.2016.
 */
public class User {

    private DatabaseHelper dbHelper;
    private UserData userData;

    public User( Context context ){
        this.dbHelper = new DatabaseHelper( context );
        Log.d("HALLO: ", "hallo4");
        userData = new UserData( "name" );
        Log.d("HALLO: ", "hallo5");
    }

    public void updateUserData( UserData userData ){

    }
}