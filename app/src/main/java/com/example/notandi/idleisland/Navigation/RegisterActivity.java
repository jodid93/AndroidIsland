package com.example.notandi.idleisland.Navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notandi.idleisland.Database.DatabaseHelper;
import com.example.notandi.idleisland.Database.ServerDatabaseAccess;
import com.example.notandi.idleisland.R;
import com.example.notandi.idleisland.User.UserData;
import com.example.notandi.idleisland.Utils.NetworkUtil;

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
                DatabaseHelper lDB = DatabaseHelper.getInstance(RegisterActivity.this);
                ServerDatabaseAccess sDB = ServerDatabaseAccess.getInstance();

                String userName = mRegisterInputName.getText().toString();
                String password = mRegisterInputPass.getText().toString();

                if( userName.equals("") || password.equals("")  ){
                    Toast.makeText(RegisterActivity.this,
                            R.string.register_error_message_2,
                            Toast.LENGTH_SHORT).show();
                } else if (NetworkUtil.isOnline(RegisterActivity.this)) {
                    if (!sDB.userNameExistsSync(userName)) {
                        Log.i("REGISTER", "Server: the user name " + userName + "is not taken ");

                        UserData userData = UserData.getInstance(userName);
                        String sUserData = userData.toJSONString();
                        sDB.registerAsync(userName, password, sUserData);
                        Log.i("REGISTER", "User should now benn stored in server");
                        String d = sDB.getUserDataSync(userName);
                        Log.i("REGISTER", "Get userdata from user " + userName + " -> " + d);
                        lDB.createNewUser(userName, password);
                        toMenu(sUserData);
                    } else {
                        Log.i("REGISTER", "Server: the user name " + userName + "is taken");
                        Toast.makeText(RegisterActivity.this,
                                R.string.register_error_message_1,
                                Toast.LENGTH_SHORT).show();
                    }
                } else if (!lDB.userNameExists(userName)) {
                    Log.i("REGISTER A USER", "Trying to create New User " + userName + " with password " + password);
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
                } else {
                    Log.i("REGISTER", "The user name doesn't exist in local database" +
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
        RegisterActivity.this.finish();
    }

    private void toMenu(String userData){
        Intent i = MenuActivity.newIntent(RegisterActivity.this, userData );
        startActivityForResult(i, ENTER_GAME);
    }
}
