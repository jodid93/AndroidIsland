package com.example.notandi.idleisland.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by thorgeir on 17.3.2016.
 */
public class display{
    public static void info(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
