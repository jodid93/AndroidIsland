package com.example.notandi.idleisland.Navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notandi.idleisland.Database.ServerDatabaseAccess;
import com.example.notandi.idleisland.R;
import com.example.notandi.idleisland.User.UserData;

import java.util.ArrayList;
import java.util.List;

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


                // Adding child data
                ArrayList<String> globalHighScores = new ArrayList<String>();

                //SERVER STUFF
                //String currUser = com.example.notandi.idleisland.User.UserData.getInstance(null).getUserName();
                ServerDatabaseAccess sDB = ServerDatabaseAccess.getInstance();
                String[] highScoreArray = sDB.getHighScoresSync();
                if( highScoreArray.length > 0 && !highScoreArray[0].matches("") ){
                    insertToList(globalHighScores,highScoreArray);
                } else {
                    //TODO: create dummy pending that is disable for clicking
                    globalHighScores.add("No network connection");
                }

                //Dummy high scores for testing purposes.
                //ArrayList<String> highScoreList = new ArrayList<String>();
                //highScoreList.add("Bob");
                //highScoreList.add("Jim");
                //highScoreList.add("JimBob");

                listView.setAdapter(new ArrayAdapter<>(HighscoreActivity.this, R.layout.list_item, globalHighScores));


                Toast.makeText(HighscoreActivity.this, "Load Global", Toast.LENGTH_SHORT ).show();
            }
        });

        mFriendsScoreButton = (Button) findViewById(R.id.high_friends);
        mFriendsScoreButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Adding child data
                ArrayList<String> friendHighScores = new ArrayList<String>();

                //SERVER STUFF
                String currUser = com.example.notandi.idleisland.User.UserData.getInstance(null).getUserName();
                ServerDatabaseAccess sDB = ServerDatabaseAccess.getInstance();
                String[] highScoreArray = sDB.getFriendHighScoresSync(currUser);
                if( highScoreArray.length > 0 && !highScoreArray[0].matches("") ){
                    insertToList(friendHighScores,highScoreArray);
                } else {
                    //TODO: create dummy pending that is disable for clicking
                    friendHighScores.add("     You have no friend :/");
                }

                //Dummy high scores for testing purposes.
                //ArrayList<String> highScoreList = new ArrayList<String>();
                //highScoreList.add("Þorgeir");
                //highScoreList.add("Jósúa");
                //highScoreList.add("Máni");

                listView.setAdapter(new ArrayAdapter<>(HighscoreActivity.this, R.layout.list_item, friendHighScores));

                Toast.makeText(HighscoreActivity.this, "Load Friends", Toast.LENGTH_SHORT).show();
            }
        });

        UserData = getIntent().getStringExtra(UsrDat);
    }

    private void insertToList(List<String> list, String[] str){
        // first item is equal null, so i=1
        for(int i=1; i<str.length; i=i+2){
            Log.i("i", String.valueOf(i));
            String space ="    ";
            list.add( space+str[i]+space+space+str[i+1] );
        }
    }

    public static Intent newIntent(Context packageContext, String usrData){
        Intent i = new Intent(packageContext, HighscoreActivity.class);
        i.putExtra(UsrDat, usrData);
        return i;
    }
}

