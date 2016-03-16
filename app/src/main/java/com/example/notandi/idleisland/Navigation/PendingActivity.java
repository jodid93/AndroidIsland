package com.example.notandi.idleisland.Navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notandi.idleisland.Database.ServerDatabaseAccess;
import com.example.notandi.idleisland.R;
import com.example.notandi.idleisland.User.UserData;

/**
 * Created by thorkellmani on 05/03/16.
 * This Activity handles the pending friend requests from other users. When you have a pending
 * friend request (the database part is yet to be implemented) the user has a choice to either
 * reject he request or accept it. For now the code only shows which button the user has pressed.
 */
public class PendingActivity extends AppCompatActivity {


    private static final String UsrDat = "fokkÞorgeir";
    Button mAcceptButton;
    Button mRejectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);

        mAcceptButton = (Button) findViewById(R.id.accept);
        mRejectButton = (Button) findViewById(R.id.reject);

        //When a user either accepts or rejects a request the activity is closed by using .finish();

        mAcceptButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(PendingActivity.this, "Accepted friend request", Toast.LENGTH_SHORT ).show();

                ServerDatabaseAccess sDB = ServerDatabaseAccess.getInstance();

                //The current user
                String accepter = UserData.getInstance(null).getUserName();
                String requester = getIntent().getStringExtra(UsrDat);
                sDB.addFriendAsync(accepter, requester);

                PendingActivity.this.finish();
            }
        });

        mRejectButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(PendingActivity.this, "Rejected friend request", Toast.LENGTH_SHORT ).show();

                ServerDatabaseAccess sDB = ServerDatabaseAccess.getInstance();

                //The current user
                String rejecter = UserData.getInstance(null).getUserName();
                String requester = getIntent().getStringExtra(UsrDat);
                sDB.rejectFriendRequestAsync(rejecter, requester);

                PendingActivity.this.finish();
            }
        });

        String receiver = getIntent().getStringExtra(UsrDat);
        TextView friend = (TextView) findViewById(R.id.pending_text);
        friend.setText(receiver + "'s");
    }

    public static Intent newIntent(Context packageContext, String usrData) {
        Intent i = new Intent(packageContext, PendingActivity.class);
        i.putExtra(UsrDat, usrData);
        return i;
    }
}