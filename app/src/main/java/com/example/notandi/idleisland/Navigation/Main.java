package com.example.notandi.idleisland.Navigation;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;

//import com.google.gson.Gson;

import com.example.notandi.idleisland.Database.DatabaseHelper;
import com.example.notandi.idleisland.Database.ServerDatabaseAccess;
import com.example.notandi.idleisland.R;
import com.example.notandi.idleisland.User.User;
import com.example.notandi.idleisland.User.UserData;
import com.example.notandi.idleisland.Utils.NetworkUtil;


public class Main extends AppCompatActivity {

    private Button mLogInButton;
    private TextView mLogInInputName;
    private TextView mLogInInputPassword;
    private Button mRegisterButton;
    //private DatabaseHelper DB;
    private static final int ENTER_GAME = 0;
    private static final int ENTER_REGISTER = 1;

    //private ServerDatabaseAccess sDB = new ServerDatabaseAccess();
    //private Gson gson = new Gson();

    //private User user; //TODO:remove the user when it is possible

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //final DatabaseHelper DB = DatabaseHelper.getInstance(this);

        //Create temporarily user named "hannes"
        //String userName = "hannes";
        //final User tmpUser = new User( userName );

        mLogInInputName = (EditText) findViewById(R.id.log_in_name);
        mLogInInputPassword = (EditText) findViewById(R.id.log_in_password);
        mLogInButton = (Button) findViewById(R.id.LogIn_button);
        mRegisterButton = (Button) findViewById(R.id.Register_button);

        mLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper lDB = DatabaseHelper.getInstance(Main.this);
                ServerDatabaseAccess sDB = ServerDatabaseAccess.getInstance();
                //User oldUser = null;

                String userName = mLogInInputName.getText().toString();
                String password = mLogInInputPassword.getText().toString();

                UserData userData = null;

                // Check if user exist in local database
                if( lDB.isValid(userName, password) ){
                    //userData = lDB.getUserData(userName);
                    toMenu(userName);
                } else if( NetworkUtil.isOnline(Main.this) ){
                    String validUser = sDB.authorizationSync(userName, password);
                    if( validUser.equals("true") ) {
                        //UserData userData = sDB.getUserData();
                        String userdata = sDB.getUserDataSync(userName);
                        Log.d("VALID USER","User data is -> "+userdata);

                        toMenu(userName); //startMenu(userData);
                    } else {
                        Toast.makeText(Main.this, R.string.login_error_message_1, Toast.LENGTH_SHORT).show();
                    }
                }  else {
                    if( NetworkUtil.offlineMode == false ){
                        Toast.makeText(Main.this, R.string.login_error_message_2, Toast.LENGTH_SHORT).show();
                        Toast.makeText(Main.this, R.string.login_error_message_1, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Main.this, R.string.login_error_message_1, Toast.LENGTH_SHORT).show();
                        String message =
                                "User doesn't exist in local database "+
                                "and client is not connected to the Internet!";
                        Log.i("LOGIN INFO",message);
                    }
                }

                Log.d("INFO", "from name input -> " + userName);
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DatabaseHelper lDB = DatabaseHelper.getInstance(Main.this);
                toRegister();
                /*UserData userData = getUserdata(userName);
                Intent i = MenuActivity.newIntent(Main.this, userData.toJSONString());
                startActivityForResult(i, ENTER_GAME);*/
            }
        });

    }

    private void toRegister(){
        Intent i = RegisterActivity.newIntent(Main.this );
        startActivityForResult(i, ENTER_REGISTER);
    }

    private void toMenu(String userName){
        UserData userData = getUserdata(userName);
        Intent i = MenuActivity.newIntent(Main.this, userData.toJSONString() );
        startActivityForResult(i, ENTER_GAME);
    }


    public String getUserdataString(User user){
        return user.userDataToJSON();
    }


    public UserData getServerUserData( String userName ){
        ServerDatabaseAccess sDB = ServerDatabaseAccess.getInstance();
        String userData = sDB.getUserDataSync(userName);// UserData.getInstance("Mani");
        Log.i("GET SERVER USER",userData);
        UserData oUserData =  UserData.convertStringToUserData(userData); //getUserDataFromJSON(userData);
        return oUserData;
    }

    public UserData getUserdata(String userName){
        DatabaseHelper DB = DatabaseHelper.getInstance(this);

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

        UserData lUserData;

        if( NetworkUtil.isOnline(this) ){
            Log.d("INTENET CONNECTION", "CONNECTION: TRUE");

            lUserData = DB.getUserData(userName);

            if( lUserData==null ){
                return UserData.getInstance("Mani");
            }else{
                //TODO: get UserData from the server.
                //UserData onlineUserData = UserData.getInstance("Mani");

                UserData oUserData = getServerUserData( userName );

                Timestamp localTimes = lUserData.getTimestamp();
                Timestamp onlineTimes = oUserData.getTimestamp();

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
            }
        } else {
            Log.d("INTENET CONNECTION","CONNECTION:FALSE");
            lUserData = DB.getUserData(userName);
        }
        return lUserData;
    }

    /*public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }*/

    // Converts UserData userD to JSON String
    // and returns it
    /*public String userDataToJSON(UserData userData){
        Log.d("-- DFSJKLDSFKJL---",userData.getUserName());
        return this.gson.toJson(userData);
    }

    public UserData getUserDataFromJSON(String json) {
        Log.d("CONVERT","Taking the string \""+json+"\"");
        UserData newData = this.gson.fromJson(json, UserData.class);
        return newData;
    }*/

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, Main.class);
        return i;
    }
}