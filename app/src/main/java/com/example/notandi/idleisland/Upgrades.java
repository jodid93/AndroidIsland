package com.example.notandi.idleisland;

/**
 * Created by thorgeir on 18.2.2016.
 */
public class Upgrades {
    private Integer level;
    private Integer numBoughtItems;
    private int[][] upgrades;
    private int[][] prices;


    //true if this upgrades are available for the user
    private Boolean available;

    public Upgrades( Integer level, Integer xGrid, Integer yGrid, Boolean available ) {
        if(level == 1){
            this.prices = new int[][] {{10,2500,150000},{1000,15000,250000},{5000,100000,500000}};
        }else{
            this.prices = new int[][] {{300000,1000000,8000000},{700000,2000000,25000000},{1500000,10000000,100000000}};
        }
        this.level = level;
        this.available = available;
        this.numBoughtItems = 0;
        this.upgrades = new int[xGrid][yGrid];
        this.upgrades[0][0] = 1;
    }

    public void buyUpgrade(int x, int y){
        int xLimit = 2;

        System.out.println("keypt: "+x+", "+y);

        if( upgrades[y][x] == 0 || upgrades[y][x] == 2 ){
            return;
        }

        //if user has bought the first item in the column x
        else if( x == 0 && y < xLimit ){
            this.upgrades[y][x] = 2;
            this.upgrades[y][x+1] = 1;
            this.upgrades[y+1][x] = 1;
            return;
        }else if(y <= xLimit){
            this.upgrades[y][x] = 2;
            if(x < xLimit){
                this.upgrades[y][x+1] = 1;
            }else{
                return;
            }
        }
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

    public int getPrice(int i, int j){
        return prices[i][j];
    }

}
