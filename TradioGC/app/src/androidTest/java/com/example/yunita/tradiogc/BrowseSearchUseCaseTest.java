package com.example.yunita.tradiogc;

import android.test.ActivityInstrumentationTestCase2;

import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.inventory.Item;
import com.example.yunita.tradiogc.market.SearchInventory;
import com.example.yunita.tradiogc.market.SearchItem;

public class BrowseSearchUseCaseTest extends ActivityInstrumentationTestCase2 {

    public BrowseSearchUseCaseTest() {
        super(com.example.yunita.tradiogc.MainActivity.class);
    }

    // 03.01.01, 03.02.01
    public void testSearchFriendInventory() {
        Inventory friendInventory = new Inventory();
        Item item1 = new Item("Chapters", 0, 50.00, "chapters gc", true, 1, 0, "image");
        Item item2 = new Item("Bestbuy", 1, 150.00, "bestbuy gc", true, 1, 0, "image");
        friendInventory.add(item1);

        assertTrue(friendInventory.contains(item1));
        assertFalse(friendInventory.contains(item2));
    }

    // 03.01.02, 03.02.01
    public void testBrowseInventoryByCategory() {
        // SearchInventory contains owner name and owner item
        SearchInventory market = new SearchInventory();

        Item item1 = new Item("Chapters", 0, 50.00, "chapters gc", true, 1, 0, "image");
        Item item2 = new Item("Bestbuy", 1, 150.00, "bestbuy gc", true, 1, 0, "image");

        market.add(new SearchItem("john", item1));
        market.add(new SearchItem("ann", item2));

        SearchInventory searchByCategory = new SearchInventory();
        searchByCategory.setInventoryByCategory(market, 0);

        assertEquals(searchByCategory.size(), 1);
    }

    // 03.01.03, 03.02.01
    public void testBrowseInventoryByTextQuery() {
        // SearchInventory contains owner name and owner item
        SearchInventory market = new SearchInventory();

        Item item1 = new Item("Chapters", 0, 50.00, "chapters gc", true, 1, 0, "image");
        Item item2 = new Item("Bestbuy", 1, 150.00, "bestbuy gc", true, 1, 0, "image");

        market.add(new SearchItem("john", item1));
        market.add(new SearchItem("ann", item2));

        SearchInventory searchByQuery = new SearchInventory();
        searchByQuery.setInventoryByQuery(market, "Bestbuy");

        assertEquals(searchByQuery.size(), 1);
    }

}
