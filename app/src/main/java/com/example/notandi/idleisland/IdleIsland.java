package com.example.notandi.idleisland;

/**
 * Created by Notandi on 18.2.2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
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

    private Background background;
    private Sprite[][] sprites;

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

        background = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.game_coconut_tree));

        sprites = new Sprite[3][3];
        sprites[0][0] = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.kall_animation),
                                   5, 247, 242, 50, 50, 1000 );

        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();

    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        System.out.print("snerting");
        return super.onTouchEvent(event);
    }



    public void update(double dt)
    {
        int currentCurrency = this.userData.getCurrency();
        int currency = this.calculator.calculateCurrency(dt, this.userData.getCurrency(), this.userData.getCurrFactor());
        int gained = currency - currentCurrency;

        this.userData.setCurrency(gained);

        this.sprites[0][0].update(dt);
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
            sprites[0][0].draw(canvas);
            canvas.restoreToCount(savedState);
        }
    }


}
