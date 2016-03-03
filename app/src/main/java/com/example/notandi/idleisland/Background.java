package com.example.notandi.idleisland;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Notandi on 3.3.2016.
 */
public class Background {
    private Bitmap image;
    private float x, y;

    public Background(Bitmap res)
    {
        image = res;
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(image, x, y,null);

    }
}
