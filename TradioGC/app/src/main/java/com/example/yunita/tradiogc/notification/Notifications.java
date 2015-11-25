package com.example.yunita.tradiogc.notification;


import com.example.yunita.tradiogc.trade.Trade;

import java.util.ArrayList;

public class Notifications extends ArrayList<Notification> {

    public Notifications() {}

    public void addNotification(Trade trade) {
        this.add(new Notification(trade));
    }

    public Notification findNotificationById(int id) {
        for (Notification notification : this) {
            if (notification.getTrade().getId() == id) {
                return notification;
            }
        }
        return null;
    }
}
