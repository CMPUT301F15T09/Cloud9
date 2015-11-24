package com.example.yunita.tradiogc.trade;


import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.inventory.Item;

import java.util.Random;

public class Trade {

    private String owner = "";
    private String borrower = "";
    private Item ownerItem = new Item();
    private Inventory borrowerItems = new Inventory();
    private String status = ""; //offered, pending, accepted, approved, declined, completed
    private int id;
    private boolean read = true;


    public Trade(){
        Random random = new Random();
        id = random.nextInt(999999999);
    }

    /**
     * copy a trade
     */
    public Trade(Trade trade){
        owner = trade.getOwner();
        borrower = trade.getBorrower();
        ownerItem = trade.getOwnerItem();
        borrowerItems = trade.getBorrowerItems();
        id = trade.getId();
        status = trade.getStatus();
        read = false;
    }

    public Trade(String owner, Item ownerItem, Inventory borrowerItems) {
        this.owner = owner;
        this.ownerItem = ownerItem;
        this.borrowerItems = borrowerItems;
        Random random = new Random();
        id = random.nextInt(999999999);
    }

    public Trade(String owner, String borrower, Item ownerItem, Inventory borrowerItems) {
        this.owner = owner;
        this.borrower = borrower;
        this.ownerItem = ownerItem;
        this.borrowerItems = borrowerItems;
        Random random = new Random();
        id = random.nextInt(999999999);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        // offered, pending, accepted,(declined?), completed
        this.status = status;
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
        if(status.equals("offered")){
            str += borrower + " offered a trade for " + ownerItem.getName();
        } else if (status.equals("completed")){
            // print informative message
        } else if (status.equals("accepted")){
            // print informative message
        } else if(status.equals("pending")) {
            // print informative message
        } else if(status.equals("declined")) {
            // print informative message
        } else if(status.equals("approved")) {
            str += owner + " approved your trade for " + ownerItem.getName();
        }
        if (!read) {
            str = "[New!]" + str;
        }
        return str;
    }

}
