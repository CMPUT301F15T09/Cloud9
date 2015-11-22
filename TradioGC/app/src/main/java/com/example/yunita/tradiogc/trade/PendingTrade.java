package com.example.yunita.tradiogc.trade;

import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.inventory.Item;

public class PendingTrade extends Trade{
    public PendingTrade(String owner, String borrower, Item ownerItem, Inventory borrowerItems) {
        super(owner, borrower, ownerItem, borrowerItems);
    }

    @Override
    public String toString(){
        return super.toString();
    }
}
