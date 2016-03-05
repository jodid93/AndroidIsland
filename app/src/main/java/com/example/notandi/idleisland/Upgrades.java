package com.example.notandi.idleisland;

/**
 * Created by thorgeir on 18.2.2016.
 */
public class Upgrades {
    private Integer level;
    private Integer numBoughtItems;
    private int[][] upgrades;

    //true if this upgrades are available for the user
    private Boolean available;

    public Upgrades( Integer level, Integer xGrid, Integer yGrid, Boolean available ) {
        this.level = level;
        this.available = available;
        this.numBoughtItems = 0;
        this.upgrades = new int[xGrid][yGrid];
    }

    public void add(int value, int x, int y){
        this.upgrades[x][y] = value;
    }



    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Boolean getAvailable() {
        return available;
    }


    public Integer getLevel() {
        return level;
    }

    public int[][] getUpgrades() {
        return upgrades;
    }


    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setUpgrades(int[][] upgrades) {
        this.upgrades = upgrades;
    }

}
