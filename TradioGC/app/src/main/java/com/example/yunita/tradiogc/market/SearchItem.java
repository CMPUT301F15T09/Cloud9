package com.example.yunita.tradiogc.market;

import com.example.yunita.tradiogc.inventory.Item;

import java.io.Serializable;

public class SearchItem implements Serializable {

    private String ownerName;
    private Item oItem;

    /**
     * Class constructor.
     */
    public SearchItem() {

    }

    /**
     * Class constructor specifying the details of the object.
     *
     * @param ownerName owner name.
     * @param oItem     owner item.
     */
    public SearchItem(String ownerName, Item oItem) {
        this.ownerName = ownerName;
        this.oItem = oItem;
    }

    /**
     * Gets the name of the owner.
     *
     * @return String
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * Changes the name of the owner.
     *
     * @param ownerName new owner name.
     */
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    /**
     * Gets the item of the owner.
     *
     * @return Item
     */
    public Item getoItem() {
        return oItem;
    }

    /**
     * Changes the item of the owner.
     *
     * @param oItem new owner item.
     */
    public void setoItem(Item oItem) {
        this.oItem = oItem;
    }

    /**
     * Return the new printing format of the search item.
     * <p>The new format of item is [item name]\n[price]\n[owner]
     *
     * @return String
     */
    @Override
    public String toString() {
        return oItem.getName() + "\nPrice: $" + oItem.getPrice() + "\nOwner: " + ownerName;
    }
}
