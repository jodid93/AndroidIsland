package com.example.notandi.idleisland;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Notandi on 16.2.2016.
 */
public class MenuActivity extends AppCompatActivity {

    private Button mPlayButton;
    private Button mHighscoreButton;
    private Button Friends;
    private Button LogOut;
    private String UserData;

    private static final int PLAY_GAME = 666;
    private static final int HIGHSCORE = 1337;
    private static final String UsrDat = "idleisland.userdata";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mPlayButton = (Button) findViewById(R.id.Play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = GameEngine.newIntent(MenuActivity.this, UserData);
                startActivityForResult(i, PLAY_GAME);


                //Toast.makeText(MenuActivity.this, R.string.message, Toast.LENGTH_SHORT ).show();
            }
        });

        mHighscoreButton = (Button) findViewById(R.id.Highscore_button);
        mHighscoreButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent i = HighscoreActivity.newIntent(MenuActivity.this, UserData);
                startActivityForResult(i, HIGHSCORE);
            }
        });

        UserData = getIntent().getStringExtra(UsrDat);
    }

    public static Intent newIntent(Context packageContext, String usrData){
        Intent i = new Intent(packageContext, MenuActivity.class);
        i.putExtra(UsrDat,usrData);
        return i;
    }


}
