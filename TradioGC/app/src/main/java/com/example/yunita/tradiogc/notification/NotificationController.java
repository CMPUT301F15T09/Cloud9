package com.example.yunita.tradiogc.notification;


import android.content.Context;

import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.trade.Trade;
import com.example.yunita.tradiogc.user.UserController;

public class NotificationController {
    private Context context;
    private UserController userController;

    public NotificationController(Context context) {
        this.context = context;
        userController = new UserController(context);
    }

    public void updateNotification() {
        for (Trade trade: LoginActivity.USERLOGIN.getTrades()) {
            if (trade.getStatus().equals("offered")
                    || trade.getStatus().equals("approved")) {
                if (LoginActivity.USERLOGIN.getNotifications().findTradeById(trade.getId()) == null) {
                    LoginActivity.USERLOGIN.getNotifications().add(new Trade(trade));
                }
            }
        }
        updateToWebServer();
    }

    public void updateToWebServer() {
        Thread updateUserThread = userController.new UpdateUserThread(LoginActivity.USERLOGIN);
        updateUserThread.start();
    }
}
