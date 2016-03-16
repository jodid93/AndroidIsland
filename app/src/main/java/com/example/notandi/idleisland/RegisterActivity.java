package com.example.notandi.idleisland;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by thorgeir on 15.3.2016.
 */
public class RegisterActivity extends AppCompatActivity{

    //private String s_UserData;
    //public UserData userData;
    private static final String UsrDat = "idleisland.userdata";

    private Button mBack;
    private Button mRegister;
    private EditText mRegisterInputName;
    private EditText mRegisterInputPass;

    private static final int ENTER_GAME = 0;
    private static final int ENTER_LOGIN = 1;

    public static Intent newIntent(Context packageContext){
        Intent i = new Intent(packageContext, RegisterActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        intiButtons();
    }

    private void intiButtons(){

        mRegisterInputName = (EditText) findViewById(R.id.register_name);
        mRegisterInputPass = (EditText) findViewById(R.id.register_password);

        mRegister = (Button) findViewById(R.id.register_button);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper lDB=DatabaseHelper.getInstance(RegisterActivity.this);
                ServerDatabaseAccess sDB=ServerDatabaseAccess.getInstance();

                String userName = mRegisterInputName.getText().toString();
                String password = mRegisterInputPass.getText().toString();

                if( Util.isOnline(RegisterActivity.this) ) {
                    if (!sDB.userNameExistsSync(userName)) {
                        Log.i("REGISTER", "Server: the user name " + userName + "is not taken ");

                        UserData userData = UserData.getInstance(userName);
                        String sUserData = userData.toJSONString();
                        sDB.registerAsync(userName, password, sUserData);
                        Log.i("REGISTER", "User should now benn stored in server");
                        String d = sDB.getUserDataSync( userName );
                        Log.i("REGISTER", "Get userdata from user "+userName+" -> "+d);

                        toMenu(sUserData);
                    } else {
                        Log.i("REGISTER", "Server: the user name " + userName + "is taken");
                        Toast.makeText(RegisterActivity.this,
                                R.string.register_error_message_1,
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else if( !lDB.userNameExists(userName) ){
                    Log.i("REGISTER A USER","Trying to create New User "+userName+" with password "+password);
                    lDB.createNewUser(userName, password);
                    toLogin();
                    //TODO: create new column in localdatabase,
                    //      wich contains values "DUMMY" or "REAL".
                    //      DUMMY ->represents that next time user is
                    //              online, this dummy user tries to
                    //              register on the server.
                    //      REAL  ->represents that user is okay, still
                    //              need to check if this user in the server
                    //              has change
                } else{
                    Log.i("REGISTER","The user name doesn't exist in local database"+
                    " and your device is not connect to the internet");
                    toLogin();
                    Toast.makeText(RegisterActivity.this,
                            R.string.register_error_message_1,
                            Toast.LENGTH_SHORT).show();
                }

            }
        });


        mBack = (Button) findViewById(R.id.mBackToLogin);
        mBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                toLogin();
            }
        });
    }

    private void toLogin(){
        Intent i = Main.newIntent(RegisterActivity.this);
        startActivityForResult(i, ENTER_LOGIN);
    }

    private void toMenu(String userData){
        Intent i = MenuActivity.newIntent(RegisterActivity.this, userData );
        startActivityForResult(i, ENTER_GAME);
    }
}
