package com.example.notandi.idleisland.Navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notandi.idleisland.R;

/**
 * Created by thorkellmani on 05/03/16.
 */
public class PendingActivity extends AppCompatActivity {

    private String UserData;

    private static final String UsrDat = "fokkÞorgeir";
    Button mAcceptButton;
    Button mRejectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);

        mAcceptButton = (Button) findViewById(R.id.accept);
        mRejectButton = (Button) findViewById(R.id.reject);

        mAcceptButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(PendingActivity.this, "Accepted friend request", Toast.LENGTH_SHORT ).show();
                PendingActivity.this.finish();
            }
        });

        mRejectButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(PendingActivity.this, "Rejected friend request", Toast.LENGTH_SHORT ).show();
                PendingActivity.this.finish();
            }
        });

        UserData = getIntent().getStringExtra(UsrDat);
        TextView friend = (TextView) findViewById(R.id.pending_text);
        friend.setText(UserData);
    }

    public static Intent newIntent(Context packageContext, String usrData) {
        Intent i = new Intent(packageContext, PendingActivity.class);
        i.putExtra(UsrDat, usrData);
        return i;
    }
}