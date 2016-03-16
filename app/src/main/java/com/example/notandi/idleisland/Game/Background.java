package com.example.notandi.idleisland.Game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Notandi on 3.3.2016.
 * class used to display the background image for the game
 *
 * DRAWBAKC:
 *      this class is probably unnecessary since we could initialize this background in IdleIsland.class
 */
public class Background {

    //the image itself
    private Bitmap image;

    //x,y pos of top left corner
    private float x, y;

    public Background(Bitmap res)
    {
        image = res;
    }

    //method to draw the bitmap onto the canvas
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(image, x, y,null);

    }
}
