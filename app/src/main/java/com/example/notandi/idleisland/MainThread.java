package com.example.notandi.idleisland;

/**
 * Created by Notandi on 18.2.2016.
 */
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread
{
    private int FPS = 30;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    //private IdleIsland idleIsland;
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
    public void run()
    {
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount =0;
        long targetTime = 1000/FPS;

        long deltaTime;
        long newTime;
        long oldTime = System.nanoTime();

        while(running) {

            newTime = System.nanoTime();
            deltaTime = newTime - oldTime;
            double DoubleDeltaTime = (double)deltaTime/(double)1000.0;

            startTime = System.nanoTime();
            canvas = null;

            //try locking the canvas for pixel editing
            try {
                canvas = this.surfaceHolder.lockCanvas();
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

            oldTime = System.nanoTime();




            /*timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime-timeMillis;

            try{
                this.sleep(waitTime);
            }catch(Exception e){}

            totalTime += System.nanoTime()-startTime;
            frameCount++;
            if(frameCount == FPS)
            {
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount =0;
                totalTime = 0;
                System.out.println(averageFPS);
            }*/
        }
    }
    public void setRunning(boolean b)
    {
        running=b;
    }
}
