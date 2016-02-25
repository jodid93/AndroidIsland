package com.example.notandi.idleisland;


import org.json.JSONException;
import org.json.JSONObject;

import java.security.Timestamp;

/**
 * Created by thorgeir on 18.2.2016.
 */
public class UserData {

    private DefaultUserData dUserData;

    private String userName;
    private Integer[][] upgradesLVL_1;
    private Integer currency;
    private JSONObject settings;
    private Timestamp timestamp;
    private int currFactor;
    private int treeFactor;
    private Integer numBoughtItems;
    private Integer numItems;
    private Integer bought;
    private Integer available;
    private Integer unreachable;
    private Integer level;
    private Upgrades[] upgrades;

    //BRETYR SEM ÞURFA AÐ FARA EINHVER ANNAÐ HELD ÉG.
    private Integer numLevel;

    public UserData(String userName) {

        dUserData = new DefaultUserData();

        this.userName = userName;
        this.numLevel = 2;
        this.level = 1;
        this.numBoughtItems = 0;

        fetchFromDefaultUserData();

        //Initialize upgrade grid for every level
        this.upgrades = new Upgrades[this.numLevel];

        Boolean available = null;

        for(int i=0; i<this.numLevel; i++){
            if( this.level == 1 )
                available = true;
            else
                available = false;

            this.upgrades[i] =
            new Upgrades(
                this.level+i,
                dUserData.xGrid,
                dUserData.yGrid,
                available
            );
        }
    }


    private void fetchFromDefaultUserData(){
        this.numItems = dUserData.xGrid + dUserData.xGrid;
        this.unreachable = dUserData.unreachableID;
        this.currFactor = dUserData.currFactor;
        this.treeFactor = dUserData.treeFactor;
        this.available = dUserData.availableID;
        this.currency = dUserData.currency;
        this.settings = dUserData.settings;
        this.bought = dUserData.boughtID;
    }
    // Updates the upgrade grid on the current level
    // for a giving coordinate position.
    // more detail...
    // mark [x][y] as baugth item also
    // mark new available items around the bought item.
    public void buyUpgrades(Integer x, Integer y) {

        //ÞARF KANNSKI AÐ TILGREINA HVAÐA LEVEL-I við erum í

        numBoughtItems++;

        //If user tries to buy item when he
        // has already bought every item
        if( numBoughtItems == numItems+1 ){
            System.out.print( "Somthing going wrong!" );
            return;
        } else{
            updateUpgrades(x,y);
        }
    }


    // change the items around [x][y] from to be
    // unreachable to be aviable for the user
    private void updateUpgrades(Integer x, Integer y){
        Integer xLimit = dUserData.xGrid - 1;
        Integer yLimit = dUserData.yGrid - 1;

        Upgrades upgrade = this.upgrades[this.level];

        upgrade.add( this.bought, x, y );

        if( x == xLimit || y == xLimit ){
            // We have reach the last upgrade
            // column -> no need for changes
        }

        // if user have bought first item in last column
        if( x == xLimit || y == 0 ){
            this.upgrades[++this.level].setAvailable( true );
        }

        //if user has bought the first item in the column x
        else if( y == 0 && x <= xLimit ){
            upgrade.add( this.available, x+1, y );
        }
        if( 0 < y && y <= yLimit ){
            if( 0 < x && x <= xLimit ){
                upgrade.add( this.available, x, y+1 );
            }
        }
    }

    //
    // GETTERS AND SETTERS (straight forward)
    //      |
    //      v

    public String getUserName() {
        return userName;
    }

    public Integer getCurrency() {
        return currency;
    }

    public JSONObject getSettings() {
        return settings;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public int getCurrFactor() {
        return currFactor;
    }

    public int getTreeFactor() {
        return treeFactor;
    }



    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setCurrFactor(int currFactor) {
        this.currFactor = currFactor;
    }

    public void setSettings(JSONObject settings) {
        this.settings = settings;
    }

    public void setTreeFactor(int treeFactor) {
        this.treeFactor = treeFactor;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
