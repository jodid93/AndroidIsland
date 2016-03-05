package com.example.notandi.idleisland;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        s_UserData = getIntent().getStringExtra(UsrDat);

        //turn title off


        /*/set to full screen*/

        this.userData = new UserData(s_UserData);
        this.calculator = new Calculator();



        this.idleIsland = new IdleIsland[]{new IdleIsland(this, this, this.calculator, this.userData, 0, createSprites(0)),
                                            new IdleIsland(this, this, this.calculator, this.userData, 1, createSprites(1))};
        setContentView(this.idleIsland[this.level]);
    }


    private Sprite[][] createSprites(int level){
        Sprite[][] sprites = new Sprite[4][3];
        //sprite takes in arguments (bitmap, number_of_frames, frameWidth, frameHeight, X_pos, Y_pos, animationSpeed)

        if(level == 0){

            //initialize sprites for level 1
            //item 1
            sprites[0][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.kall_animation_upgrade_1),
                    5, 246, 244, 50, 50, 1000, true );

            sprites[0][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.kall_animation_upgrade_2),
                    5, 246, 244, 50, 50, 800, true );

            sprites[0][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.kall_animation_upgrade_3),
                    5, 246, 244, 50, 50, 600, true );

            //item 2
            sprites[1][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.veidistong_animation_upgrade_1),
                    18, 127, 104, 50, 50, 1000, true );

            sprites[1][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.veidistong_animation_upgrade_2),
                    18, 127, 200, 50, 50, 800, true );

            sprites[1][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.veidistong_animation_upgrade_3),
                    18, 127, 200, 50, 50, 600, true );

            //item 3
            sprites[2][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.bird_animation_upgrade_1),
                    22, 200, 400, 50, 50, 1000 , true);

            sprites[2][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.bird_animation_upgrade_2),
                    22, 200, 400, 50, 50, 800 , true);

            sprites[2][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.bird_animation_upgrade_3),
                    22, 200, 400, 50, 50, 600 , true);

            //base animation
            sprites[3][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.kall_animation),
                    5, 247, 242, 50, 50, 200, false );

            sprites[3][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.kall_animation),
                    5, 247, 242, 50, 50, 200 , false);

            sprites[3][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.kall_animation),
                    5, 247, 242, 50, 50, 200 , false);

        }else if(level == 1){

            //initialize sprites for level 2
            //item 1
            sprites[0][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.molekall_animation_upgrade_1),
                    5, 246, 243, 50, 50, 1000, true );

            sprites[0][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.molekall_animation_upgrade_2),
                    5, 246, 243, 50, 50, 800, true );

            sprites[0][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.molekall_animation_upgrade_3),
                    5, 245, 243, 50, 50, 600, true );
            //item 2
            sprites[1][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.mole_animation_upgrade_1),
                    20, 38, 46, 50, 50, 1000, true );

            sprites[1][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.mole_animation_upgrade_2),
                    20, 38, 46, 50, 50, 800, true );

            sprites[1][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.mole_animation_upgrade_3),
                    20, 38, 44, 50, 50, 600, true );
            //item 3
            sprites[2][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.miner_animation_upgrade_1),
                    10, 70, 65, 50, 50, 1000, true );

            sprites[2][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.miner_animation_upgrade_2),
                    10, 69, 63, 50, 50, 800, true );

            sprites[2][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.miner_animation_upgrade_3),
                    10, 69, 63, 50, 50, 600, true );
            //base animation
            sprites[3][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.molekall_animation),
                    1, 449, 241, 50, 50, 1000, false );

            sprites[3][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.molekall_animation),
                    1, 449, 241, 50, 50, 1000, false );

            sprites[3][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.molekall_animation),
                    1, 449, 241, 50, 50, 1000, false );
        }

        return sprites;
    }

    public void draw(Canvas canvas){
        this.idleIsland[this.level].draw(canvas);
    }

    public void update(double dt){

        this.idleIsland[this.level].update(dt);
    }
}