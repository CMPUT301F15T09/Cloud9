package com.example.yunita.tradiogc.market;

import java.util.ArrayList;

/**
 * This class defines an inventory search.
 */
public class SearchInventory extends ArrayList<SearchItem> {

    /**
     * Class constructor for searching inventories.
     */
    public SearchInventory() {
    }

    /**
     * Sets the current search inventory by the specified category.
     *
     * @param searchInventory friends' inventories
     * @param category        item category to be searched by
     */
    public void setInventoryByCategory(SearchInventory searchInventory, int category) {
        for (SearchItem searchItem : searchInventory) {
            if (searchItem.getoItem().getCategory() == category) {
                this.add(searchItem);
            }
        }
    }

    /**
     * Sets the current search inventory by the specified item's name.
     *
     * @param searchInventory friends' inventories
     * @param search          item's name
     */
    public void setInventoryByQuery(SearchInventory searchInventory, String search) {
        for (SearchItem searchItem : searchInventory) {
            if (searchItem.getoItem().getName().toLowerCase().contains(search.toLowerCase())) {
                this.add(searchItem);
            }
        }
    }

}
