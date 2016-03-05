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

    public Sprite(Bitmap spriteSheet, int numFrames, int frameWidth, int frameHeight, int x, int y, int animationSpeed){

        //this.animationSpeed er tími í millisec hvað hver rammi á að vera lengi
        numberOfFrames = numFrames;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.x = x;
        this.y = y;
        this.animationSpeed = ((double)animationSpeed)/((double)numFrames);
        frames = new Bitmap[numFrames];

        for (int i = 0; i < frames.length; i++)
        {
            frames[i] = Bitmap.createBitmap(spriteSheet, i*frameWidth, 0, frameWidth, frameHeight);
        }
    }

    public void update(double dt){
        this.currentAnimationTime += dt;
        if(this.currentAnimationTime > (this.animationSpeed * this.numberOfFrames)){
            this.currentAnimationTime = 0;
        }

        double percentageDone = (this.currentAnimationTime/(this.animationSpeed*this.numberOfFrames));
        this.displayFrame = (int)( percentageDone * this.numberOfFrames);


    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(frames[this.displayFrame], this.x, this.y,null);
    }
}
