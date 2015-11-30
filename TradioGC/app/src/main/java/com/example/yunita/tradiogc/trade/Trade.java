package com.example.yunita.tradiogc.trade;


import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.inventory.Item;
import com.example.yunita.tradiogc.login.LoginActivity;

import java.util.Random;

/**
 * This class defines a trade.
 */
public class Trade {

    private String owner = "";
    private String borrower = "";
    private Item ownerItem = new Item();
    private Inventory borrowerItems = new Inventory();
    private String status = "";
    private int id;

    /**
     * Class constructor for a trade.
     */
    public Trade(){
        Random random = new Random();
        id = random.nextInt(999999999);
    }

    /**
     * Class constructor for a trade.
     *
     * @param trade existing trade
     */
    public Trade(Trade trade){
        this.owner = trade.getOwner();
        this.borrower = trade.getBorrower();
        this.ownerItem = trade.getOwnerItem();
        this.borrowerItems = trade.getBorrowerItems();
        this.status = trade.getStatus();
        this.id = trade.getId();
    }

    /**
     * Class constructor for a trade.
     *
     * @param owner         owner of the item in the trade
     * @param ownerItem     item from the owner
     * @param borrowerItems item from the borrower
     */
    public Trade(String owner, Item ownerItem, Inventory borrowerItems) {
        this.owner = owner;
        this.ownerItem = ownerItem;
        this.borrowerItems = borrowerItems;
        Random random = new Random();
        id = random.nextInt(999999999);
    }

    /**
     * Class constructor for a trade.
     *
     * @param owner         owner of the item in the trade
     * @param borrower      borrower of the item in the trade
     * @param ownerItem     item from the owner
     * @param borrowerItems item from the borrower
     */
    public Trade(String owner, String borrower, Item ownerItem, Inventory borrowerItems) {
        this.owner = owner;
        this.borrower = borrower;
        this.ownerItem = ownerItem;
        this.borrowerItems = borrowerItems;
        Random random = new Random();
        id = random.nextInt(999999999);
    }

    /**
     * Gets the id for the trade.
     *
     * @return id id of the trade
     */
    public int getId() {
        return id;
    }

    /**
     * Changes the id for the trade.
     *
     * @param id id of the trade
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the owner name for the trade.
     *
     * @return owner name of the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Changes the owner name for the trade.
     *
     * @param owner name of the owner in the trade
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Gets the borrower name for the trade.
     *
     * @return borrower name of the borrower
     */
    public String getBorrower() {
        return borrower;
    }

    /**
     * Changes the borrower name for the trade.
     *
     * @param borrower name of the borrower in the trade
     */
    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    /**
     * Gets the owner's item to be used in the trade.
     *
     * @return ownerItem owner's item to be traded
     */
    public Item getOwnerItem() {
        return ownerItem;
    }

    /**
     * Gets the borrower items to be used in the trade.
     *
     * @return borrowerItems borrower's items to be traded
     */
    public Inventory getBorrowerItems() {
        return borrowerItems;
    }

    /**
     * Changes the borrower's items to be traded.
     *
     * @param borrowerItems borrower's items to be traded
     */
    public void setBorrowerItems(Inventory borrowerItems) {
        this.borrowerItems = borrowerItems;
    }

    /**
     * Gets the status of the trade.
     *
     * @return status status of the trade
     */
    public String getStatus() {
        return status;
    }

    /**
     * Changes the status of the trade.
     *
     * @param status status of the trade
     */
    public void setStatus(String status) {
        // offered, pending, accepted, declined, completed
        this.status = status;
    }

    /**
     * Returns the printing format for the trade statuses.
     * <p>The format of the trade records are:
     * <p>Offered: [borrower] offered a trade for [owner's item]
     * <p>Completed: Trade for [owner's item] is completed.
     * <p>Accepted: [owner] accepted your trade for [owner's item]
     * <p>Pending: Trade for [owner's item] is pending.
     * <p>Declined: [owner] declined your trade for [owner's item]
     *
     * @return String containing borrower/owner, the items in the trade, and the status
     */
    @Override
    public String toString(){
        String str = "";
        /*
        if(status.equals("offered")){
            str += borrower + " offered a trade for " + ownerItem.getName();
        } else if (status.equals("completed")){
            str += "Trade for " + ownerItem + " is completed.";
        } else if (status.equals("accepted")){
            str += owner + " accepted your trade for " + ownerItem.getName();
        } else if(status.equals("pending")) {
            str += "Trade for " + ownerItem + " is pending.";
        } else if(status.equals("declined")) {
            str += owner + " declined your trade for " + ownerItem.getName();
        }
        */
        if (LoginActivity.USERLOGIN.getUsername().equals(owner)) {
            str += "Trade with " + borrower + "\n" + ownerItem.getName() + "\nStatus:" + status;
        } else {
            str += "Trade with " + owner + "\n" + ownerItem.getName() + "\nStatus: " + status;

        }
        return str;
    }

}
