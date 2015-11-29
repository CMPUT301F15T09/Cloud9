package com.example.yunita.tradiogc;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by dshin on 11/17/15.
 */
public class CheckNetwork {

    //http://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html

    private Context context;

    public CheckNetwork(Context context) {
        this.context = context;
    }


    public Boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isAvailable() && activeNetwork.isConnected();
        return isConnected;
    }

}
