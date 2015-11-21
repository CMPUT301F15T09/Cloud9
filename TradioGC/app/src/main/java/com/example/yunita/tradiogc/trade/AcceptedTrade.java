package com.example.yunita.tradiogc.trade;

import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.inventory.Item;

public class AcceptedTrade extends Trade {

    public AcceptedTrade(String owner, String borrower, Item ownerItem, Inventory borrowerItems) {
        super(owner, borrower, ownerItem, borrowerItems);
    }

    public void sendEmail(){
        // send email after trade is accepted
    }

    @Override
    public String toString(){
        return getOwner() + " accepted your offer for " + getOwnerItem().getName();
    }
}
