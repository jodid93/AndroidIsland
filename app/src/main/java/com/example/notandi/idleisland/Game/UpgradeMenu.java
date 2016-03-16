package com.example.notandi.idleisland.Game;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notandi.idleisland.R;
import com.example.notandi.idleisland.User.UserData;

import java.text.DecimalFormat;

/**
 * Created by josua on 5.3.2016.
 * this class implements the activity for buying upgrades and updating the upgrades display
 *
 * DRAWBACK:
 *          The initialization of the imageButtons and the unlocking of upgrades method, seems a little messy.
 *          I don't see a prettier way to do it
 */
public class UpgradeMenu extends AppCompatActivity {

    private String s_UserData;
    public UserData userData;
    private static final String UsrDat = "idleisland.userdata";

    private ImageButton[][] upgradeButtons = new ImageButton[3][3]; //the upgrade buttons
    private TextView currency;  //the currency the player has
    private Button mBack;       //the back button (goes back to the game engine)

    public static Intent newIntent(Context packageContext, String usrData){
        Intent i = new Intent(packageContext, UpgradeMenu.class);
        i.putExtra(UsrDat, usrData);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrademenu);
        s_UserData = getIntent().getStringExtra(UsrDat);
        this.userData = UserData.getInstance(s_UserData);

        //initialize all the buttons
        intiButtons();
    }

    private void intiButtons(){

        //format the currency (see IdleIsland.draw() for further detail)
        currency = (TextView) findViewById(R.id.mCurrency2);
        DecimalFormat df = new DecimalFormat("#.##");
        String curr;
        if(this.userData.getCurrency() >= 1000 && this.userData.getCurrency() < 1000000){
            String currency = df.format(((double)this.userData.getCurrency() /(double) 1000) );
            curr = currency+" K";

        }else if(this.userData.getCurrency() >= 1000000){
            String currency = df.format(((double)this.userData.getCurrency() /(double) 1000000) );
            curr = currency+" M";
        }else{
            curr = this.userData.getCurrency()+"";
        }

        currency.setText(curr);

        //load the correct images for each button
        upgradeButtons[0][0] = (ImageButton) findViewById(R.id.item1upgrade1);
        upgradeButtons[1][0] = (ImageButton) findViewById(R.id.item1upgrade2);
        upgradeButtons[2][0] = (ImageButton) findViewById(R.id.item1upgrade3);

        upgradeButtons[0][1] = (ImageButton) findViewById(R.id.item2upgrade1);
        upgradeButtons[1][1] = (ImageButton) findViewById(R.id.item2upgrade2);
        upgradeButtons[2][1] = (ImageButton) findViewById(R.id.item2upgrade3);

        upgradeButtons[0][2] = (ImageButton) findViewById(R.id.item3upgrade1);
        upgradeButtons[1][2] = (ImageButton) findViewById(R.id.item3upgrade2);
        upgradeButtons[2][2] = (ImageButton) findViewById(R.id.item3upgrade3);

        //iterate through all the buttons and set an event listener
        for(int i = 0; i<3; i++){
            for(int j = 0; j<3; j++){
                setListener(i,j);
             }
        }

        //initialize the back button
        mBack = (Button) findViewById(R.id.mBackToGame);
        mBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                UpgradeMenu.this.finish();
            }
        });
    }

    private void setListener(int i, int j){

        //make the indexes final so we can use them in the listeners
        final int g,h;
        g = i;
        h = j;

        //get the upgrades for the current level
        int[][] upgrades = this.userData.getUpgrades(userData.getLevel()-1).getUpgrades();

            //create listener for the current imageButton
            upgradeButtons[i][j].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    //get the upgrades for the current level (has to happen again because we are in another context)
                    Upgrades current = userData.getUpgrades(userData.getLevel() - 1);
                    int[][] upgr = current.getUpgrades();

                    //see if we can afford the upgrade
                    if (userData.getCurrency() < current.getPrice(h, g)) {

                        //if not we display a message about it
                        Toast.makeText(UpgradeMenu.this, R.string.no_money, Toast.LENGTH_SHORT).show();
                        return;

                    } else if(upgr[h][g] == 2) {
                        //if we already own the upgrade
                        Toast.makeText(UpgradeMenu.this, R.string.already_bought, Toast.LENGTH_SHORT).show();
                    }else if(upgr[h][g] == 0) {
                        //if the upgrade is locked
                        Toast.makeText(UpgradeMenu.this, R.string.upgrade_unavailable, Toast.LENGTH_SHORT).show();
                    }else{

                        //if we can afford the upgrade and it is available

                        //use the Upgrades class to purchase the upgrade
                        current.buyUpgrade(g, h);

                        //update the players' currency
                        userData.setCurrency(userData.getCurrency() - current.getPrice(h, g));

                        //calculate the new factor and treeFactor
                        userData.setTreeFactor((double) Calculator.calculateTreeFactor(userData.getUpgrades(0).getUpgrades(), userData.getUpgrades(1).getUpgrades()));
                        userData.setCurrFactor((double) Calculator.createFactor(userData.getUpgrades(0).getUpgrades(), userData.getUpgrades(1).getUpgrades()));

                        //let the user know the upgrade has been bought
                        Toast.makeText(UpgradeMenu.this, R.string.upgrade_purchased, Toast.LENGTH_SHORT).show();

                        //chance the image on the imageButtons that are now available
                        unlockUpgrades();
                    }

                    //format the new currency after the purchase (see IdleIsland.draw() for further detail)
                    DecimalFormat df = new DecimalFormat("#.##");
                    String curr;
                    if(userData.getCurrency() >= 1000 && userData.getCurrency() < 1000000){
                        String currency = df.format(((double)userData.getCurrency() /(double) 1000) );
                        curr = currency+" K";

                    }else if(userData.getCurrency() >= 1000000){
                        String currency = df.format(((double)userData.getCurrency() /(double) 1000000) );
                        curr = currency+" M";
                    }else{
                        curr = userData.getCurrency()+"";
                    }

                    //update the text area
                    currency.setText(curr);

                }
            });

        //if the current upgrade is not available then we change the image to a lock
        if(upgrades[i][j] != 1 && upgrades[i][j] != 2){
            upgradeButtons[j][i].setBackgroundResource(R.drawable.game_upgrade_lock_02);
        }
    }

    /*
        method to change the imageButtons to their correct picture if they are available or have been bought
     */
    private void unlockUpgrades(){

        //initialize the correct images...
        upgradeButtons[0][0].setBackgroundResource(R.drawable.item1upgrade1);
        upgradeButtons[1][0].setBackgroundResource(R.drawable.item1upgrade2);
        upgradeButtons[2][0].setBackgroundResource(R.drawable.item1upgrade3);

        upgradeButtons[0][1].setBackgroundResource(R.drawable.item2upgrade1);
        upgradeButtons[1][1].setBackgroundResource(R.drawable.item2upgrade2);
        upgradeButtons[2][1].setBackgroundResource(R.drawable.item2upgrade3);

        upgradeButtons[0][2].setBackgroundResource(R.drawable.item3upgrade1);
        upgradeButtons[1][2].setBackgroundResource(R.drawable.item3upgrade2);
        upgradeButtons[2][2].setBackgroundResource(R.drawable.item3upgrade3);

        int[][] upgrades = this.userData.getUpgrades(userData.getLevel()-1).getUpgrades();

        //...then go through the images and...
        for(int i= 0; i<3; i++){
            for(int j = 0; j<3;j++){

                //...see if the upgrade is unavailable...
                if(upgrades[i][j] != 1 && upgrades[i][j] != 2) {

                    //...and if it is then we change the image to a lock
                    upgradeButtons[j][i].setBackgroundResource(R.drawable.game_upgrade_lock_02);
                }
            }
        }
    }
}
