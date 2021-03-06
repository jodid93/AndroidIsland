package com.example.notandi.idleisland.Game;

/**
 * Created by Notandi on 18.2.2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.notandi.idleisland.Database.DatabaseHelper;
import com.example.notandi.idleisland.R;
import com.example.notandi.idleisland.User.UserData;

import java.text.DecimalFormat;
import java.util.Vector;

/*
    Class that holds all the methods for running and maintaining the game itself

    DRAWBACK:
            We could have used more singletons so we wouldn't have to pass things like Calculator through
            the chain of classes.
            Secondly, the String formatter in the draw method should have been a static method so UpgradeMenu
            could use it as well.
            most drawbacks that apply to GameEngine apply to this class

 */
public class IdleIsland extends SurfaceView implements SurfaceHolder.Callback
{

    private MainThread thread;      //the engine that drives the gameloop
    private Calculator calculator;  // the calculator that we get from GameEngine.class
    private UserData userData;      // the global UserData object that contains all the relevant info for the player
    private int level;              //the current level of this instance of idleisland
    private int [][] upgrades;      //the upgrade array for this level

    private Background background;  //the background image
    private Sprite[][] sprites;     //sprites that are used in this level

    private GameEngine context;

    private Bitmap coconut; //coconut image used with the gainedAnimation

    public IdleIsland(Context context, GameEngine engine, Calculator calculator, UserData userData, int level, Sprite[][] sprites)
    {
        super(context);

        this.context = engine;
        //basic
        this.sprites = sprites;
        this.level = level;
        this.calculator = calculator;
        this.userData = userData;
        this.upgrades = this.userData.getUpgrades(level).getUpgrades();
        this.coconut = BitmapFactory.decodeResource(getResources(), R.drawable.game_coconut_money);

        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        //initialize the gameloop itself
        thread = new MainThread(getHolder(),engine);

        //make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    //not used
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    /*
        method used to kill the gameloop in MainThread
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while(retry)
        {
            try{thread.setRunning(false);
                thread.join();

            }catch(InterruptedException e){e.printStackTrace();}
            retry = false;
        }

    }

    /*
        same as surfaceDestoyed but as a non-extended method
        used by GameEngine.class
     */
    public void kill(){
        boolean retry = true;
        while(retry)
        {
            try{thread.setRunning(false);
                thread.join();

            }catch(InterruptedException e){e.printStackTrace();}
            retry = false;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){

        //create the background
        background = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.sky1));

        //get the upgrades for this level from the userdata
        Upgrades upgradesT = this.userData.getUpgrades(this.level);
        this.upgrades = upgradesT.getUpgrades();

        //we can safely start the game loop
        thread.setRunning(true);

        //if the thread hasn't started then we start the gameloop
        if (thread.getState() == Thread.State.NEW)
        {
            thread.start();
        }

    }

    //Vector containing any and all Strings of score that are supposed to animate
    // see gainedAnimation for more info
    Vector<gainedAnimation> gainedCocos = new Vector<gainedAnimation>();

    /*
        this method is called when the player taps the screen. it represents the punching of the tree
     */
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        if(event.getAction() == 0){ //ACTION DOWN

            //if no upgrades have been bought we need to loop the base animation once.
            //after the first upgrade has been bought the sprites should animate automatically and continuously
            if(this.upgrades[0][0] != 2){

                sprites[3][0].loopOnce();
            }

            //add a new gainedAnimation to the gainedCocos vector so we can animate the amount gained

            gainedCocos.add(new gainedAnimation(Integer.toString((int)(1 * this.userData.getTreeFactor())), coconut));

            //update the players currency and score
            userData.setCurrency(this.userData.getCurrency() + (int) (1 * this.userData.getTreeFactor()));
            int scoreid = this.userData.getScore() + (int)(1 * this.userData.getTreeFactor());
            this.userData.setScore(scoreid);
        }

