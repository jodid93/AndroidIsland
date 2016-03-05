package com.example.notandi.idleisland;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by thorkellmani on 05/03/16.
 */
public class GiftActivity extends AppCompatActivity {

    private String UserData;

    private static final String UsrDat = "fokkÞorgeir";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);

        UserData = getIntent().getStringExtra(UsrDat);
    }

    public static Intent newIntent(Context packageContext, String usrData) {
        Intent i = new Intent(packageContext, GiftActivity.class);
        i.putExtra(UsrDat, usrData);
        return i;
    }
}
