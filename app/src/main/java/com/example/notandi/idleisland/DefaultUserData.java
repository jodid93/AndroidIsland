package com.example.notandi.idleisland;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by thorgeir on 18.2.2016.
 */
public class DefaultUserData {

    public Integer xGrid;
    public Integer yGrid;
    public Integer[][] upgradesLVL_1;
    public Integer currency;
    public JSONObject settings;
    public int currFactor;
    public int treeFactor;

    //Upgrades states
    public Integer boughtID;
    public Integer availableID;
    public Integer unreachableID;

    public DefaultUserData() {
        this.xGrid = 4;
        this.yGrid = 4;

        this.currency = 10000;
        this.currFactor = 1;
        this.treeFactor = 1;

        this.boughtID = 2;
        this.availableID = 1;
        this.unreachableID = 0;

        // SETTINGS
        //Audio off -> mute = 0
        //Audio on -> mute = 1
        // 0 <= volume <= 100,
        // 0 -> audio is off, 100 -> audio is in fullpower
        this.settings = new JSONObject();

        try {
            this.settings.put("mute", new Integer(0));
            this.settings.put ("volume", new Integer(50));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // UPGRADES
        // ef...
        // upgrade = 2 -> the item has been bought
        // upgrade = 1 -> the item is aviable
        // upgrade = 0 -> the item is unreachable
        this.upgradesLVL_1 = new Integer[this.xGrid][this.yGrid];
        this.upgradesLVL_1[0][0] = this.availableID;
        //the rest is unreachableID (=0)

    }
}
