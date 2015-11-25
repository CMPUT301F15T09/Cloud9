package com.example.yunita.tradiogc.notification;


import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.inventory.Item;
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
        if(trade.getStatus().equals("offered")){
            str += trade.getBorrower() + " offered a trade for " + trade.getOwnerItem().getName();
        } else if (trade.getStatus().equals("completed")){
            str += trade.getBorrower() + " completed the trade for " + trade.getOwnerItem().getName();
        } else if (trade.getStatus().equals("accepted")){
            str += trade.getOwner() + " accepted your trade for " + trade.getOwnerItem().getName();
        } else if(trade.getStatus().equals("declined")) {
            str += trade.getOwner() + " declined your trade for " + trade.getOwnerItem().getName();
        }

        return str;
    }

}
