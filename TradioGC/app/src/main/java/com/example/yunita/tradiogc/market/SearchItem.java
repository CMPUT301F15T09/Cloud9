package com.example.yunita.tradiogc.market;

import com.example.yunita.tradiogc.inventory.Item;

import java.io.Serializable;

/**
 * This class defines an item search.
 */
public class SearchItem implements Serializable {

    private String ownerName;
    private Item oItem;

    /**
     * Class constructor for searching an item.
     */
    public SearchItem() {

    }

    /**
     * Class constructor specifying the details of the item.
     *
     * @param ownerName owner's name
     * @param oItem     owner's item
     */
    public SearchItem(String ownerName, Item oItem) {
        this.ownerName = ownerName;
        this.oItem = oItem;
    }

    /**
     * Gets the name of the owner.
     *
     * @return ownerName owner's name
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * Changes the name of the owner.
     *
     * @param ownerName new owner name
     */
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    /**
     * Gets the item of the owner.
     *
     * @return oItem owner's item
     */
    public Item getoItem() {
        return oItem;
    }

    /**
     * Changes the item of the owner.
     *
     * @param oItem new owner item
     */
    public void setoItem(Item oItem) {
        this.oItem = oItem;
    }

    /**
     * Return the new printing format of the search item.
     * <p>The format of item is [item name]\n[price]\n[owner]
     *
     * @return String contains the item's name, price, quantity, and owner name
     */
    @Override
    public String toString() {
        return oItem.getName() + "\nPrice: $" + oItem.getPrice() + " x " + oItem.getQuantity() + "\nOwner: " + ownerName;
    }
}
