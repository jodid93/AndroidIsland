package com.example.notandi.idleisland;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Main extends AppCompatActivity {

    private Button mLogInButton;
    private Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLogInButton = (Button) findViewById(R.id.LogIn_button);
        mLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Main.this, R.string.message, Toast.LENGTH_SHORT).show();
            }
        });
        mRegisterButton = (Button) findViewById(R.id.Register_button);
    }
}
