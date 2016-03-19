package com.example.notandi.idleisland.Game;



import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.notandi.idleisland.Database.DatabaseHelper;
import com.example.notandi.idleisland.R;
import com.example.notandi.idleisland.User.UserData;

import java.security.spec.ECField;

/*
    class that contains the activity for the game itself

    DRAWBACK:
            This class holds the sceleton for an implementation for a second level but it has not yet
            been put to use and so there is a lot of code that is never used in practice.
            Also the createSprites method seems really messy but that is only because each Sprite
            has it's specific values so making another method to initialize each sprite seemed like a
            waste of time.
            Further more we have two buttons on this display that are hardcoded here, but that is
            because we are drawing them onto a canvas and not a onto a view so this was the only way
            I could find to do it :/

 */
public class GameEngine extends AppCompatActivity {

    private String s_UserData;
    private IdleIsland[] idleIsland; //idleIsland is a class that hold all the actions for the game
    private FrameLayout game;// Sort of "holder" for everything we are placing
    private RelativeLayout GameButtons;//Holder for the buttons

    private Calculator calculator;  //util to calculate stuff
    private UserData userData;  // userData is the Global object that contains all the relevant data for the player
    private int level = 0;  //default level value (level 1)

    private static final String UsrDat = "idleisland.userdata";
    private static final int BUY_UPGRADES = 879;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    /*
        used to get this activity from caller
     */
    public static Intent newIntent(Context packageContext, String usrData) {
        Intent i = new Intent(packageContext, GameEngine.class);
        i.putExtra(UsrDat, usrData);
        return i;
    }

    /*
        method to restart the game after you come back from upgradeMenu
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        game.removeView(idleIsland[0]);
        game.removeView(GameButtons);

        //initialize both levels for the game, see IdleIsland class for more info
        idleIsland = new IdleIsland[]{new IdleIsland(this, this, this.calculator, this.userData, 0, createSprites(0)),
                                      new IdleIsland(this, this, this.calculator, this.userData, 1, createSprites(1))};
        //load level 1
        game.addView(idleIsland[0]);
        game.addView(GameButtons);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //make fullscreen and take the title away
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        s_UserData = getIntent().getStringExtra(UsrDat);
        this.userData = UserData.getInstance(s_UserData);

        this.calculator = new Calculator();

        System.out.println("---------fyrir reikning--------------: " + this.userData.getTimestamp());
        int currentiCurr = this.userData.getCurrency();
        this.userData.setCurrency((int)this.calculator.calculateOfflineCurrency(this.userData.getTimestamp(), currentiCurr, this.userData.getCurrFactor()));
        this.userData.setScore(this.userData.getScore()+(int)this.calculator.calculateOfflineScore(this.userData.getTimestamp(), currentiCurr, this.userData.getCurrFactor()));

        //initialize the calculator and levels

        this.idleIsland = new IdleIsland[]{new IdleIsland(this, this, this.calculator, this.userData, 0, createSprites(0)),
                                           new IdleIsland(this, this, this.calculator, this.userData, 1, createSprites(1))};
        this.game = new FrameLayout(this);
        this.GameButtons = new RelativeLayout(this);

        //initialize the upgrade Menu button
        Button upgradeMenu = new Button(this);
        upgradeMenu.setId(R.id.upgradeButton);
        upgradeMenu.setBackgroundResource(R.drawable.game_button_upgrade);
        upgradeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //kill the game loop so it dosn't run in the background when the game shouldn't be running
                idleIsland[level].kill();
                Intent i = UpgradeMenu.newIntent(GameEngine.this, "hannes");
                startActivityForResult(i, 0);
            }
        });

        //Define the layout parameter for the button to wrap the content for both width and height
        RelativeLayout.LayoutParams b1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.FILL_PARENT);
        GameButtons.setLayoutParams(params);

        GameButtons.addView(upgradeMenu);
        b1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        b1.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        upgradeMenu.setLayoutParams(b1);

        //initialize the exit button
        Button exit = new Button(this);
        exit.setBackgroundResource(R.drawable.game_button_exit);
        exit.setId(R.id.upgradeButton);
        final UserData ud = this.userData;
        final String uName = this.userData.getUserName();
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //kill the game loop so it doesn't run in the background when the game shouldn't be running
                idleIsland[level].kill();
                idleIsland[level+1].kill();

                DatabaseHelper lDB = DatabaseHelper.getInstance(GameEngine.this);
                ud.updateTime();
                System.out.println("-----------------------: "+ud.getTimestamp());
                lDB.insertUserData(uName, ud);

                GameEngine.this.finish();

            }
        });


        //Define the layout parameter for the button to wrap the content for both width and height
        RelativeLayout.LayoutParams b2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.FILL_PARENT);
        GameButtons.setLayoutParams(params2);

        GameButtons.addView(exit);
        b2.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        b2.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        exit.setLayoutParams(b2);

        game.addView(idleIsland[0]);
        game.addView(GameButtons);

        setContentView(game);

    }

    /*
        method to get a position on the screen relative to the background

        xPos is the percentage from 0 -> screenWidth
        yPos is the perventage from 0 -> screenHeight

        the method return a relative x, y coordinate array of ints
     */
    private int[] getRelPos(double xPos, double yPos){

        //get the height and width of the screen
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        //get a scale factor for the screen according to the background image size
        final float scaleFactorX = width/540;
        final float scaleFactorY = height/584;

        return new int[] {(int)((width/scaleFactorX)*xPos) , (int)((width/scaleFactorY)*yPos)};
    }

