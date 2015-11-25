package com.example.yunita.tradiogc.trade;

import android.content.Context;

import com.example.yunita.tradiogc.WebServer;
import com.google.gson.Gson;

public class TradeController {
    private static final String TAG = "TradeController";
    private Gson gson;
    private WebServer webServer = new WebServer();
    private Context context;

    public TradeController(Context context){
        this.context = context;
    }


}
