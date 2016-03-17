package com.example.notandi.idleisland.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

// IN GENERAL ...
//  NetworkUtil contains global and reusable variables/methods
//  for the whole app.

/**
 * Created by thorgeir on 15.3.2016.
 */
public class NetworkUtil {
    public static Boolean offlineMode = false;

    public static boolean isOnline( Context context ) {
        if( !offlineMode ){
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        } else return false;
    }
}
