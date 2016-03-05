package com.example.notandi.idleisland;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by thorkellmani on 25/02/16.
 */
public class HighscoreActivity extends AppCompatActivity{

    Button mGlobalScoreButton;
    Button mFriendsScoreButton;
    ListView listView;

    private String UserData;

    private static final String UsrDat = "grillað helvíti maðr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        mGlobalScoreButton = (Button) findViewById(R.id.high_global);
        listView = (ListView) findViewById(R.id.listview1);


        mGlobalScoreButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Intent i = GameEngine.newIntent(MenuActivity.this, UserData);
                //startActivityForResult(i, PLAY_GAME);

                ArrayList<String> highScoreList = new ArrayList<String>();
                highScoreList.add("Bob");
                highScoreList.add("Jim");
                highScoreList.add("JimBob");
                listView.setAdapter(new ArrayAdapter<String>(HighscoreActivity.this, android.R.layout.simple_list_item_1, highScoreList));


                Toast.makeText(HighscoreActivity.this, "Load Global", Toast.LENGTH_SHORT ).show();
            }
        });

        mFriendsScoreButton = (Button) findViewById(R.id.high_friends);
        mFriendsScoreButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ArrayList<String> highScoreList = new ArrayList<String>();
                highScoreList.add("Þorgeir");
                highScoreList.add("Jósúa");
                highScoreList.add("Máni");
                listView.setAdapter(new ArrayAdapter<String>(HighscoreActivity.this, android.R.layout.simple_list_item_1, highScoreList));

                Toast.makeText(HighscoreActivity.this, "Load Friends", Toast.LENGTH_SHORT).show();
            }
        });

        UserData = getIntent().getStringExtra(UsrDat);
    }

    /**
     * Array adapter for the high scores
     */
    /*private class highScoreAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return DummiHS.USERS.length;
        }

        @Override
        public String getItem(int position) {
            return DummiHS.USERS[position];
        }

        @Override
        public long getItemId(int position) {
            return DummiHS.USERS[position].hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item, container, false);
            }

            ((TextView) convertView.findViewById(R.id.item))
                    .setText(getItem(position));
            return convertView;
        }
    }*/

    public static Intent newIntent(Context packageContext, String usrData){
        Intent i = new Intent(packageContext, HighscoreActivity.class);
        i.putExtra(UsrDat, usrData);
        return i;
    }
}

