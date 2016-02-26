package com.example.notandi.idleisland;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by thorkellmani on 26/02/16.
 */
public class FriendsActivity extends AppCompatActivity{

    private String UserData;

    private static final String UsrDat = "fokkJósúa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        UserData = getIntent().getStringExtra(UsrDat);
    }

    public static Intent newIntent(Context packageContext, String usrData){
        Intent i = new Intent(packageContext, FriendsActivity.class);
        i.putExtra(UsrDat, usrData);
        return i;
    }
}
