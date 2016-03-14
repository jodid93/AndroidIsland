package com.example.notandi.idleisland;

/**
 * Created by Notandi on 18.2.2016.
 */

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class IdleIsland extends SurfaceView implements SurfaceHolder.Callback
{
    private MainThread thread;
    private Calculator calculator;
    private UserData userData;
    private int level;
    private int [][] upgrades;

    private Background background;
    private Sprite[][] sprites;

    public IdleIsland(Context context, GameEngine engine, Calculator calculator, UserData userData, int level, Sprite[][] sprites)
    {
        super(context);


        this.sprites = sprites;
        this.level = level;
        this.calculator = calculator;
        this.userData = userData;
        this.upgrades = this.userData.getUpgrades(level).getUpgrades();
        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(),engine);

        //make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

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

        System.out.println("nu mattu yta a takka");
        background = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.game_coconut_tree));

        Upgrades upgradesT = this.userData.getUpgrades(this.level);
        this.upgrades = upgradesT.getUpgrades();

        //we can safely start the game loop
        thread.setRunning(true);
        if (thread.getState() == Thread.State.NEW)
        {
            thread.start();
        }
        System.out.println("thradur startadur");

    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == 0){ //ACTION DOWN
            System.out.println("turn on animation");
            if(this.userData.getUpgrades(this.level).getUpgrades()[0][0] != 2){

                sprites[3][0].loopOnce();
            }


            userData.setCurrency( this.userData.getCurrency() + (int)(1 * this.userData.getTreeFactor()));

            System.out.println("currency:  " + userData.getCurrency());
        }

        return true;

    }


    //TODO: lata update og render fara ofugt i gegnum updates til ad rendera
    public void update(double dt)
    {
        int currentCurrency = this.userData.getCurrency();
        int currency = this.calculator.calculateCurrency(dt, this.userData.getCurrency(), this.userData.getCurrFactor());
        int gained = currency - currentCurrency;

        this.userData.setCurrency(gained + currentCurrency);
        System.out.println("Currency: "+this.userData.getCurrency());

        if(upgrades[0][0] == 1){
            sprites[3][0].update(dt);
        }else{

            for(int i = 0; i<3; i++){
                for(int j = 0; j < 3; j++ ){
                    if(upgrades[i][j] == 2 ){
                        sprites[i][j].update(dt);
                    }
                }
            }
        }
    }

    @Override
    public void draw(Canvas canvas){

        super.draw(canvas);
        final float scaleFactorX = getWidth()/383;
        final float scaleFactorY = getHeight()/396;
        if(canvas!=null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            background.draw(canvas);


            if(upgrades[0][0] == 1){
                sprites[3][0].draw(canvas);
            }else{

                for(int i = 0; i<3; i++){
                    for(int j = 0; j < 3; j++ ){
                        if(upgrades[i][j] == 2 ){
                            sprites[i][j].draw(canvas);
                        }
                    }
                }
            }
            canvas.restoreToCount(savedState);
        }
    }


}
