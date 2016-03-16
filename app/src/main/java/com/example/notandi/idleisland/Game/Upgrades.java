package com.example.notandi.idleisland.Game;

/**
 * Created by thorgeir on 18.2.2016.
 * class that contains methods concerning the upgrades
 *
 * DRAWBACK:
 *          A lot of the getters and setters shouldn't be there. they are only for development purposes 
 */
public class Upgrades {
    private Integer level;      //the level these upgrades are to be assigned to
    private int[][] upgrades;   //the status array of each upgrade
    private int[][] prices;     //the price for each upgrade


    //true if this upgrades are available for the user
    private Boolean available;

    //constructor
    public Upgrades( Integer level, Integer xGrid, Integer yGrid, Boolean available ) {

        //initialize the prices depending on the level of upgrades
        if(level == 1){
            this.prices = new int[][] {{10,2500,150000},{1000,15000,250000},{5000,100000,500000}};
        }else{
            this.prices = new int[][] {{300000,1000000,8000000},{700000,2000000,25000000},{1500000,10000000,100000000}};
        }
        this.level = level;
        this.available = available;
        this.upgrades = new int[xGrid][yGrid];

        //set the first upgrade to be available
        this.upgrades[0][0] = 1;
    }

    /*
        method to purchase an upgrade

        x,y are the indexes used to access the int[][] upgrades
     */
    public void buyUpgrade(int x, int y){

        int xLimit = 2; //size of the 2d array

        //if the upgrade is unavailable then return
        if( upgrades[y][x] == 0 || upgrades[y][x] == 2 ){
            return;
        }

        //if user has bought the first upgrade for an item
        else if( x == 0 && y < xLimit ){

            this.upgrades[y][x] = 2;    //mark the upgrade as purchased

            this.upgrades[y][x+1] = 1;  //and unlock the upgrades adjacent to it
            this.upgrades[y+1][x] = 1;

            return;
        }else if(y <= xLimit){              //else if we are purchasing the second or third upgrade for an item
            this.upgrades[y][x] = 2;        //mark the upgrade as bought

            if(x < xLimit){                 //make sure not to go out of bounds
                this.upgrades[y][x+1] = 1;  //unlock the upgrade below it
            }else{
                return;                     //unless we have reached the last upgrade for an item
            }
        }
    }

    //method to set a value to an array
    //the values are: 0 = unavailable, 1 = available, 2 = bought
    public void add(int value, int x, int y){
        this.upgrades[x][y] = value;
    }

    //getters and setters
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

    public int getPrice(int i, int j){
        return prices[i][j];
    }

}
