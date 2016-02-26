package com.example.notandi.idleisland;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class Main extends AppCompatActivity {

    private Button mLogInButton;
    private Button mRegisterButton;
    //private DatabaseHelper DB;
    private static final int ENTER_GAME = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLogInButton = (Button) findViewById(R.id.LogIn_button);
        mLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userdata = getUserdata();
                Intent i = MenuActivity.newIntent(Main.this, userdata);
                startActivityForResult(i, ENTER_GAME);
            }
        });
        mRegisterButton = (Button) findViewById(R.id.Register_button);


        //
        // TEST USER AND DATABASE
        //

        //String userName = "hannes";
        //INITILIZE USER "hannes"
        //User hannes = new User( userName );
        //Get the database
        //DatabaseHelper DB = DatabaseHelper.getInstance(this);
        //We insert the user and
        //Integer rowID = DB.insertUserData( hannes );
        //Log.d("ROW_ID ->>>",rowID.toString());
        //String[] JSONUserData = DB.getUserData();
    }

    public static String getUserdata(){
        //TODO implementa allt database dót
        return "json object sem verdur userdata";
    }
}