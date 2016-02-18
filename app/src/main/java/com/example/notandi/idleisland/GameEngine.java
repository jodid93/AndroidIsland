package com.example.notandi.idleisland;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class GameEngine extends AppCompatActivity {

    private String UserData;

    private static final String UsrDat = "idleisland.userdata";


    public static Intent newIntent(Context packageContext, String usrData){
        Intent i = new Intent(packageContext, GameEngine.class);
        i.putExtra(UsrDat,usrData);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        UserData = getIntent().getStringExtra(UsrDat);

        //turn title off
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

        setContentView(new GamePanel(this));
    }
}