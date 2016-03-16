package com.example.notandi.idleisland;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

// IN GENERAL ...
//  Util contains global and reusable variables/methods
//  for the hole app.

/**
 * Created by thorgeir on 15.3.2016.
 */
public class Util {
    public static Boolean offlineMode = true;

    public static boolean isOnline( Context context ) {
        if( !offlineMode ){
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        } else return false;
    }

}