        return true;
    }

    //these variables are used to find out how much the player has gained in one second
    private double secCounter = 0.0; //time counter that accumulates from the delta time up to one second and then resets
    private int gainedInSec = 0; // currency counter that accumulates each iteration of the gameloop
                                // and holds the amount gained from second to second

    private double autoSaveCounter = 0.0;

    /*
        method to update the game

        dt is the time delta since the last iteration of the gameloop
     */
    public void update(double dt)
    {
        //update secCounter
        secCounter += dt;
        autoSaveCounter += dt;
        if(autoSaveCounter > 10000){
            autoSave();
            autoSaveCounter = 0.0;
        }

        //update the players currency and score
        int currentCurrency = this.userData.getCurrency();
        int currency = this.calculator.calculateCurrency(dt, this.userData.getCurrency(), this.userData.getCurrFactor());
        int gained = currency - currentCurrency;
        int scoreid = this.userData.getScore() + gained;
        this.userData.setScore(scoreid);
        this.userData.setCurrency(gained + currentCurrency);

        //check if a second has passed
        if(secCounter > 1000.0){

            //if so then check if we gained more than 0 coconuts
            gainedInSec += gained;
            if(gainedInSec >= 1){
                //if so then we create a new gainedAnimation object with the amount we gained
                //and add it to our vector of gainedAnimation to animate the Text later
                gainedCocos.add(new gainedAnimation(Integer.toString(gainedInSec), coconut));
            }

            //reset the sec counter and gained counter
            secCounter = 0.0;
            gainedInSec  = 0;
        }else{
            //update the gained with the amount gained from this iteration of the gameloop
            gainedInSec += gained;
        }


        //little mess to get the gainedAnimation objects into a gainedAnimation array
        Object[] temp = (Object[]) this.gainedCocos.toArray();
        gainedAnimation[] gainedFylki = new gainedAnimation[temp.length];
        if(temp.length != 0){
            for(int i = 0; i<temp.length; i++){
                gainedFylki[i]= (gainedAnimation) temp[i];
            }
        }

        //check if the gainedAnimation array is empty
        if(gainedFylki.length != 0){

            //if not then we iterate through it and update each gainedAnimation object
            for(int i = 0; i<gainedFylki.length; i++){

                //if the object has left the screen it returns -1...
                if(gainedFylki[i].update(dt) == -1){

                    //...and we kill it
                    this.gainedCocos.removeElementAt(i);
                }
            }
        }

        //segment to update all the sprites

        //if no upgrades have been purchased then we only update the base animation...
        if(upgrades[0][0] == 1){
            //...whether or not something happenes is determined by the Sprite itself. See Sprite.class for more info
            sprites[3][0].update(dt);
        }else{

            //if any upgrades have been bought
            //then we iterate through the sprite array and find the latest update for each item
            //that was bought and only update that items' upgrade. (thats why j runs from 2 -> 0)
            for(int i = 0; i<3; i++){
                for(int j = 2; j >= 0; j-- ){
                    if(upgrades[i][j] == 2 ){
                        sprites[i][j].update(dt);

                        //so we don't update any items' upgrade that has a newer upgrade on it
                        break;
                    }
                }
            }
        }
    }

    private void autoSave(){
        DatabaseHelper lDB = DatabaseHelper.getInstance(context);
        this.userData.updateTime();
        lDB.insertUserData(this.userData.getUserName(), this.userData);
    }

    /*
        method to draw all the elements on the screen
     */
    @Override
    public void draw(Canvas canvas){

        super.draw(canvas);

        //scale factor for the screen according to the background image size
        final float scaleFactorX = ((float)getWidth())/((float)352);//getWidth()/540;
        final float scaleFactorY = ((float)getHeight())/((float)416);


        //make sure the canvas exists
        if(canvas!=null) {

            //save the state for scaling...
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);

            //draw the background image
            background.draw(canvas);
            canvas.restoreToCount(savedState);
        }

        //drawElement takes the arguments, canvas, Bitmap image, the image with, image Height, width in % of the screen, Height in % of the
        // screen, x position in % of the screen and finally y position in % of the screen
        drawElement(canvas,BitmapFactory.decodeResource(getResources(), R.drawable.ocean), 178.0, 183.0, 1.0, 0.4, 0.0, 0.6);
        drawElement(canvas,BitmapFactory.decodeResource(getResources(), R.drawable.game_sand_3_01), 677.0, 136.0, 0.7, 0.1, 0.15, 0.65);
        drawElement(canvas,BitmapFactory.decodeResource(getResources(), R.drawable.game_coconut_tree), 383.0, 396.0, 0.3, 0.3, 0.15, 0.40);

        drawText(canvas);


        //segment to draw all the sprites

        //if no upgrades have been purchased then we only draw the base animation...
        if(upgrades[0][0] == 1){
            sprites[3][0].scaleDraw(canvas);
        }else{

            //if any upgrades have been bought
            //then we iterate through the sprite array and find the latest update for each item
            //that was bought and only draw that items' upgrade. (thats why j runs from 2 -> 0)
            for(int i = 0; i<3; i++){
                for(int j = 2; j >= 0; j-- ){
                    if(upgrades[i][j] == 2 ){
                        sprites[i][j].scaleDraw(canvas);
                     //so we don't draw any items' upgrade that has a newer upgrade on it
                        break;
                    }
                }
            }
        }

        //little mess to get the gainedAnimation objects into a gainedAnimation array
        Object[] temp = (Object[]) this.gainedCocos.toArray();
        gainedAnimation[] gainedFylki = new gainedAnimation[temp.length];
        if(temp.length != 0){
            for(int i = 0; i<temp.length; i++){
                gainedFylki[i]= (gainedAnimation) temp[i];
            }
        }

        //check if the gainedAnimation array is empty
        if(gainedFylki.length != 0){
         //if not then we iterate through it and draw each gainedAnimation object
            for(int i = 0; i<gainedFylki.length; i++){
                gainedFylki[i].draw(canvas);
            }
        }
        //...and then we restore the state. this is done so the screen doesn't scale infinitely


    }

    public void drawText(Canvas canvas){
        //stuff to draw text onto the canvas
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize((int)((canvas.getWidth()*0.4)/(7.0)));

        //a segment to format the onscreen score and currency to a format of for example 10.25M instead of 10250000
        String curr;

        //our string format
        DecimalFormat df = new DecimalFormat("#.##");

        //check if currency is between 1000 and 1.000.000
        if(this.userData.getCurrency() >= 1000 && this.userData.getCurrency() < 1000000){

            //if so then give the give the currency string the suffix "K"
            String currency = df.format(((double)this.userData.getCurrency() /(double) 1000));
            curr = "Coconuts: "+currency+" K";

        }else if(this.userData.getCurrency() >= 1000000){

            //else if the currency is larger than 1.000.000
            //then give the give the currency string the suffix "M"
            String currency = df.format(((double)this.userData.getCurrency() /(double) 1000000));
            curr = "Coconuts: "+currency+" M";
        }else{

            //if the currency is smaller than 1.000 then we do nothing
            curr = "Coconuts: "+this.userData.getCurrency()+"";
        }

        int relocate = ((int)((canvas.getWidth()*0.4)/(10.0))/20);

        //draw the formatted version of the players currency
        canvas.drawText(curr, 20, getHeight()-(40*relocate), paint);

        //segment to draw the score
        paint.setColor(Color.WHITE);
        String score = "Score: "+this.userData.getScore()+"";
        canvas.drawText(score, 20, getHeight() - 10, paint);
    }



    public void drawElement(Canvas canvas, Bitmap img, double imgWidth, double imgHeight, double xSize, double ySize, double xPos, double yPos) {

        //Draw the ocean
        float scaleFactorX = (float) ((canvas.getWidth() * xSize) / (imgWidth));
        float scaleFactorY = (float) ((canvas.getHeight() * ySize) / (imgHeight));

        double elementPosX = (canvas.getWidth() * xPos) / scaleFactorX;
        double elementPosY = (canvas.getHeight() * yPos) / scaleFactorY;

        //make sure the canvas exists
        if (canvas != null) {

            //save the state for scaling...
            int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);

            //draw the background image


            canvas.drawBitmap(img, (int) elementPosX, (int) elementPosY + 1, null);
            canvas.restoreToCount(savedState);
        }
    }

}
