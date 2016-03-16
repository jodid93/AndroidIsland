package com.example.notandi.idleisland.Game;

/**
 * Created by Notandi on 25.2.2016.
 * Class used for utilities. This class contains methods for calculating score and currency
 *
 * DRAWBACK:
 *      the methods calculateOfflineScore, calculateOfflineCurrency and calculateCurrencyOffline are not used
 *      currently since the database access for the game has not been implemented on all levels of the design yet
 */

import java.util.Date;

public class Calculator {

    //prices for the upgrades in level 1
    public int[][] prices1 = {{10,2500,150000},{1000,15000,250000},{5000,100000,500000}};

    //factors is the incremental score factor that you get when buying upgrades
    //this is used in the create factor method
    private static int[][] factors1 = {{1,5,10},{2,7,15},{3,9,20}};

    //same as above but for level 2
    public int[][] prices2 = {{300000,1000000,8000000},{700000,2000000,25000000},{1500000,10000000,100000000}};
    private static int[][] factors2 = {{1*2,5*2,10*20},{2*2,7*2,15*2},{3*2,9*2,20*2}};

    //difference is used to get an integer from the calculate currency method
    private double difference = 0.0;

    //method for calculating the amount gained in one second
    //time is the delta time since the last iteration,
    //currency is the current currency the player has
    //currFactor is the current factor the player has (from the upgrades he has bought)
    //this method returns the integer amount gained from each iteration
    public int calculateCurrency(double time, int currency, Double currFactor){

        //start by getting the amount gained in this iteration
        // from the time for this iteration * the factor
        double tala = (time/1000.0)*(double)currFactor;
        //get the integer amount of that number
        int intTala = (int)(tala);

        //keep track of the total amount gained for each iteration since we last
        //had an amout that was bigger than 1
        double difference = tala - (double)intTala;
        this.difference += difference;

        //and if the accumulated difference is greater than 1
        if(this.difference >= 1.0){

            //then add 1 to the integer amount and balance the difference
            this.difference -= 1.0;
            intTala += 1;
        }

        //return the new total currency the player has
        return currency + intTala;


    };

    /*
        method to calculate the currency gained while the player was offline

        time is the time difference since the player logged out and to when he logged back in
        currency is the total amount the player has before this method
        currFactor is the current factor the player has (from the upgrades he has bought)

        the method return the new total currency that the player owns

        see inner workings from the method above
     */
    public int calculateCurrencyOffline(double time, int currency, Double currFactor){
        double tala = (time/1000)*currFactor;
        int intTala = (int)(tala);
        double difference = tala - intTala;
        this.difference += difference;
        if(this.difference >= 1){
            this.difference -= 1;
            intTala += 1;
        }

        return intTala;


    };

    /*
    * method to get the latest treefactor from the accumulative upgrades bought
    *
    * upgrades, upgrades2 are the upgrade arrays that contains the indexes for
    * whether an upgrade is unavailable, bought or available
    *
    * method returns a new treeFactor
    * (treeFactor is used to calculate the amount gained from each treepunch)
    * */
    public static int calculateTreeFactor(int [][] upgrades,int [][] upgrades2){

        //factor will be an accumulative value for each time we see an upgrade that
        //has been bought
        int factor = 0;

        //iterate through the 2d array
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                //if we see a bought upgrade (labeled as 2)
                if(upgrades[i][j] == 2){
                    //we increment the factor
                    factor += 1;
                }
                if(upgrades2[i][j] == 2){
                    factor += 1;
                }
            }
        }

        return factor;
    };

    /*
    method to prep the calculateCurrencyOffline method

    date is a timeStamp from the time the player logged off
    currency is the players current total amount
    factor is the current factor gained form upgrades bought
     */
    public double calculateOfflineCurrency(long date, int currency, Double factor){

        //get the time in nanoseconds since the last time the player logged off
        double timeElapsedInSecs = (System.nanoTime() - date)/1000000.0;
        double curr = this.calculateCurrency(timeElapsedInSecs,currency, factor);

        return curr;

    };

    /*
    really the same as the method calculateOfflineCurrency since score has a linear relationship to currency
     */
    public double calculateOfflineScore(long date, int currency, Double factor){

        double timeElapsedInSecs = (System.nanoTime() - date)/1000000.0;

        double curr = this.calculateCurrencyOffline(timeElapsedInSecs,currency, factor);

        return curr;

    };

    /*
    * method to get the latest factor from the accumulative upgrades bought
    *
    * upgrades, upgrades2 are the upgrade arrays that contains the indexes for
    * whether an upgrade is unavailable, bought or available
    *
    * method returns a new Factor
    * (Factor is used to calculate the amount gained from each iteration)
    *
    * for inner working see method createTreeFactor
    */
    public static int createFactor(int [][] upgrades,int [][] upgrades2){

        int factor = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(upgrades[i][j] == 2){
                    factor += factors1[i][j];
                }
                if(upgrades2[i][j] == 2){
                    factor += factors2[i][j];
                }
            }
        }

        return factor;

    };
}
