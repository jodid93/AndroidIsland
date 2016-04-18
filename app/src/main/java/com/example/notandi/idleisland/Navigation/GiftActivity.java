package com.example.notandi.idleisland.Navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notandi.idleisland.Database.DatabaseHelper;
import com.example.notandi.idleisland.Database.ServerDatabaseAccess;
import com.example.notandi.idleisland.R;
import com.example.notandi.idleisland.User.UserData;
import com.example.notandi.idleisland.User.UserData;

/**
 * Created by thorkellmani on 05/03/16.
 * This activity is responsible for enabling users to send their friends a portion of their coconuts
 * As of now it only displays a toast for how much you want to send your friend.
 */
public class GiftActivity extends AppCompatActivity {

    private String receiver;

    private static final String UsrDat = "fokkJósúa";
    private static final int FRIENDS = 4;
    private Button mGiftButton;
    private Button mBackButton;
    private RadioGroup radioGroup;
    private static final int RB1 = 2131492953; //These are the id's which are retrieved by using the
    private static final int RB2 = 2131492954; //.getCheckedRadioButtonId(); to know which radio
    private static final int RB3 = 2131492955; // button is currently checked.

    // gift represented in percent (%)
    private static final int gift1 = 10;
    private static final int gift2 = 25;
    private static final int gift3 = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);

        mGiftButton = (Button) findViewById(R.id.gift_button);
        mBackButton = (Button) findViewById(R.id.cancel_gift);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        receiver = getIntent().getStringExtra(UsrDat);


        mBackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GiftActivity.this.finish();
            }
        });

        mGiftButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int radioID = radioGroup.getCheckedRadioButtonId();
                int gift = 0;
                switch (radioID) {
                    case RB1:
                        gift = gift1;
                        Toast.makeText(GiftActivity.this, "Gifted 10%", Toast.LENGTH_SHORT).show();
                        break;
                    case RB2:
                        gift = gift2;
                        Toast.makeText(GiftActivity.this, "Gifted 25%", Toast.LENGTH_SHORT).show();
                        break;
                    case RB3:
                        gift = gift3;
                        Toast.makeText(GiftActivity.this, "Gifted 50%", Toast.LENGTH_SHORT).show();
                        break;
                }
                String currUser = UserData.getInstance(null).getUserName();

                // get Database access instance
                ServerDatabaseAccess sDB = ServerDatabaseAccess.getInstance();
                DatabaseHelper lDB = DatabaseHelper.getInstance(GiftActivity.this);
                // Calculate and store the gift for the receiver user
                // and the current user
                sDB.addGiftAsync(currUser, receiver, gift);
                UserData currentUserData = UserData.getInstance( currUser );
                currentUserData.setCurrencyGift( gift );
                lDB.insertUserData(currUser, currentUserData);
                GiftActivity.this.finish();
            }
        });

        TextView friend = (TextView) findViewById(R.id.textView2);
        friend.setText("GIFT COCONUTS TO " + receiver);
    }

    public static Intent newIntent(Context packageContext, String usrData) {
        Intent i = new Intent(packageContext, GiftActivity.class);
        i.putExtra(UsrDat, usrData);
        return i;
    }
}