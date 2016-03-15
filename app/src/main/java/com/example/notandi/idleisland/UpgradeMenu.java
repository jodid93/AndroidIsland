package com.example.notandi.idleisland;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

/**
 * Created by josua on 5.3.2016.
 */
public class UpgradeMenu extends AppCompatActivity {

    private String s_UserData;
    public UserData userData;
    private static final String UsrDat = "idleisland.userdata";

    private ImageButton[][] upgradeButtons = new ImageButton[3][3];
    private TextView currency;
    private Button mBack;

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

        intiButtons();
    }

    private void intiButtons(){

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



        upgradeButtons[0][0] = (ImageButton) findViewById(R.id.item1upgrade1);
        upgradeButtons[1][0] = (ImageButton) findViewById(R.id.item1upgrade2);
        upgradeButtons[2][0] = (ImageButton) findViewById(R.id.item1upgrade3);

        upgradeButtons[0][1] = (ImageButton) findViewById(R.id.item2upgrade1);
        upgradeButtons[1][1] = (ImageButton) findViewById(R.id.item2upgrade2);
        upgradeButtons[2][1] = (ImageButton) findViewById(R.id.item2upgrade3);

        upgradeButtons[0][2] = (ImageButton) findViewById(R.id.item3upgrade1);
        upgradeButtons[1][2] = (ImageButton) findViewById(R.id.item3upgrade2);
        upgradeButtons[2][2] = (ImageButton) findViewById(R.id.item3upgrade3);

        for(int i = 0; i<3; i++){
            for(int j = 0; j<3; j++){
                setListener(i,j);
             }
        }

        mBack = (Button) findViewById(R.id.mBackToGame);
        mBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Intent i = GameEngine.newIntent(UpgradeMenu.this, "hannes");
                startActivityForResult(i, 666);


                //Toast.makeText(MenuActivity.this, R.string.message, Toast.LENGTH_SHORT ).show();
            }
        });
    }

    private void setListener(int i, int j){
        final int g,h;
        g = i;
        h = j;
        int[][] upgrades = this.userData.getUpgrades(userData.getLevel()-1).getUpgrades();


            upgradeButtons[i][j].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Upgrades current = userData.getUpgrades(userData.getLevel() - 1);
                    int[][] upgr = current.getUpgrades();
                    if (userData.getCurrency() < current.getPrice(h, g)) {
                        System.out.println("Currency: " + userData.getCurrency() + " price: " + current.getPrice(h, g));
                        Toast.makeText(UpgradeMenu.this, R.string.no_money, Toast.LENGTH_SHORT).show();
                        return;
                    } else if(upgr[h][g] == 2) {
                        Toast.makeText(UpgradeMenu.this, R.string.already_bought, Toast.LENGTH_SHORT).show();
                    }else if(upgr[h][g] == 0) {
                        Toast.makeText(UpgradeMenu.this, R.string.upgrade_unavailable, Toast.LENGTH_SHORT).show();
                    }else{

                        current.buyUpgrade(g, h);
                        userData.setCurrency(userData.getCurrency() - current.getPrice(h, g));
                        userData.setTreeFactor((double) Calculator.calculateTreeFactor(userData.getUpgrades(0).getUpgrades(), userData.getUpgrades(1).getUpgrades()));
                        userData.setCurrFactor((double) Calculator.createFactor(userData.getUpgrades(0).getUpgrades(), userData.getUpgrades(1).getUpgrades()));
                        Toast.makeText(UpgradeMenu.this, R.string.upgrade_purchased, Toast.LENGTH_SHORT).show();
                        unlockUpgrades();
                    }
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

                    currency.setText(curr);

                }
            });
        if(upgrades[i][j] != 1 && upgrades[i][j] != 2){
            upgradeButtons[j][i].setBackgroundResource(R.drawable.game_upgrade_lock_02);
        }
    }

    private void unlockUpgrades(){
        System.out.println("herna inni i fuck sakanum");
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

        for(int i= 0; i<3; i++){
            for(int j = 0; j<3;j++){
                if(upgrades[i][j] != 1 && upgrades[i][j] != 2){
                    upgradeButtons[j][i].setBackgroundResource(R.drawable.game_upgrade_lock_02);
                }else{
                    System.out.println("cool beans: "+i+"   "+j);
                }
            }
        }
    }
}
