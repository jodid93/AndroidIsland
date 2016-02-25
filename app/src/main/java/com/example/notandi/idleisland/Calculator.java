package com.example.notandi.idleisland;

/**
 * Created by Notandi on 25.2.2016.
 */

import java.util.Date;

public class Calculator {

    public int[][] prices1 = {{10,2500,150000},{1000,15000,250000},{5000,100000,500000}};
    private int[][] factors1 = {{1,5,10},{2,7,15},{3,9,20}};

    public int[][] prices2 = {{300000,1000000,8000000},{700000,2000000,25000000},{1500000,10000000,100000000}};
    private int[][] factors2 = {{1*2,5*2,10*20},{2*2,7*2,15*2},{3*2,9*2,20*2}};

    private int difference = 0;

    public int calculateCurrency(double time, int currency, int currFactor){
        double tala = (time/1000)*currFactor;
        int intTala = (int)(tala);
        double difference = tala - intTala;
        this.difference += difference;
        if(this.difference >= 1){
            this.difference -= 1;
            intTala += 1;
        }

        return currency + intTala;


    };

    public int calculateCurrencyOffline(double time, int currency, int currFactor){
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

    public int calculateTreeFactor(int [][] upgrades,int [][] upgrades2){

        int factor = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(upgrades[i][j] == 2){
                    factor += 1;
                }
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 3; k++) {
                if(upgrades2[i][k] == 2){
                    factor += 1;
                }
            }
        }
        return factor;
    };

    public double calculateOfflineCurrency(long date, int currency, int factor){
        double timeElapsedInSecs = (System.nanoTime() - date);
        double curr = this.calculateCurrency(timeElapsedInSecs,currency, factor);

        return curr;


    };

    public double calculateOfflineScore(long date, int currency, int factor){
        double timeElapsedInSecs = (System.nanoTime() - date);
        double curr = this.calculateCurrencyOffline(timeElapsedInSecs,currency, factor);

        return curr;


    };

    public int createFactor(int [][] upgrades,int [][] upgrades2){

        int factor = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(upgrades[i][j] == 2){
                    factor += this.factors1[i][j];
                }
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 3; k++) {
                if(upgrades2[i][k] == 2){
                    factor += this.factors2[i][k];
                }
            }
        }

        return factor;

    };


}
