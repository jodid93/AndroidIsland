package com.example.notandi.idleisland.Game;

/**
 * Created by Notandi on 18.2.2016.
 * this class is the gameloop itself. it runs in the background of the game and keeps track of time
 */
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread
{

    private SurfaceHolder surfaceHolder;
    private boolean running;                //is the game running
    public static Canvas canvas;            //for GameEngine.IdleIsland.draw(canvas)
    private GameEngine gameEngine;          //reference to this class's caller

    //constructor
    public MainThread(SurfaceHolder surfaceHolder, GameEngine gameEngine)
    {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameEngine = gameEngine;
    }

    /*
        method that is the actual engine of our game
     */
    @Override
    public void run(){

        long deltaTime;                     //time difference since last loop of the game engine
        long newTime;                       //latest time of the game loop
        long oldTime = System.nanoTime();   //first time that the game loop works with

        while(running) {

            newTime = System.nanoTime(); //get the current time in nanoseconds
            deltaTime = newTime - oldTime;  //find the delta time from the newTime and oldTime
            double DoubleDeltaTime = (double)deltaTime/(double)1000000.0;   //convert the units to milliseconds

            canvas = null;

            //try locking the canvas for pixel editing
            try {
                canvas = this.surfaceHolder.lockCanvas();;
                synchronized (surfaceHolder) {

                    //call for the updates and draw methods of the game itself
                    //with the new delta time
                    this.gameEngine.update(DoubleDeltaTime);
                    this.gameEngine.draw(canvas);
                }
            } catch (Exception e) {
            }
            finally{
                if(canvas!=null)
                {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    catch(Exception e){e.printStackTrace();}
                }
            }

            //so the timeDelta correctly reflects the time each iteration of the loop took
            oldTime = newTime;

        }
    }

    /*
        method to unlock the gameloop
     */
    public void setRunning(boolean b)
    {
        running=b;
    }
}
