package com.example.yunita.tradiogc.notification;


import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.trade.Trade;

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
            switch (trade.getStatus()) {
                case "offered":
                    str += trade.getBorrower() + " offered a trade for " + trade.getOwnerItem().getName();
                    break;
                case "completed":
                    // print informative message
                    break;
                case "accepted": // the counter trade offered by owner is accepted
                    str += trade.getBorrower() + " accepted your counter trade for " + trade.getOwnerItem().getName();
                    break;
                case "pending":
                    // print informative message
                    break;
                case "declined": // the counter trade offered by owner is declined
                    str += trade.getBorrower() + " declined your counter trade for " + trade.getOwnerItem().getName();
                    break;
                case "current":
                    // print informative message
                    break;
            }
        } else if (LoginActivity.USERLOGIN.getUsername().equals(trade.getBorrower())) {
            switch (trade.getStatus()) {
                case "offered": // login user is offered a counter trade
                    str += trade.getOwner() + " offered a counter trade to you for " + trade.getOwnerItem().getName();
                    break;
                case "completed":
                    // print informative message
                    break;
                case "accepted":
                    str += trade.getOwner() + " accepted your trade for " + trade.getOwnerItem().getName();
                    break;
                case "pending":
                    // print informative message
                    break;
                case "declined":
                    str += trade.getOwner() + " declined your trade for " + trade.getOwnerItem().getName();
                    break;
                case "current":
                    // print informative message
                    break;
            }
        }
        return str;
    }

}
