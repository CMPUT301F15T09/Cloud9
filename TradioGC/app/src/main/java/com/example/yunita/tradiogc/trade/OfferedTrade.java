package com.example.yunita.tradiogc.trade;

import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.inventory.Item;

public class OfferedTrade extends Trade {

    public OfferedTrade(String borrower, Item ownerItem, Inventory borrowerItems) {
        super(borrower, ownerItem, borrowerItems);
    }

    @Override
    public String toString(){
        return getBorrower() + " offered a trade for " + getOwnerItem().getName();
    }

}
