package com.example.notandi.idleisland;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;


public class Main extends AppCompatActivity {

    private Button mLogInButton;
    private TextView mLogInInputName;
    private TextView mLogInInputPassword;
    private Button mRegisterButton;
    //private DatabaseHelper DB;
    private static final int ENTER_GAME = 0;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DatabaseHelper DB = DatabaseHelper.getInstance(this);

        //Create temporarily user named "hannes"
        String userName = "hannes";
        final User tmpUser = new User( userName );

        mLogInInputName = (EditText) findViewById(R.id.log_in_name);
        mLogInInputPassword = (EditText) findViewById(R.id.log_in_password);
        mLogInButton = (Button) findViewById(R.id.LogIn_button);
        mRegisterButton = (Button) findViewById(R.id.Register_button);

        mLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User oldUser = null;

                String userName = mLogInInputName.getText().toString();
                String password = mLogInInputPassword.getText().toString();

                if (DB.isValid(userName, password)) {
                    try {
                        oldUser = DB.getUser(userName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(Main.this, R.string.login_error_message_1, Toast.LENGTH_SHORT).show();
                }

                user = oldUser;

                Log.d("INFO", "from name input -> " + userName);

                String userdata = getUserdataString(tmpUser);
                Intent i = MenuActivity.newIntent(Main.this, userdata);
                startActivityForResult(i, ENTER_GAME);
            }
        });



        //
        // Þorgeir's test area
        //

        //ServerDatabaseAccess onlineDB = new ServerDatabaseAccess();

        //onlineDB.authorization("a", "b");

        //
        // TESTING USER AND DATABASE
        //
        //Get the database
        //DatabaseHelper DB = DatabaseHelper.getInstance(this);
        //DB.clearTable();
        //We insert the user and
        //Boolean b = DB.insertUser(tmpUser);
        //Log.d("INSERT USER RESULT", String.valueOf(b));
        //Log.d("ROW_ID ->>>",rowID.toString());
        //UserData userData = DB.getUserData();
        //UserData a = getUserdata(tmpUser);
        //Log.d("GET USERDATA"," from tmpUser -> " + getUserdataString(tmpUser));
    }


    public String getUserdataString(User user){
        return user.userDataToJSON();
    }

    public UserData getUserdata(User user){
        //TODO implementa allt database dót

        //ef online
        // Sækja online
        // Sækja offline
        // para saman timestamp
        // nýjasti timestamp verður nýjasta gildið
        // update-a userdata í timestampi sem er gamalt
        // skila user data
        //ef offline
        // Sækja offline
        // skila user data

        UserData localUserData;

        DatabaseHelper DB = DatabaseHelper.getInstance(this);

        if( isOnline() ){
            Log.d("INTENET CONNECTION", "CONNECTION: TRUE");

            localUserData = user.getUserData();

            //TODO: get UserData from the server.
            UserData onlineUserData = UserData.getInstance("Mani");

            Timestamp localTimes = localUserData.getTimestamp();
            Timestamp onlineTimes = onlineUserData.getTimestamp();

            Log.d("TIMESTAMP COMPARE", String.valueOf(localTimes.getTime()) );
            Log.d("TIMESTAMP COMPARE", String.valueOf(onlineTimes.getTime()));

            if( localTimes.after(onlineTimes) ){
                // If online timestamp is older than local timestamp
                // TODO: replace the local userdata instead of online UserData
                Log.d("TIMESTAMP COMPARE", "local time is after onlineTimes");
            } else {
                // If online timestamp is newer than local timestamp
                // TODO: replace online userdata instead of local timestamp
                Log.d("TIMESTAMP COMPARE", "local time is before onlineTimes");
            }
        } else {
            Log.d("INTENET CONNECTION","CONNECTION:FALSE");
            localUserData = DB.getUserData(user);
        }
        return localUserData;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}