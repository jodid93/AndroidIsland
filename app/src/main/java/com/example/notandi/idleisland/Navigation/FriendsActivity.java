package com.example.notandi.idleisland.Navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.example.notandi.idleisland.Database.ServerDatabaseAccess;
import com.example.notandi.idleisland.User.UserData;
import com.example.notandi.idleisland.Utils.ExpandableListAdapter;
import com.example.notandi.idleisland.R;

/**
 * Created by thorkellmani on 26/02/16.
 * This activity is responsible for showing the friends list and the pending requests list. It uses
 * a custom ExpandableListAdapter to inflate the ExpandableListView, creating an expandable
 * drop-down list for pending, friends or both.
 *
 */
public class FriendsActivity extends AppCompatActivity{

    //private String UserData;

    private static final String UsrDat = "fokkJósúa";
    private static final int GIFT = 69;
    private static final int PENDING = 70;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        String rejecter = getIntent().getStringExtra(UsrDat);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.friendExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                String childText = (String) listAdapter.getChild(groupPosition, childPosition);
                System.out.println(groupPosition);
                if (groupPosition == 1 ) {
                    Intent i = GiftActivity.newIntent(FriendsActivity.this, childText);
                    startActivityForResult(i, GIFT);
                }
                else {
                    Intent i = PendingActivity.newIntent(FriendsActivity.this, childText);
                    startActivityForResult(i, PENDING);
                }

                return false;
            }
        });




        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                System.out.println("groupPosition er " + groupPosition);
            }
        });

        // Listview Group collapsed listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });
    }
     //For now the list only contains some dummy data.
    //The listDataHeader is the name of the list, either pending or friends.
    //listDataChild contains the entries in the lists.
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Pending Friend Requests");
        listDataHeader.add("Friend List");

        // Adding child data
        List<String> Pending = new ArrayList<String>();
        //TODO: get pending list
        String currUser = UserData.getInstance(null).getUserName();
        ServerDatabaseAccess sDB = ServerDatabaseAccess.getInstance();

        String[] list = sDB.getPendingListSync( currUser );
        if( list!=null){
            for(Object o : list){
                Log.d("PENDING LIST", "Friend -> "+o.toString());
            }
        }

        Pending.add("Þorgeir");
        Pending.add("Algjör Pedo");

        List<String> Friends = new ArrayList<String>();
        //TODO: get friends

        Friends.add("Jósúa");
        Friends.add("Annar Pedo");
        Friends.add("Þunglyndi");

        listDataChild.put(listDataHeader.get(0), Pending); // Header, Child data
        listDataChild.put(listDataHeader.get(1), Friends);
    }

    public static Intent newIntent(Context packageContext, String usrData){
        Intent i = new Intent(packageContext, FriendsActivity.class);
        i.putExtra(UsrDat, usrData);
        return i;
    }
}
