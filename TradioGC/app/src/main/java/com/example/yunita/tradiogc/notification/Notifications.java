package com.example.yunita.tradiogc.notification;


import com.example.yunita.tradiogc.trade.Trade;

import java.util.ArrayList;

/**
 * This class defines a list of notifications for the user.
 */
public class Notifications extends ArrayList<Notification> {

    /**
     * Class constructor for notifications.
     */
    public Notifications() {}

    /**
     * Adds notifications onto the user's notification list.
     *
     * @param trade trade to be added to the notification list
     */
    public void addNotification(Trade trade) {
        this.add(0, new Notification(trade));
    }

    /**
     * Searches and returns a notification on the user's notification list.
     *
     * @param id index of the trade on the user's notification list
     * @return notification notification pressed by the user
     */
    public Notification findNotificationById(int id) {
        for (Notification notification : this) {
            if (notification.getTrade().getId() == id) {
                return notification;
            }
        }
        return null;
    }
}