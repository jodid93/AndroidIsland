package com.example.notandi.idleisland;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Notandi on 15.3.2016.
 */
public class gainedAnimation {

    private int lifetime = 2000;
    private String gained;
    private double x,y;
    private double deltaX;
    private double deltaY = -0.2;

    public gainedAnimation(String gained){
        this.gained = gained;
        x = 100.0;
        y = 150.0;

        if(Math.random() < 0.5){
            this.deltaX = Math.random()*0.5;
        }else{
            this.deltaX = -Math.random()*0.5;
        }
    }

    public int update(double dt){
        this.x += deltaX*dt;
        this.y += deltaY*dt;
        if(this.y < 0){
            //kill me
            return -1;
        }
        return 1;
    }

    public String herna(){
        return "HALLO!!!!";
    }

    public void draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        canvas.drawText(this.gained, (int)this.x, (int)this.y, paint);
    }
}
