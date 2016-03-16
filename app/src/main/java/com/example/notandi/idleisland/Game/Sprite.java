package com.example.notandi.idleisland.Game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Notandi on 3.3.2016.
 * this class is responsible for rendering and updating the sprite
 * as well as creating a sprite from a static image
 */
public class Sprite {

    private Bitmap[] frames;                    //each frame of the animation as a bitmap
    private int numberOfFrames;                 //basic
    private int frameWidth;                     //basic
    private int frameHeight;                    //basic
    private int x,y;                            //the top left position of each frame in frames
    private double animationSpeed;              //the time in milliseconds each frame should take
    private double currentAnimationTime = 0.0;  //variable used to select the correct frame for each moment
    private int displayFrame;                   //reference into the frames array

    private boolean shouldAnimate;              //boolean to allow the loopOnce method in IdleIsland
    private boolean animateOnce;                //boolean to allow the loopOnce method in IdleIsland
    private boolean stopflag;                   //boolean to allow the loopOnce method in IdleIsland
    private Bitmap map;                         //a reference to the sprite sheet itself

    public Sprite(Bitmap spriteSheet, int numFrames, int frameWidth, int frameHeight, int x, int y, int animationSpeed, boolean shouldAnimate){

        //animationSpeed = the time in milliseconds it should take the sprite to loop
        //this.animationSpeed = see above

        //initialize the variables
        this.map = spriteSheet;
        numberOfFrames = numFrames;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.x = x;
        this.y = y;
        this.animationSpeed = ((double)animationSpeed)/((double)numFrames);
        this.shouldAnimate = shouldAnimate;

        //segment to create each frame from sprite sheet

        //initialize frames[]
        frames = new Bitmap[numFrames];

        //for each frame create that frame with the appropriate size
        for (int i = 0; i < frames.length; i++)
        {
            if(i*frameWidth >= spriteSheet.getWidth()-frameWidth-1){
                frames[i] = Bitmap.createBitmap(spriteSheet,spriteSheet.getWidth()-frameWidth-1 , 0, frameWidth, frameHeight);
            }else{
                frames[i] = Bitmap.createBitmap(spriteSheet, i*frameWidth, 0, frameWidth, frameHeight);
            }
        }
    }

    /*
        method to update the sprite if it should animate at all

        dt is the time in millisecond since the last iteration of the gameloop
     */
    public void update(double dt){

        //if this sprite should animate...
        if(this.shouldAnimate){

            //...then we update the current time
            this.currentAnimationTime += dt;

            //if the time is indicates that the sprite should have looped...
            if(this.currentAnimationTime > (this.animationSpeed * this.numberOfFrames)){

                //then we reset the time counter
                this.currentAnimationTime = 0;
            }

            //find out what frame to display
            double percentageDone = (this.currentAnimationTime/(this.animationSpeed*this.numberOfFrames)); //pretty straight forward
            this.displayFrame = (int)( percentageDone * this.numberOfFrames); //get the reference to the right frame in frames[]

            //if the sprite has looped once...
            if(displayFrame == numberOfFrames-1){

                //...then activate the stopflag (if the sprite was only supposed to animate once this will stop it)
                this.stopflag = true;
            }

            //the stopping condition for the loopOnce animations
            if( this.stopflag && displayFrame == 0 && animateOnce){

                //reset the sprite to be lifeless
                this.displayFrame = 0;
                this.currentAnimationTime = 0.0;
                this.shouldAnimate = false;
                animateOnce = false;
                this.stopflag = false;
            }
        }

    }

    /*
        method to draw the current frame of the animation cycle for the sprite
     */
    public void draw(Canvas canvas){

        //basic
        canvas.drawBitmap(frames[this.displayFrame], this.x, this.y,null);
    }

    /*
        method to activate the loop once feature. this method is called upon by IdleIsland when
        we only want to animate the base sprite for one cycle
     */
    public void loopOnce(){

        this.shouldAnimate = true;
        this.animateOnce = true;

    }

}