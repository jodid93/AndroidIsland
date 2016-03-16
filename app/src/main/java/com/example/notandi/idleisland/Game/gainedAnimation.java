package com.example.notandi.idleisland.Game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Notandi on 15.3.2016.
 * class used to animate the numbers that pop up from the tree when you gain currency
 *
 * DRAWBACK:
 *          The animations are ugly since they don't yet have "gravity"
 */
public class gainedAnimation {

    private int lifetime = 2000;
    private String gained;  //The string amount that is supposed to be animated
    private double x,y; // current x, y pos of the animated String
    private double deltaX; //difference in x movement for each iteration
    private double deltaY = -0.2; //difference in y movement for each iteration

    //constructor
    public gainedAnimation(String gained){
        this.gained = gained;

        //initial coordinates for the animation (Top of Tree)
        x = 100.0;
        y = 150.0;

        //get a random direction for the animation
        if(Math.random() < 0.5){
            this.deltaX = Math.random()*0.5;
        }else{
            this.deltaX = -Math.random()*0.5;
        }
    }

    /*
        method to update the location of the animated String according to the time delta provided

        the method returns -1 if the "this" object is to be destroyed otherwise it returns 1
     */
    public int update(double dt){

        //update coordinates
        this.x += deltaX*dt;
        this.y += deltaY*dt;

        //check if the text has left the screen
        if(this.y < 0){
            //kill me
            return -1;
        }
        return 1;
    }

    /*
        method to draw the this.gained Text onto the canvas
     */
    public void draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);

        //use the latest x,y to draw
        canvas.drawText(this.gained, (int)this.x, (int)this.y, paint);
    }
}
