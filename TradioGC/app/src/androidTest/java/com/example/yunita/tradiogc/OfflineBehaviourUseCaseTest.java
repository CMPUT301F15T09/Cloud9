package com.example.yunita.tradiogc;

import android.test.ActivityInstrumentationTestCase2;

import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.inventory.Item;
import com.example.yunita.tradiogc.trade.Trade;
import com.example.yunita.tradiogc.user.User;

public class OfflineBehaviourUseCaseTest extends ActivityInstrumentationTestCase2 {

    public OfflineBehaviourUseCaseTest() {
        super(com.example.yunita.tradiogc.MainActivity.class);
    }

    /**
     * Use Case 35
     * 09.01.01
     * Test for making a new inventory item while offline.
     */
    public void testMakeInventoryItemsOffline(){
        Inventory inventory = new Inventory();
        Item item = new Item(1,"Chapters", 0, 50.00, "chapters gc", true, 1, 0);
        inventory.add(item);
        assertEquals(inventory.size(), 1);
    }

    /**
     * Use Case 36
     * 09.02.01
     * Test for making a new trade while offline.
     */
    public void testMakeTradeOffline(){
        User john = new User();
        User ann = new User();

        Trade offeredTrade = new Trade();
        // ann is owner, so ann will have "offered" trade
        offeredTrade.setStatus("offered");
        ann.getTrades().add(offeredTrade);

        // john as a borrower will have "pending" trade
        Trade pendingTrade = new Trade(offeredTrade);
        pendingTrade.setStatus("pending");
        john.getTrades().add(pendingTrade);

        // john should have 1 pending trade while ann should have 1 offered trade
        assertEquals(john.getTrades().getPendingTrades().size(), 1);
        assertEquals(ann.getTrades().getOfferedTrades().size(), 1);
    }

    /**
     * Use Case 37
     * 09.03.01
     * Test for browsing a user's friend's inventory while offline.
     */
    public void testBrowseFriendInventoryOffline(){
        User john = new User();
        Item item = new Item(1,"Chapters", 0, 50.00, "chapters gc", true, 1, 0);
        john.getInventory().add(item);
        assertEquals(john.getInventory().size(), 1);

        assertNotNull(john.getInventory());
        assertTrue(john.getInventory().contains(item));
    }

}
