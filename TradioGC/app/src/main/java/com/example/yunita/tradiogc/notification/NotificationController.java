package com.example.yunita.tradiogc.notification;


import android.content.Context;

import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.trade.Trade;

public class NotificationController {
    private Context context;

    public NotificationController() {

    }

    public void updateNotification() {
        for (Trade trade: LoginActivity.USERLOGIN.getTrades()) {
            if (trade.getStatus().equals("offered")
                    || trade.getStatus().equals("accepted")) {
                LoginActivity.USERLOGIN.getNotifications().add(new Trade(trade));
            }
        }
    }
}
