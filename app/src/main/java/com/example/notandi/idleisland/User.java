package com.example.notandi.idleisland;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;

import java.util.Objects;

//
// User CONTAINS
// ----------------
//  -> Userdata
//
//
// User PERFORM/IMPLEMENTS
// ----------------
//  -> Convert UserData to JSON and visa versa
//  -> Buy upgrades (updates the upgrade grid for a given grid position)
//  -> ...more in the future


/**
 * Created by thorgeir on 18.2.2016.
 */
public class User {

    private DefaultUserData dUserD;
    private Gson gson = new Gson();

    private UserData userD;
    private SQL query;

    public User( String name ) {
        /*if( nameOrUserData.length() > 30 ){
            //this.userD = this.getUserDataFromJSON(nameOrUserData);
        } else {
        }*/
        this.userD = new UserData( name );
        this.dUserD = new DefaultUserData();
        this.query = new SQL();
    }


    // Updates the upgrade grid on the current level
    // for a giving coordinate position.
    // more detail...
    // mark [x][y] as baugth item also
    // mark new available items around the bought item.
    public void buyUpgrades(Integer x, Integer y) {

        //ÞARF KANNSKI AÐ TILGREINA HVAÐA LEVEL-I við erum í

        userD.numBoughtItems++;

        //If user tries to buy item when he
        // has already bought every item
        if( userD.numBoughtItems == userD.numItems+1 ){
            System.out.print( "Somthing going wrong!" );
        } else{
            updateUpgrades(x,y);
        }
    }



    // change the items around [x][y] from to be
    // unreachable to be aviable for the user
    private void updateUpgrades(Integer x, Integer y){
        Integer xLimit = dUserD.xGrid - 1;
        Integer yLimit = dUserD.yGrid - 1;

        Boolean levelChanged = false;
        Integer currentLevel = userD.getLevel();

        // Get all upgrades
        Upgrades[] upgrades = userD.getUpgrades();

        // get the upgrades from the current level and
        // the next level
        Upgrades currentUpgrades = upgrades[currentLevel];
        Upgrades nextUpgrades = upgrades[currentLevel+1];

        currentUpgrades.add( dUserD.boughtID, x, y );

        if( x == xLimit || y == xLimit ){
            // We have reach the last upgrade
            // column -> no need for changes
        }

        // if user have bought first item in last column
        // ...then set available flag on the upgrades on
        // on the next level
        if( x == xLimit || y == 0 ){
            levelChanged = true;
            nextUpgrades.setAvailable(true);
        }

        //if user has bought the first item in the column x
        else if( y == 0 && x <= xLimit ){
            currentUpgrades.add( dUserD.availableID, x+1, y );
        }
        if( 0 < y && y <= yLimit ){
            if( 0 < x && x <= xLimit ){
                currentUpgrades.add( dUserD.availableID, x, y+1 );
            }
        }

        upgrades[currentLevel  ] = currentUpgrades;
        upgrades[currentLevel+1] = nextUpgrades;

        userD.setUpgrades( upgrades );

        if(levelChanged){
            currentLevel++;
        }

        userD.setLevel( currentLevel );
    }



    // Converts UserData userD to JSON String
    // and returns it
    public String userDataToJSON(){
        return this.gson.toJson(userD);
    }

    public UserData getUserData(){
        return this.userD;
    }


    public String getUserName( ){
        return userD.getUserName();
    }
}