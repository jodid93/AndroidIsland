package com.example.notandi.idleisland;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by josua on 5.3.2016.
 */
public class UpgradeMenu extends AppCompatActivity {

    private String s_UserData;
    private static final String UsrDat = "idleisland.userdata";

    public static Intent newIntent(Context packageContext, String usrData){
        Intent i = new Intent(packageContext, GameEngine.class);
        i.putExtra(UsrDat, usrData);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        s_UserData = getIntent().getStringExtra(UsrDat);


    }
}
