package com.example.notandi.idleisland;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.Window;
import android.view.WindowManager;

public class GameEngine extends AppCompatActivity {

    private String s_UserData;
    private IdleIsland[] idleIsland;
    private Calculator calculator;
    private UserData userData;
    private int level = 0;

    private static final String UsrDat = "idleisland.userdata";


    public static Intent newIntent(Context packageContext, String usrData){
        Intent i = new Intent(packageContext, GameEngine.class);
        i.putExtra(UsrDat, usrData);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        s_UserData = getIntent().getStringExtra(UsrDat);

        //turn title off
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        this.userData = new UserData(s_UserData);
        this.calculator = new Calculator();
        this.idleIsland = new IdleIsland[]{new IdleIsland(this, this, this.calculator, this.userData, 0),new IdleIsland(this, this, this.calculator, this.userData, 1)};
        setContentView(this.idleIsland[this.level]);
    }

    public void draw(Canvas canvas){
        //this.idleIsland[this.level].draw(canvas);
    }

    public void update(double dt){
        this.idleIsland[this.level].update(dt);
    }
}