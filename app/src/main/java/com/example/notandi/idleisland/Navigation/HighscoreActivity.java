package com.example.notandi.idleisland.Navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notandi.idleisland.R;

import java.util.ArrayList;

/**
 * Created by thorkellmani on 25/02/16.
 * This activity is responsible for enabling users to view the high scores table, either displaying
 * only their friends or the global high score table.
 */
public class HighscoreActivity extends AppCompatActivity{

    Button mGlobalScoreButton;
    Button mFriendsScoreButton;
    ListView listView;
    TextView listItem;

    private String UserData;

    private static final String UsrDat = "idleisland.userData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        mGlobalScoreButton = (Button) findViewById(R.id.high_global);
        listView = (ListView) findViewById(R.id.listview1);

        mGlobalScoreButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Dummy high scores for testing purposes.
                ArrayList<String> highScoreList = new ArrayList<String>();
                highScoreList.add("Bob");
                highScoreList.add("Jim");
                highScoreList.add("JimBob");
                listView.setAdapter(new ArrayAdapter<>(HighscoreActivity.this, R.layout.list_item, highScoreList));


                Toast.makeText(HighscoreActivity.this, "Load Global", Toast.LENGTH_SHORT ).show();
            }
        });

        mFriendsScoreButton = (Button) findViewById(R.id.high_friends);
        mFriendsScoreButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Dummy high scores for testing purposes.
                ArrayList<String> highScoreList = new ArrayList<String>();
                highScoreList.add("Þorgeir");
                highScoreList.add("Jósúa");
                highScoreList.add("Máni");
                listView.setAdapter(new ArrayAdapter<>(HighscoreActivity.this, R.layout.list_item, highScoreList));

                Toast.makeText(HighscoreActivity.this, "Load Friends", Toast.LENGTH_SHORT).show();
            }
        });

        UserData = getIntent().getStringExtra(UsrDat);
    }

    public static Intent newIntent(Context packageContext, String usrData){
        Intent i = new Intent(packageContext, HighscoreActivity.class);
        i.putExtra(UsrDat, usrData);
        return i;
    }
}

