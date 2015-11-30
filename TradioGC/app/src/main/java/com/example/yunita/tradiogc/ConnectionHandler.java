package com.example.yunita.tradiogc;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * This class handles the application's internet connection.
 */
public class ConnectionHandler {

    public ConnectionHandler() {

    }

    // taken from http://developer.android.com/training/basics/network-ops/connecting.html

    public void loginHandler(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
        } else {
            // display error
        }
    }

}
