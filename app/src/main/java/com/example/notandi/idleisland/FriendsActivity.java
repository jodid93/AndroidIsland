package com.example.notandi.idleisland;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by thorkellmani on 26/02/16.
 */
public class FriendsActivity extends ListActivity{

    private String UserData;

    private static final String UsrDat = "fokkJósúa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        setListAdapter(new friendsAdapter());

        UserData = getIntent().getStringExtra(UsrDat);
    }

    private class friendsAdapter extends BaseAdapter {
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
    }

    public static Intent newIntent(Context packageContext, String usrData){
        Intent i = new Intent(packageContext, FriendsActivity.class);
        i.putExtra(UsrDat, usrData);
        return i;
    }
}
