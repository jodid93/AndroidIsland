package com.example.notandi.idleisland.Mocks;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by thorgeir on 18.2.2016.
 */
public class DefaultUserData {

    public Integer xGrid;
    public Integer yGrid;
    public Integer[][] upgrades;
    public Integer currency;
    public int score;
    public JSONObject settings;
    public Double currFactor;
    public Double treeFactor;

    //Upgrades states
    public Integer boughtID;
    public Integer availableID;
    public Integer unreachableID;

    public DefaultUserData() {
        this.xGrid = 3;
        this.yGrid = 3;

        this.score = 0;

        this.currency = 10000000;
        this.currFactor = 0.0;
        this.treeFactor = 1.0;

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
        this.upgrades = new Integer[this.xGrid][this.yGrid];
        this.upgrades[0][0] = this.availableID;
        //the rest is unreachableID (=0 or null)

    }
}
