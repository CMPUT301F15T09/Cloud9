package com.example.yunita.tradiogc.trade;

import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.inventory.Item;

public class CompletedTrade extends Trade{

    public CompletedTrade(String owner, String borrower, Item ownerItem, Inventory borrowerItems) {
        super(owner, borrower, ownerItem, borrowerItems);
    }

    @Override
    public String toString(){
        return getOwner() + " and you have completed the trade for " +  getOwnerItem().getName();
    }

}
