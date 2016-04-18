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

        UserData.clearUserData();

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

                Log.i("REGISTER", "username ->" + userName);
                Log.i("REGISTER", "password ->" + password);

                if( userName.equals("") || password.equals("")  ){
                    Toast.makeText(Main.this,
                            R.string.register_error_message_2,
                            Toast.LENGTH_SHORT).show();
                }
                // Check if user exist in local database
                else if( lDB.isValid(userName, password) ){
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

    @Override
    protected void onRestart() {
        super.onRestart();
        UserData.clearUserData();
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


    public void getServerUserData( String userName ){
        ServerDatabaseAccess sDB = ServerDatabaseAccess.getInstance();
        String userData = sDB.getUserDataSync(userName);// UserData.getInstance("Mani");
        Log.i("GET SERVER USER",userData);
        UserData.convertStringToUserData(userData); //getUserDataFromJSON(userData);
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

            DB.getUserData(userName);
            lUserData = UserData.getInstance(userName);

            if( lUserData==null ){
                return UserData.getInstance("Mani");
            }else{
                //TODO: get UserData from the server.
                //UserData onlineUserData = UserData.getInstance("Mani");

                getServerUserData( userName );
                UserData oUserData = UserData.getInstance(userName);

                long localTimes = lUserData.getTimestamp();
                long onlineTimes = oUserData.getTimestamp();

                if( localTimes > onlineTimes ){
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
            DB.getUserData(userName);
            lUserData = UserData.getInstance(userName);
        }
        return lUserData;
    }
}