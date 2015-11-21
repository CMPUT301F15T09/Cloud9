package com.example.yunita.tradiogc.trade;

import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.inventory.Item;

public class DeclinedTrade extends Trade{

    public DeclinedTrade(String owner, String borrower, Item ownerItem, Inventory borrowerItems) {
        super(owner, borrower, ownerItem, borrowerItems);
    }

    public void offerCounterTrade(){
        // do you want to counter trade?
        // if yes, create a new offered trade
        // if no, it is done
    }

    @Override
    public String toString(){
        return getOwner() + " rejected your trade for " +  getOwnerItem().getName();
    }

}
