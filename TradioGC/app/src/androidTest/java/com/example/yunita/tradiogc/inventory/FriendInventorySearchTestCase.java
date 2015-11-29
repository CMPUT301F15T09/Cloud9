package com.example.yunita.tradiogc.inventory;

import android.test.ActivityInstrumentationTestCase2;

import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.inventory.Item;
import com.example.yunita.tradiogc.market.SearchInventory;
import com.example.yunita.tradiogc.market.SearchItem;

public class FriendInventorySearchTestCase extends ActivityInstrumentationTestCase2 {

    public FriendInventorySearchTestCase() {
        super(com.example.yunita.tradiogc.MainActivity.class);
    }

    /**
     * Use Case 15
     * 03.01.01, 03.02.01
     * Test for searching a friend's inventory and only displaying public items.
     */
    public void testSearchFriendInventory() {
        Inventory friendInventory = new Inventory();
        Item item1 = new Item(1, "Chapters", 0, 50.00, "chapters gc", true, 1, 0);
        Item item2 = new Item(2, "Bestbuy", 1, 150.00, "bestbuy gc", true, 1, 0);
        friendInventory.add(item1);

        assertTrue(friendInventory.contains(item1));
        assertFalse(friendInventory.contains(item2));
    }

    /**
     * Use Case 16
     * 03.01.02, 03.02.01
     * Test for browsing an friend's inventory by category.
     */
    public void testBrowseInventoryByCategory() {
        // SearchInventory contains owner name and owner item
        SearchInventory market = new SearchInventory();

        Item item1 = new Item(1, "Chapters", 0, 50.00, "chapters gc", true, 1, 0);
        Item item2 = new Item(2, "Bestbuy", 1, 150.00, "bestbuy gc", true, 1, 0);

        market.add(new SearchItem("john", item1));
        market.add(new SearchItem("ann", item2));

        SearchInventory searchByCategory = new SearchInventory();
        searchByCategory.setInventoryByCategory(market, 0);

        assertEquals(searchByCategory.size(), 1);
    }

    /**
     * Use Case 17
     * 03.01.03, 03.02.01
     * Test for browsing an friend's inventory by text query.
     */
    public void testBrowseInventoryByTextQuery() {
        // SearchInventory contains owner name and owner item
        SearchInventory market = new SearchInventory();

        Item item1 = new Item(1, "Chapters", 0, 50.00, "chapters gc", true, 1, 0);
        Item item2 = new Item(2, "Bestbuy", 1, 150.00, "bestbuy gc", true, 1, 0);

        market.add(new SearchItem("john", item1));
        market.add(new SearchItem("ann", item2));

        SearchInventory searchByQuery = new SearchInventory();
        searchByQuery.setInventoryByQuery(market, "Bestbuy");

        assertEquals(searchByQuery.size(), 1);
    }

}
