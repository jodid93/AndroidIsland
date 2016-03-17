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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

import com.example.notandi.idleisland.Database.ServerDatabaseAccess;
import com.example.notandi.idleisland.User.UserData;
import com.example.notandi.idleisland.Utils.ExpandableListAdapter;
import com.example.notandi.idleisland.R;
import com.example.notandi.idleisland.Utils.display;

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

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private Button mAddFriend;
    private EditText mAddFriendUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        String rejecter = getIntent().getStringExtra(UsrDat);

        mAddFriend = (Button) findViewById(R.id.add_friend_button);
        mAddFriendUsername = (EditText) findViewById(R.id.add_friend);

        mAddFriend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String userName = mAddFriendUsername.getText().toString();

                String currUser = UserData.getInstance(null).getUserName();
                String receiver = mAddFriendUsername.getText().toString();

                //TODO: test these conditions
                if( currUser.equals(receiver) || receiver.matches("")){
                    Toast.makeText(FriendsActivity.this, "Not valid input", Toast.LENGTH_SHORT).show();
                }else {
                    ServerDatabaseAccess sDB = ServerDatabaseAccess.getInstance();
                    if( sDB.userNameExistsSync( receiver ) ){
                        String msg= "Friend request sent to " + receiver;
                        display.info(FriendsActivity.this, msg);
                        sDB.addPendingAsync(currUser, receiver);
                    } else {
                        String msg="The user doesn't exists";
                        display.info(FriendsActivity.this, msg);
                    }
                }
            }
        });
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

        //SERVER STUFF
        String currUser = UserData.getInstance(null).getUserName();
        ServerDatabaseAccess sDB = ServerDatabaseAccess.getInstance();
        String[] list = sDB.getPendingListSync( currUser );
        if( list.length > 0 && !list[0].matches("") ){
            for(int i=0; i<list.length; i++){
                Log.i("i", String.valueOf(i));
                Pending.add(list[i]);
            }
        } else {
            //TODO: create dummy pending that is disable for clicking
            Pending.add("No friend request's");
        }

        //dummy
        //Pending.add("Þorgeir");
        //Pending.add("Algjör Pedo");

        List<String> Friends = new ArrayList<String>();

        //SERVER STUFF
        String[] friendList = sDB.getFriendListSync( currUser );
        for(int i=0; i<friendList.length; i++){
            Log.i("FRIENDLIST", "["+i+"]"+friendList[i]);
        }
        if( friendList.length > 0 && !friendList[0].matches("") ){
            for(int i=0; i<friendList.length; i++){
                Friends.add(friendList[i]);
            }
        } else{
            Friends.add("You have no friend's, you can "+
                    "add them by typing their user name "+
                    "on top of this screen :)");
        }

        //dummy
        //Friends.add("Jósúa");
        //Friends.add("Annar Pedo");
        //Friends.add("Þunglyndi");

        listDataChild.put(listDataHeader.get(0), Pending); // Header, Child data
        listDataChild.put(listDataHeader.get(1), Friends);
    }

    public static Intent newIntent(Context packageContext, String usrData){
        Intent i = new Intent(packageContext, FriendsActivity.class);
        i.putExtra(UsrDat, usrData);
        return i;
    }
}
