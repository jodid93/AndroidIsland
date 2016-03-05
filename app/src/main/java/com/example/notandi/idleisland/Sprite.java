package com.example.notandi.idleisland;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Notandi on 3.3.2016.
 */
public class Sprite {

    private Bitmap[] frames;
    private int numberOfFrames;
    private int frameWidth;
    private int frameHeight;
    private int x,y;
    private double animationSpeed;
    private double currentAnimationTime = 0.0;
    private int displayFrame;
    private boolean shouldAnimate;
    private boolean animateOnce;
    private boolean stopflag;

    public Sprite(Bitmap spriteSheet, int numFrames, int frameWidth, int frameHeight, int x, int y, int animationSpeed, boolean shouldAnimate){

        //this.animationSpeed er tími í millisec hvað hver rammi á að vera lengi
        numberOfFrames = numFrames;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.x = x;
        this.y = y;
        this.animationSpeed = ((double)animationSpeed)/((double)numFrames);
        this.shouldAnimate = shouldAnimate;
        frames = new Bitmap[numFrames];

        for (int i = 0; i < frames.length; i++)
        {
            frames[i] = Bitmap.createBitmap(spriteSheet, i*frameWidth, 0, frameWidth, frameHeight);
        }
    }

    public void update(double dt){
        if(this.shouldAnimate){

            this.currentAnimationTime += dt;
            if(this.currentAnimationTime > (this.animationSpeed * this.numberOfFrames)){
                this.currentAnimationTime = 0;
            }

            double percentageDone = (this.currentAnimationTime/(this.animationSpeed*this.numberOfFrames));
            this.displayFrame = (int)( percentageDone * this.numberOfFrames);
            System.out.println("% = "+percentageDone);
            if(displayFrame == numberOfFrames-1){
                this.stopflag = true;
            }
            if( this.stopflag && displayFrame == 0 && animateOnce){
                System.out.println("slokkva");
                this.displayFrame = 0;
                this.currentAnimationTime = 0.0;
                this.shouldAnimate = false;
                animateOnce = false;
                this.stopflag = false;
            }
        }

    }

    public void draw(Canvas canvas){

        canvas.drawBitmap(frames[this.displayFrame], this.x, this.y,null);
    }

    public void loopOnce(){

        this.shouldAnimate = true;
        this.animateOnce = true;

    }

}