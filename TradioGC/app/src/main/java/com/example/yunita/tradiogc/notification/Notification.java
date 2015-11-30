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
     * Class constructor specifying details of notifications.
     */
    public Notification(Trade trade){
        this.trade = new Trade(trade);
        read = false;
    }

    /**
     * Gets a trade that was offered.
     *
     * @return trade trade that was offered to the owner
     */
    public Trade getTrade() {
        return trade;
    }

    /**
     * Changes a trade that was offered.
     *
     * @param trade trade that has been modified
     */
    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    /**
     * Changes whether or not the notification has been read.
     *
     * @param read  read or unread status of the notification
     */
    public void setRead(boolean read) {
        this.read = read;
    }

    /**
     * Returns the printing format of the notification.
     * <p>The format of the notification is [owner/borrower] offered a [trade/counter trade]
     * for [item].
     *
     * @return String   contains the name of the person offering the
     *                  trade/counter trade and the item involved
     */
    @Override
    public String toString(){
        String str = "";
        if (!read) {
            str += "[ New! ] ";
        }
        if (LoginActivity.USERLOGIN.getUsername().equals(trade.getOwner())) {
            str += trade.getBorrower() + " offered a trade for " + trade.getOwnerItem().getName();
        } else if (LoginActivity.USERLOGIN.getUsername().equals(trade.getBorrower())) {
            str += trade.getOwner() + " offered a counter trade for " + trade.getOwnerItem().getName();
        }
        return str;
    }
}