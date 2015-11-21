package com.example.yunita.tradiogc.inventory;

import android.test.ActivityInstrumentationTestCase2;

public class ItemUITestCase extends ActivityInstrumentationTestCase2 {
    private ItemActivity itemActivity;

    public ItemUITestCase() {
        super(com.example.yunita.tradiogc.inventory.ItemActivity.class);
    }

    /**
     * Test for setting an item into an inventory list view.
     */
    public void testItemActivityUI() {
        Item item = new Item("testitem", 0, 5.0, "test", true, 1, 0, null);
        // Start an ItemActivity
        itemActivity = (ItemActivity) getActivity();

        // Set an item
        itemActivity.setItem(item);

        // Update the UI
        itemActivity.runOnUiThread(itemActivity.getDoUpdateGUIDetails());
        getInstrumentation().waitForIdleSync();

        // Check the information
        assertEquals(itemActivity.getName().getText().toString(), "testitem");
        assertEquals(itemActivity.getCategory().getText().toString(), "Books");
        assertEquals(itemActivity.getPrice().getText().toString(), "$5.0");
        assertEquals(itemActivity.getDescription().getText().toString(), "test");
        assertEquals(itemActivity.getQuantity().getText().toString(), "1");
        assertEquals(itemActivity.getQuality().getText().toString(), "New");
    }
}