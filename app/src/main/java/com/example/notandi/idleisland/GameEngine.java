package com.example.notandi.idleisland;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class GameEngine extends AppCompatActivity {

    private String s_UserData;
    private IdleIsland[] idleIsland;
    private FrameLayout game;// Sort of "holder" for everything we are placing
    private RelativeLayout GameButtons;//Holder for the buttons

    private Calculator calculator;
    private UserData userData;
    private int level = 0;
    private ImageButton mUpgradeButton;

    private static final String UsrDat = "idleisland.userdata";
    private static final int BUY_UPGRADES = 879;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    public static Intent newIntent(Context packageContext, String usrData) {
        Intent i = new Intent(packageContext, GameEngine.class);
        i.putExtra(UsrDat, usrData);
        return i;
    }

    public void openUpgradeMenu(View view){
        Intent i = UpgradeMenu.newIntent(GameEngine.this, s_UserData);
        startActivityForResult(i, BUY_UPGRADES);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        /*mUpgradeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Intent i = UpgradeMenu.newIntent(GameEngine.this, s_UserData);
                startActivityForResult(i, BUY_UPGRADES);


                //Toast.makeText(MenuActivity.this, R.string.message, Toast.LENGTH_SHORT ).show();
            }
        });*/

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        s_UserData = getIntent().getStringExtra(UsrDat);

        this.userData = new UserData(s_UserData);
        this.calculator = new Calculator();
        this.idleIsland = new IdleIsland[]{new IdleIsland(this, this, this.calculator, this.userData, 0, createSprites(0)),
                                           new IdleIsland(this, this, this.calculator, this.userData, 1, createSprites(1))};
        this.game = new FrameLayout(this);
        this.GameButtons = new RelativeLayout(this);


        Button butOne = new Button(this);
        butOne.setText("Upgrade menu");
        butOne.setId(R.id.upgradeButton);
        butOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = MenuActivity.newIntent(GameEngine.this, "hannes");
                startActivityForResult(i, 0);
            }
        });


        //Define the layout parameter for the button to wrap the content for both width and height
        RelativeLayout.LayoutParams b1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.FILL_PARENT);
        GameButtons.setLayoutParams(params);

        GameButtons.addView(butOne);
        b1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        b1.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        butOne.setLayoutParams(b1);

        game.addView(idleIsland[0]);
        game.addView(GameButtons);
        setContentView(game);

    }


    private Sprite[][] createSprites(int level) {
        Sprite[][] sprites = new Sprite[4][3];
        //sprite takes in arguments (bitmap, number_of_frames, frameWidth, frameHeight, X_pos, Y_pos, animationSpeed)

        if (level == 0) {

            //initialize sprites for level 1
            //item 1
            sprites[0][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.kall_animation_upgrade_1),
                    5, 246, 244, 50, 50, 1000, true);

            sprites[0][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.kall_animation_upgrade_2),
                    5, 246, 244, 50, 50, 800, true);

            sprites[0][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.kall_animation_upgrade_3),
                    5, 246, 244, 50, 50, 600, true);

            //item 2
            sprites[1][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.veidistong_animation_upgrade_1),
                    18, 127, 104, 50, 50, 1000, true);

            sprites[1][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.veidistong_animation_upgrade_2),
                    18, 127, 200, 50, 50, 800, true);

            sprites[1][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.veidistong_animation_upgrade_3),
                    18, 127, 200, 50, 50, 600, true);

            //item 3
            sprites[2][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.bird_animation_upgrade_1),
                    22, 200, 400, 50, 50, 1000, true);

            sprites[2][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.bird_animation_upgrade_2),
                    22, 200, 400, 50, 50, 800, true);

            sprites[2][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.bird_animation_upgrade_3),
                    22, 200, 400, 50, 50, 600, true);

            //base animation
            sprites[3][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.kall_animation),
                    5, 247, 242, 50, 50, 20, false);

            sprites[3][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.kall_animation),
                    5, 247, 242, 50, 50, 20, false);

            sprites[3][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.kall_animation),
                    5, 247, 242, 50, 50, 20, false);

        } else if (level == 1) {

            //initialize sprites for level 2
            //item 1
            sprites[0][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.molekall_animation_upgrade_1),
                    5, 246, 243, 50, 50, 1000, true);

            sprites[0][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.molekall_animation_upgrade_2),
                    5, 246, 243, 50, 50, 800, true);

            sprites[0][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.molekall_animation_upgrade_3),
                    5, 245, 243, 50, 50, 600, true);
            //item 2
            sprites[1][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.mole_animation_upgrade_1),
                    20, 38, 46, 50, 50, 1000, true);

            sprites[1][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.mole_animation_upgrade_2),
                    20, 38, 46, 50, 50, 800, true);

            sprites[1][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.mole_animation_upgrade_3),
                    20, 38, 44, 50, 50, 600, true);
            //item 3
            sprites[2][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.miner_animation_upgrade_1),
                    10, 70, 65, 50, 50, 1000, true);

            sprites[2][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.miner_animation_upgrade_2),
                    10, 69, 63, 50, 50, 800, true);

            sprites[2][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.miner_animation_upgrade_3),
                    10, 69, 63, 50, 50, 600, true);
            //base animation
            sprites[3][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.molekall_animation),
                    1, 449, 241, 50, 50, 1000, false);

            sprites[3][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.molekall_animation),
                    1, 449, 241, 50, 50, 1000, false);

            sprites[3][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.molekall_animation),
                    1, 449, 241, 50, 50, 1000, false);
        }

        return sprites;
    }

    public void draw(Canvas canvas) {
        this.idleIsland[this.level].draw(canvas);
    }

    public void update(double dt) {
        System.out.print(".");
        this.idleIsland[this.level].update(dt);
    }

}