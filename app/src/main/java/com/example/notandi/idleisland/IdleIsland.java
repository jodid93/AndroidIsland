package com.example.notandi.idleisland;

/**
 * Created by Notandi on 18.2.2016.
 */

import android.content.Context;
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
    public IdleIsland(Context context, GameEngine engine, Calculator calculator, UserData userData, int level)
    {
        super(context);
        this.level = level;
        this.calculator = calculator;
        this.userData = userData;
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

    @Override
    public void surfaceCreated(SurfaceHolder holder){

        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();

    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return super.onTouchEvent(event);
    }



    public void update(double dt)
    {
        System.out.println(dt);

        int currentCurrency = this.userData.getCurrency();
        int currency = this.calculator.calculateCurrency(dt, this.userData.getCurrency(), this.userData.getCurrFactor());
        int gained = currency - currentCurrency;



    }


}