    /*
        method that initializes the sprites to be used in the game according to what level
        we're in and the upgrades the player owns

        level is the current level of the game

        method returns a 2d Sprite array conntaining either null or a sprite object (depends on what
        upgrades the player owns)
     */
    private Sprite[][] createSprites(int level) {

        //initialize the sprite array. the first dimension is set to 4 since every level starts
        //with a base animation, other wise the array is 3*3 since we have 3 different items
        //that all have 3 different forms (upgrades)
        Sprite[][] sprites = new Sprite[4][3];

        //get the upgrade arrays from level 1 and 2 from userData
        int [][] upgrades1 = this.userData.getUpgrades(0).getUpgrades();
        int [][] upgrades2 = this.userData.getUpgrades(1).getUpgrades();

        //initialize the sprites for level 1
        if (level == 0) {

            //get the relative positions for each item that can come on to the screen
            int[] pos1 = getRelPos(0.3,0.7);
            int[] pos2 = getRelPos(0.7, 0.95);
            int[] pos3 = getRelPos(0.1,0.2);

            //the nested if loop is used to load only the latest upgrade from the item.
            //if upgrades[x][y] == 2 the upgrade has been bought and we load that sprite.
            //sprite takes in arguments (bitmap, number_of_frames, frameWidth, frameHeight, X_pos, Y_pos, animationSpeed)
            // for more info on sprites See class Sprites
            if(upgrades1[0][2] == 2){
                sprites[0][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.kall_animation_upgrade_3),
                        5, 246, 244,pos1[0],pos1[1], 600, true);
            }else if(upgrades1[0][1] == 2){
                sprites[0][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.kall_animation_upgrade_2),
                        5, 246, 244, pos1[0],pos1[1], 800, true);
            }else if(upgrades1[0][0] == 2){
                sprites[0][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.kall_animation_upgrade_1),
                        5, 246, 244, pos1[0],pos1[1], 1000, true);
            }else if(upgrades1[0][0] == 1){

                //if no upgrades have been bought then we load the base animations
                sprites[3][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.kall_animation),
                        5, 248, 242,pos1[0],pos1[1], 1000, false );

                sprites[3][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.kall_animation),
                        5, 248, 242, pos1[0],pos1[1], 1000 , false);

                sprites[3][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.kall_animation),
                        5, 248, 242, pos1[0],pos1[1], 1000 , false);
            }

            if(upgrades1[1][2] == 2){
                sprites[1][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.veidistong_animation_upgrade_3),
                        18, 128, 200, pos2[0],pos2[1]-100, 600, true);
            }else if(upgrades1[1][1] == 2){
                sprites[1][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.veidistong_animation_upgrade_2),
                        18, 128, 200,pos2[0],pos2[1]-100, 800, true);
            }else if(upgrades1[1][0] == 2){
                sprites[1][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.veidistong_animation_upgrade_1),
                        18, 128, 104, pos2[0],pos2[1], 1000, true);
            }

            if(upgrades1[2][2] == 2){
                sprites[2][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.bird_animation_upgrade_3),
                        22, 200, 400, pos3[0],pos3[1], 1000, true);
            }else if(upgrades1[2][1] == 2){
                sprites[2][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.bird_animation_upgrade_2),
                        22, 200, 400, pos3[0],pos3[1], 1500, true);
            }else if(upgrades1[2][0] == 2){
                sprites[2][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.bird_animation_upgrade_1),
                        22, 200, 400, pos3[0],pos3[1], 2000, true);
            }

        //initialize the sprites for level 2
        //same rules apply as for level 1
        } else if (level == 1) {

            if(upgrades2[0][2] == 2){
                sprites[0][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.molekall_animation_upgrade_3),
                        5, 245, 243, 50, 50, 600, true);
            }else if(upgrades2[0][1] == 2){
                sprites[0][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.molekall_animation_upgrade_2),
                        5, 246, 243, 50, 50, 800, true);
            }else if(upgrades2[0][0] == 2){
                sprites[0][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.molekall_animation_upgrade_1),
                        5, 246, 243, 50, 50, 1000, true);
            }else if(upgrades2[0][0] == 1){
                /*
                sprites[3][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.molekall_animation),
                        1, 449, 241, 50, 50, 1000, false);

                sprites[3][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.molekall_animation),
                        1, 449, 241, 50, 50, 1000, false);

                sprites[3][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.molekall_animation),
                        1, 449, 241, 50, 50, 1000, false);*/
            }

            if(upgrades2[1][2] == 2){
                sprites[1][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.mole_animation_upgrade_3),
                        20, 38, 44, 50, 50, 600, true);
            }else if(upgrades2[1][1] == 2){
                sprites[1][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.mole_animation_upgrade_2),
                        20, 38, 46, 50, 50, 800, true);
            }else if(upgrades2[1][0] == 2){
                sprites[1][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.mole_animation_upgrade_1),
                        20, 38, 46, 50, 50, 1000, true);
            }

            if(upgrades2[2][2] == 2){
                sprites[2][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.miner_animation_upgrade_3),
                        10, 69, 63, 50, 50, 600, true);
            }else if(upgrades2[2][1] == 2){
                sprites[2][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.miner_animation_upgrade_2),
                        10, 69, 63, 50, 50, 800, true);
            }else if(upgrades2[2][0] == 2){
                sprites[2][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.miner_animation_upgrade_1),
                        10, 70, 65, 50, 50, 1000, true);
            }
        }

        return sprites;
    }

    /*
        method to call the draw mwthod of the current level of idleIsland
        this method is called upon from the MainTread. See MainThread
     */
    public void draw(Canvas canvas) {
        this.idleIsland[this.level].draw(canvas);
    }

    /*
       method to call the update method of the current level of idleIsland
       this method is called upon from the MainTread. See MainThread
       dt is sent from mainThread and represents the delta time since the
       last iteration of the gameloop
    */
    public void update(double dt) {
        this.idleIsland[this.level].update(dt);
    }

}