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

/**
 * Created by josua on 5.3.2016.
 */
public class UpgradeMenu extends AppCompatActivity {

    private String s_UserData;
    public UserData userData;
    private static final String UsrDat = "idleisland.userdata";

    private ImageButton[][] upgradeButtons = new ImageButton[3][3];
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
        upgradeButtons[i][j].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("-------------herna---------------");
                Upgrades current = userData.getUpgrades(userData.getLevel() - 1);
                if (userData.getCurrency() < current.getPrice(g, h)) {
                    System.out.println("Currency: " + userData.getCurrency() + " price: " + current.getPrice(g, h));
                    return;
                } else {
                    current.buyUpgrade(g, h);
                    userData.setCurrency(userData.getCurrency() - current.getPrice(g, h));
                    userData.setTreeFactor((double) Calculator.calculateTreeFactor(userData.getUpgrades(0).getUpgrades(), userData.getUpgrades(1).getUpgrades()));
                    userData.setCurrFactor((double) Calculator.createFactor(userData.getUpgrades(0).getUpgrades(), userData.getUpgrades(1).getUpgrades()));
                }
                userData.printUpgrades();

            }
        });
    }
}
