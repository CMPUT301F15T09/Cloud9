package com.example.yunita.tradiogc.trade;


import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.inventory.Item;

public class Trade {

    private String owner;
    private String borrower;
    private Item ownerItem;
    private Inventory borrowerItems;

    public Trade(){

    }

    public Trade(String owner, Item ownerItem, Inventory borrowerItems) {
        this.owner = owner;
        this.ownerItem = ownerItem;
        this.borrowerItems = borrowerItems;
    }

    public Trade(String owner, String borrower, Item ownerItem, Inventory borrowerItems) {
        this.owner = owner;
        this.borrower = borrower;
        this.ownerItem = ownerItem;
        this.borrowerItems = borrowerItems;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public Item getOwnerItem() {
        return ownerItem;
    }

    public void setOwnerItem(Item ownerItem) {
        this.ownerItem = ownerItem;
    }

    public Inventory getBorrowerItems() {
        return borrowerItems;
    }

    public void setBorrowerItems(Inventory borrowerItems) {
        this.borrowerItems = borrowerItems;
    }

    @Override
    public String toString(){
        String str = "";
        str += "Borrower: " + borrower + "\n";
        str += "Owner: " + owner + "/n";
        str += "Trade: " + ownerItem + "\n";
        str += "Offer: " + borrowerItems + "\n";
        return str;
    }
}
