package com.example.notandi.idleisland;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by thorkellmani on 05/03/16.
 */
public class GiftActivity extends AppCompatActivity {

    private String UserData;

    private static final String UsrDat = "fokkJósúa";
    private static final int FRIENDS = 4;
    Button mGiftButton;
    private RadioGroup radioGroup;
    private static final int RB1 = 2131492954;
    private static final int RB2 = 2131492955;
    private static final int RB3 = 2131492956;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);

        mGiftButton = (Button) findViewById(R.id.gift_button);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        mGiftButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int percent = radioGroup.getCheckedRadioButtonId();
                switch (percent) {
                    case RB1:
                        Toast.makeText(GiftActivity.this, "Gifted 10%", Toast.LENGTH_SHORT).show();
                        break;
                    case RB2:
                        Toast.makeText(GiftActivity.this, "Gifted 25%", Toast.LENGTH_SHORT).show();
                        break;
                    case RB3:
                        Toast.makeText(GiftActivity.this, "Gifted 50%", Toast.LENGTH_SHORT).show();
                        break;
                }
                Intent i = FriendsActivity.newIntent(GiftActivity.this, null);
                startActivityForResult(i, FRIENDS);
            }
        });

        UserData = getIntent().getStringExtra(UsrDat);
        TextView friend = (TextView) findViewById(R.id.textView3);
        friend.setText(UserData);
    }

    public static Intent newIntent(Context packageContext, String usrData) {
        Intent i = new Intent(packageContext, GiftActivity.class);
        i.putExtra(UsrDat, usrData);
        return i;
    }
}