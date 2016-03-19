package com.example.notandi.idleisland.Navigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.notandi.idleisland.Game.GameEngine;
import com.example.notandi.idleisland.R;
import com.example.notandi.idleisland.User.UserData;

/**
 * Created by Notandi on 16.2.2016.
 */
public class MenuActivity extends AppCompatActivity {

    private Button mPlayButton;
    private Button mHighscoreButton;
    private Button mFriendsButton;
    private Button mLogOutButton;

    private com.example.notandi.idleisland.User.UserData dummyUD = com.example.notandi.idleisland.User.UserData.getInstance("hannes");

    private static final int PLAY_GAME = 666;
    private static final int HIGHSCORE = 1337;
    private static final int FRIENDS = 4;

    private static final String UsrDat = "idleisland.userdata";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mPlayButton = (Button) findViewById(R.id.Play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = GameEngine.newIntent(MenuActivity.this, null);
                startActivityForResult(i, PLAY_GAME);


                //Toast.makeText(MenuActivity.this, R.string.message, Toast.LENGTH_SHORT ).show();
            }
        });

        mHighscoreButton = (Button) findViewById(R.id.Highscore_button);
        mHighscoreButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = HighscoreActivity.newIntent(MenuActivity.this, null);
                startActivityForResult(i, HIGHSCORE);
            }
        });

        mLogOutButton = (Button) findViewById(R.id.logout_button);
        mLogOutButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                SharedPreferences sp=getSharedPreferences("Login", 0);
                SharedPreferences.Editor Ed=sp.edit();
                Ed.putString("userName", "");
                Ed.putString("Password", "");
                Ed.commit();

                UserData.clearUserData();
                MenuActivity.this.finish();
            }
        });

        mFriendsButton = (Button) findViewById(R.id.Friends_button);
        mFriendsButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = FriendsActivity.newIntent(MenuActivity.this, null);
                startActivityForResult(i, FRIENDS);
            }
        });
    }

    public static Intent newIntent(Context packageContext, String usrData){
        Intent i = new Intent(packageContext, MenuActivity.class);
        i.putExtra(UsrDat,usrData);
        return i;
    }


}
