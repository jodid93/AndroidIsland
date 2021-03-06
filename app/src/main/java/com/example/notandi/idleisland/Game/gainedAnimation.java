package com.example.notandi.idleisland.Game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.ContextThemeWrapper;

import com.example.notandi.idleisland.R;

/**
 * Created by Notandi on 15.3.2016.
 * class used to animate the numbers that pop up from the tree when you gain currency
 */
public class gainedAnimation {

    private int lifetime = 0;
    private String gained;  //The string amount that is supposed to be animated
    private double x,y; // current x, y pos of the animated String
    private double deltaX; //difference in x movement for each iteration
    private double deltaY = -0.2; //difference in y movement for each iteration
    private double gravity = 0; //gravity pulls the y coord down
    private boolean set = false;

    private int sizeDiff;   //used to render the text and image in sync

    private Bitmap coconut; //image of a coconut that is the background for the String gained

    //constructor
    public gainedAnimation(String gained, Bitmap coconut){
        this.gained = gained;

        //get a random direction for the animation
        if(Math.random() < 0.5){
            this.deltaX = Math.random()*0.3;
        }else{
            this.deltaX = -Math.random()*0.3;
        }

        this.coconut = coconut;
    }

    /*
        method to update the location of the animated String according to the time delta provided

        the method returns -1 if the "this" object is to be destroyed otherwise it returns 1
     */
    public int update(double dt){

        //update coordinates
        this.x += deltaX*dt;
        this.y += deltaY*dt + dt*gravity;
        gravity += 0.07;

        //if the lifetime is over
        lifetime += dt;
        if(lifetime > 2000){
            //kill me
            return -1;
        }

        return 1;
    }

    /*
        method to draw the this.gained Text onto the canvas
     */
    public void draw(Canvas canvas){

        //to get the initial position as a ratio of the screen size
        if(!set){

            int startPosX = (int) ((canvas.getWidth()*0.27));
            int startPosY = (int) ((canvas.getHeight()*0.48));

            this.x = startPosX;
            this.y = startPosY;
            set = true;
        }

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);

        //scale factor for the image and text
        int scale = (int)(this.lifetime * 0.035);

        getScaledBitmap(30+(int)(scale/1.8)); // scale the image
        paint.setTextSize(20+scale);

        // let the letters fade away after ones econd
        if(this.lifetime>1000){
            double alphaFactor = (((double)(this.lifetime - 1000.0) / 10.0) / 100.0);
            paint.setAlpha(250 - (int)(alphaFactor* 250));
        }

        //use the latest x,y to draw with the scaled values
        canvas.drawBitmap(this.coconut, (int)x+(sizeDiff/3), (int) y-37-sizeDiff, null);
        canvas.drawText(this.gained, (int) this.x, (int)this.y, paint);

    }

    /*
        method to scale the coconut image

        after this method is over the image has been scaled and sizeDiff has accumulated
        from the last size to the new size
     */
    private void getScaledBitmap(int scale)
    {
        int sizePrev = coconut.getWidth(); //get old size
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, coconut.getWidth(), coconut.getHeight()), new RectF(0, 0, scale, scale), Matrix.ScaleToFit.CENTER);
        this.coconut =  Bitmap.createBitmap(coconut, 0, 0, coconut.getWidth(), coconut.getHeight(), m, true);
        this.sizeDiff += coconut.getWidth() - sizePrev; //find the sizeDiff from the new size and old size
    }
}
