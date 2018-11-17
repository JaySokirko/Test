package com.jay.test.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class InternetConnection {

    /**
     * Checks if there is an internet connection.
     * @param context application context
     * @return true if an internet enabled
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return (cm != null ? cm.getActiveNetworkInfo() : null) != null;
    }
}
