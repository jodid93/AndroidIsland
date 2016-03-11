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
    private UserData userData;
    private static final String UsrDat = "idleisland.userdata";

    public static Intent newIntent(Context packageContext, String usrData){
        Intent i = new Intent(packageContext, UpgradeMenu.class);
        i.putExtra(UsrDat, usrData);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrademenu);
        s_UserData = getIntent().getStringExtra(UsrDat);
        this.userData = UserData.getInstance(s_UserData);
        System.out.println(this.userData.getUserName());
        this.userData.printUpgrades();
    }
}
