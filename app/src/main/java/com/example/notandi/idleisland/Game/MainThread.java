package com.example.notandi.idleisland.Game;

/**
 * Created by Notandi on 18.2.2016.
 */
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread
{

    private SurfaceHolder surfaceHolder;
    private boolean running;
    public static Canvas canvas;
    private GameEngine gameEngine;

    public MainThread(SurfaceHolder surfaceHolder, GameEngine gameEngine)
    {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameEngine = gameEngine;
    }
    @Override
    public void run(){

        long deltaTime;
        long newTime;
        long oldTime = System.nanoTime();

        while(running) {
            newTime = System.nanoTime();
            deltaTime = newTime - oldTime;
            double DoubleDeltaTime = (double)deltaTime/(double)1000000.0;
            //System.out.println("oldTime: "+oldTime+"   newTime: "+newTime+"   doubleDeltaTime: "+DoubleDeltaTime+"   deltaTime: "+deltaTime);

            canvas = null;

            //try locking the canvas for pixel editing
            try {
                canvas = this.surfaceHolder.lockCanvas();;
                synchronized (surfaceHolder) {
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

            oldTime = newTime;

        }
    }
    public void setRunning(boolean b)
    {
        running=b;
    }
}
