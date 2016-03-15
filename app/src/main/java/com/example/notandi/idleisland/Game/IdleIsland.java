package com.example.notandi.idleisland.Game;

/**
 * Created by Notandi on 18.2.2016.
 */

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.notandi.idleisland.R;
import com.example.notandi.idleisland.User.UserData;

import java.text.DecimalFormat;
import java.util.Vector;


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
        background = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.main_background));

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

    Vector<gainedAnimation> gainedCocos = new Vector<gainedAnimation>();

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == 0){ //ACTION DOWN
            if(this.userData.getUpgrades(this.level).getUpgrades()[0][0] != 2){

                sprites[3][0].loopOnce();
            }

            gainedCocos.add(new gainedAnimation(Integer.toString((int)(1 * this.userData.getTreeFactor()))));

            userData.setCurrency(this.userData.getCurrency() + (int)(1 * this.userData.getTreeFactor()));
            int scoreid = this.userData.getScore() + (int)(1 * this.userData.getTreeFactor());
            this.userData.setScore(scoreid);

            System.out.println("currency:  " + userData.getCurrency());
        }

        return true;

    }

    private double secCounter = 0.0;
    private int gainedInSec = 0;
    public void update(double dt)
    {
        secCounter += dt;

        int currentCurrency = this.userData.getCurrency();
        int currency = this.calculator.calculateCurrency(dt, this.userData.getCurrency(), this.userData.getCurrFactor());
        int gained = currency - currentCurrency;
        int scoreid = this.userData.getScore() + gained;
        this.userData.setScore(scoreid);

        if(secCounter > 1000.0){
            gainedInSec += gained;
            if(gainedInSec >= 1){
                gainedCocos.add(new gainedAnimation(Integer.toString(gainedInSec)));
            }
            secCounter = 0.0;
            gainedInSec  = 0;
        }else{

            gainedInSec += gained;
        }

        this.userData.setCurrency(gained + currentCurrency);
        Object[] temp = (Object[]) this.gainedCocos.toArray();
        gainedAnimation[] gainedFylki = new gainedAnimation[temp.length];
        if(temp.length != 0){
            for(int i = 0; i<temp.length; i++){
                gainedFylki[i]= (gainedAnimation) temp[i];
            }
        }
        if(gainedFylki.length != 0){
            for(int i = 0; i<gainedFylki.length; i++){
                if(gainedFylki[i].update(dt) == -1){
                    this.gainedCocos.removeElementAt(i);
                }
            }
        }
        if(upgrades[0][0] == 1){
            sprites[3][0].update(dt);
        }else{

            for(int i = 0; i<3; i++){
                for(int j = 2; j >= 0; j-- ){
                    if(upgrades[i][j] == 2 ){
                        sprites[i][j].update(dt);
                        break;
                    }
                }
            }
        }
    }



    @Override
    public void draw(Canvas canvas){

        super.draw(canvas);
        final float scaleFactorX = getWidth()/540;
        final float scaleFactorY = getHeight()/584;
        if(canvas!=null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            background.draw(canvas);

            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(20);
            String curr;
            DecimalFormat df = new DecimalFormat("#.##");
            if(this.userData.getCurrency() >= 1000 && this.userData.getCurrency() < 1000000){
                String currency = df.format(((double)this.userData.getCurrency() /(double) 1000));
                curr = "Coconuts: "+currency+" K";

            }else if(this.userData.getCurrency() >= 1000000){
                String currency = df.format(((double)this.userData.getCurrency() /(double) 1000000));
                curr = "Coconuts: "+currency+" M";
            }else{
                curr = "Coconuts: "+this.userData.getCurrency()+"";
            }
            canvas.drawText(curr, 20,  (int)(getHeight()/scaleFactorY)-10, paint);

            paint.setColor(Color.WHITE);
            paint.setTextSize(20);

            String score = "Score: "+this.userData.getScore()+"";
            canvas.drawText(score, 20, (int) (getHeight() / scaleFactorY) - 40, paint);

            if(upgrades[0][0] == 1){
                sprites[3][0].draw(canvas);
            }else{

                for(int i = 0; i<3; i++){
                    for(int j = 2; j >= 0; j-- ){
                        if(upgrades[i][j] == 2 ){
                            sprites[i][j].draw(canvas);
                            break;
                        }
                    }
                }
            }
            Object[] temp = (Object[]) this.gainedCocos.toArray();
            gainedAnimation[] gainedFylki = new gainedAnimation[temp.length];
            if(temp.length != 0){
                for(int i = 0; i<temp.length; i++){
                    gainedFylki[i]= (gainedAnimation) temp[i];
                }
            }
            if(gainedFylki.length != 0){
                for(int i = 0; i<gainedFylki.length; i++){
                    gainedFylki[i].draw(canvas);
                }
            }
            canvas.restoreToCount(savedState);
        }
    }


}
