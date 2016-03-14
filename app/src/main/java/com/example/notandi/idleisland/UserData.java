package com.example.notandi.idleisland;


import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by thorgeir on 18.2.2016.
 */
public class UserData {

    private static volatile UserData instance;
    public Integer numItems;
    public Integer numBoughtItems;

    private String userName;
    private Integer currency;
    private JSONObject settings;
    //private java.sql.Timestamp timestamp;
    private Timestamp timestamp;
    private Double currFactor;
    private Double treeFactor;
    private Integer bought;
    private Integer available;
    private Integer unreachable;
    private Integer level;
    private Upgrades[] upgrades;

    private Integer numLevel;

    private UserData(String userName){
        DefaultUserData dUserData = new DefaultUserData();

        updateTime();

        this.userName = userName;
        this.numLevel = 2;
        this.level = 1;
        numBoughtItems = 0;

        fetchFromDefaultUserData(dUserData);

        //Initialize upgrade grid for every level
        this.upgrades = new Upgrades[this.numLevel];

        Boolean available = null;

        for(int i=0; i<this.numLevel; i++){
            if( this.level == 1 )
                available = true;
            else
                available = false;

            this.upgrades[i] = new
                    Upgrades(
                            this.level+i,
                            dUserData.xGrid,
                            dUserData.yGrid,
                            available
                    );
        }
        System.out.println("ad koma................");
        printUpgrades();
    }

    public static UserData getInstance(String userName) {
        if (instance == null) {
            System.out.println("userdata var null..........");
            synchronized (UserData.class) {
                if (instance == null)
                    System.out.println("bua til nytt userdata thi thad var null..........");
                    instance = new UserData (userName);
            }
        }
        return instance;
    }


    public void printUpgrades(){
        Upgrades x = upgrades[0];
        int [][] m_upgrades = x.getUpgrades();

        for (int i = 0; i<3; i++){
            for (int j = 0; j<3; j++){
                System.out.print(m_upgrades[i][j]+", ");
            }
            System.out.println("");
        }

    }

    private void fetchFromDefaultUserData(DefaultUserData dUserData){
        this.numItems = dUserData.xGrid + dUserData.xGrid;
        this.unreachable = dUserData.unreachableID;
        this.currFactor = dUserData.currFactor;
        this.treeFactor = dUserData.treeFactor;
        this.available = dUserData.availableID;
        this.currency = dUserData.currency;
        this.settings = dUserData.settings;
        this.bought = dUserData.boughtID;
    }

    public void updateTime(){
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        this.timestamp = new Timestamp(now.getTime());
    }

    public Upgrades getUpgrades(int level ){
        return this.upgrades[level];
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

    public Double getCurrFactor() {
        return currFactor;
    }

    public Double getTreeFactor() {
        return treeFactor;
    }

    public Integer getLevel() {
        return this.level;
    }

    public Integer getNumBoughtItems() {
        return this.numBoughtItems;
    }




    public void setCurrency(Integer currency) {
        this.currency = currency;
    }



    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setNumBoughtItems(Integer numBoughtItems) {
        this.numBoughtItems = numBoughtItems;
    }

    public void setCurrFactor(Double currFactor) {
        this.currFactor = currFactor;
    }

    public void setSettings(JSONObject settings) {
        this.settings = settings;
    }

    public void setTreeFactor(Double treeFactor) {
        this.treeFactor = treeFactor;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUpgrades(Upgrades[] newUpgrades) {
        this.upgrades = newUpgrades;
    }
}
/*



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

*/