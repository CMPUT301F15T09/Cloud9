package com.example.yunita.tradiogc.notification;


import android.content.Context;

import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.trade.Trade;
import com.example.yunita.tradiogc.user.UserController;

/**
 * This controller handles a user's notifications.
 */
public class NotificationController {
    private Context context;
    private UserController userController;

    /**
     * Class constructor specifying that this controller class is a subclass of Context.
     *
     * @param context
     */
    public NotificationController(Context context) {
        this.context = context;
        userController = new UserController(context);
    }

    /**
     * Updates the owner's notifications if a new trade has been offered.
     */
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
            // Add new offered trade into owner's notification
            if (trade.getStatus().equals("offered") ) {
                if (LoginActivity.USERLOGIN.getNotifications().findNotificationById(trade.getId()) == null) {
                    LoginActivity.USERLOGIN.getNotifications().addNotification(trade);
                }
            }
        }

        updateToWebServer();
    }

    /**
     * Updates the notification list to the webserver.
     */
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
