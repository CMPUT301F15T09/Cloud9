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
        Thread getUserLoginThread = userController.new GetUserLoginThread(LoginActivity.USERLOGIN.getUsername());
        getUserLoginThread.start();

        synchronized (getUserLoginThread) {
            try {
                getUserLoginThread.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (Trade trade: LoginActivity.USERLOGIN.getTrades()) {
            // add new offered trade into notification
            if (trade.getStatus().equals("offered") ) {
                if (LoginActivity.USERLOGIN.getNotifications().findNotificationById(trade.getId()) == null) {
                    LoginActivity.USERLOGIN.getNotifications().addNotification(trade);
                }
            }
            // add new accepted trade into notification and change the status to "current"
            else if (trade.getStatus().equals("accepted")) {
                if (LoginActivity.USERLOGIN.getNotifications().findNotificationById(trade.getId()) == null) {
                    LoginActivity.USERLOGIN.getNotifications().addNotification(trade);
                    trade.setStatus("current");
                }
            }
            // add new declined trade into notification
            else if (trade.getStatus().equals("declined") ) {
                if (LoginActivity.USERLOGIN.getNotifications().findNotificationById(trade.getId()) == null) {
                    LoginActivity.USERLOGIN.getNotifications().addNotification(trade);
                    trade.setStatus("canceled");
                }
            }
        }

        updateToWebServer();
    }

    public void updateToWebServer() {
        Thread updateUserThread = userController.new UpdateUserThread(LoginActivity.USERLOGIN);
        updateUserThread.start();

        synchronized (updateUserThread) {
            try {
                updateUserThread.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
