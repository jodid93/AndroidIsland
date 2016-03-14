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

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        s_UserData = getIntent().getStringExtra(UsrDat);
        this.userData = UserData.getInstance(s_UserData);
        this.userData.printUpgrades();
        this.userData.setUserName("abudabi");

        this.calculator = new Calculator();
        this.idleIsland = new IdleIsland[]{new IdleIsland(this, this, this.calculator, this.userData, 0, createSprites(0)),
                                           new IdleIsland(this, this, this.calculator, this.userData, 1, createSprites(1))};
        this.game = new FrameLayout(this);
        this.GameButtons = new RelativeLayout(this);


        Button upgradeMenu = new Button(this);
        upgradeMenu.setText("Upgrade menu");
        upgradeMenu.setId(R.id.upgradeButton);
        upgradeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        Button exit = new Button(this);
        exit.setText("Exit");
        exit.setId(R.id.upgradeButton);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                idleIsland[level].kill();
                idleIsland[level+1].kill();
                Intent i = MenuActivity.newIntent(GameEngine.this, "hannes");
                startActivityForResult(i, 0);
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
    private void deb(int i, int j){
        System.out.println("loadad: ["+i+"]["+j+"]");
    }

    private Sprite[][] createSprites(int level) {
        Sprite[][] sprites = new Sprite[4][3];
        //sprite takes in arguments (bitmap, number_of_frames, frameWidth, frameHeight, X_pos, Y_pos, animationSpeed)
        int [][] upgrades1 = this.userData.getUpgrades(0).getUpgrades();
        int [][] upgrades2 = this.userData.getUpgrades(1).getUpgrades();
        if (level == 0) {

            if(upgrades1[0][2] == 2){
                sprites[0][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.kall_animation_upgrade_3),
                        5, 246, 244, 50, 50, 600, true);
                deb(0,2);
            }else if(upgrades1[0][1] == 2){
                sprites[0][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.kall_animation_upgrade_2),
                        5, 246, 244, 50, 50, 800, true);
                deb(0,1);
            }else if(upgrades1[0][0] == 2){
                sprites[0][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.kall_animation_upgrade_1),
                        5, 246, 244, 50, 50, 1000, true);
                deb(0,0);
            }else if(upgrades1[0][0] == 1){
                deb(-1,-1);
                sprites[3][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.kall_animation),

                        5, 247, 242, 50, 50, 1000, false );

                sprites[3][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.kall_animation),
                        5, 247, 242, 50, 50, 1000 , false);

                sprites[3][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.kall_animation),
                        5, 247, 242, 50, 50, 1000 , false);
            }

            if(upgrades1[1][2] == 2){
                sprites[1][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.veidistong_animation_upgrade_3),
                        18, 127, 200, 50, 50, 600, true);
                deb(1,2);
            }else if(upgrades1[1][1] == 2){
                sprites[1][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.veidistong_animation_upgrade_2),
                        18, 127, 200, 50, 50, 800, true);
                deb(1,1);
            }else if(upgrades1[1][0] == 2){
                sprites[1][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.veidistong_animation_upgrade_1),
                        18, 127, 104, 50, 50, 1000, true);
                deb(1,0);
            }

            if(upgrades1[2][2] == 2){
                sprites[2][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.bird_animation_upgrade_3),
                        22, 200, 400, 50, 50, 600, true);
                deb(2,2);
            }else if(upgrades1[2][1] == 2){
                sprites[2][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.bird_animation_upgrade_2),
                        22, 200, 400, 50, 50, 800, true);
                deb(2,1);
            }else if(upgrades1[2][0] == 2){
                sprites[2][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.bird_animation_upgrade_1),
                        22, 200, 400, 50, 50, 1000, true);
                deb(2,0);
            }

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

                sprites[3][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.molekall_animation),
                        1, 449, 241, 50, 50, 1000, false);

                sprites[3][1] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.molekall_animation),
                        1, 449, 241, 50, 50, 1000, false);

                sprites[3][2] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.molekall_animation),
                        1, 449, 241, 50, 50, 1000, false);
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

    public void draw(Canvas canvas) {
        this.idleIsland[this.level].draw(canvas);
    }

    public void update(double dt) {
        this.idleIsland[this.level].update(dt);
    }

}