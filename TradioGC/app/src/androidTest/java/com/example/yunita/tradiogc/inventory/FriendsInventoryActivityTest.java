package com.example.yunita.tradiogc.inventory;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ListView;

public class FriendsInventoryActivityTest extends ActivityInstrumentationTestCase2 {
    private FriendsInventoryActivity friendsInventoryActivity;

    public FriendsInventoryActivityTest() {
        super(com.example.yunita.tradiogc.inventory.FriendsInventoryActivity.class);
    }

    /**
     * test for friend item list;
     */
    public void testFriendInventory() {
        // start a FriendsInventoryActivity
        friendsInventoryActivity = (FriendsInventoryActivity) getActivity();


        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(ItemActivity.class.getName(), null, false);

        // click on the first item
        friendsInventoryActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                while (friendsInventoryActivity.getItem_list().getChildCount() == 0) ;
                ListView itemList = friendsInventoryActivity.getItem_list();
                View v = itemList.getChildAt(0);
                itemList.performItemClick(v, 0, v.getId());
            }
        });
        getInstrumentation().waitForIdleSync();

        // Copy from https://developer.android.com/training/activity-testing/activity-functional-testing.html
        // Validate that ReceiverActivity is started
        ItemActivity itemActivity = (ItemActivity) receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", itemActivity);
        assertEquals("Monitor for ReceiverActivity has not been called", 1, receiverActivityMonitor.getHits());
        assertEquals("Activity is of wrong type", ItemActivity.class, itemActivity.getClass());

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);
    }

    /**
     * Use Case 17, 18
     * test for browsing items by query/category
     */
    public void testSearchItem() {
        // start a FriendsInventoryActivity
        friendsInventoryActivity = (FriendsInventoryActivity) getActivity();

        // click on the first item
        friendsInventoryActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                friendsInventoryActivity.searchItem(1, "item");
            }
        });
        getInstrumentation().waitForIdleSync();

        assertTrue(friendsInventoryActivity.getItem_list().getChildCount() == 1);
        Item item = friendsInventoryActivity.getInventory().get(0);
        assertTrue(item.getName().contains("item"));
        assertEquals(item.getCategory(), 1);
    }
}