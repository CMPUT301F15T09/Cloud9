package com.example.yunita.tradiogc.notification;


import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.trade.Trade;

/**
 * This class defines a notification.
 */
public class Notification  {
    private Trade trade;
    private boolean read;

    /**
     * copy a trade into notification
     */
    public Notification(Trade trade){
        this.trade = new Trade(trade);
        read = false;
    }

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }


    @Override
    public String toString(){
        String str = "";
        if (!read) {
            str += "[ New! ] ";
        }
        if (LoginActivity.USERLOGIN.getUsername().equals(trade.getOwner())) {
            str += trade.getBorrower() + " offered a trade for " + trade.getOwnerItem().getName() + " in your inventory";
        } else if (LoginActivity.USERLOGIN.getUsername().equals(trade.getBorrower())) {
            str += trade.getOwner() + " offered a counter trade for " + trade.getOwnerItem().getName();
        }
        return str;
    }

}
