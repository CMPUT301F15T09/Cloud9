package com.example.yunita.tradiogc.inventory;

import android.test.ActivityInstrumentationTestCase2;

public class ItemActivityTest extends ActivityInstrumentationTestCase2 {
    private ItemActivity itemActivity;

    public ItemActivityTest() {
        super(com.example.yunita.tradiogc.inventory.ItemActivity.class);
    }

    public void testItemDetail () {
        Item item = new Item("testitem", 0, 5.0, "test", true, 1, 0);
        // start an ItemActivity
        itemActivity = (ItemActivity) getActivity();

        // set an item
        itemActivity.setItem(item);

        // update the UI
        itemActivity.runOnUiThread(itemActivity.getDoUpdateGUIDetails());
        getInstrumentation().waitForIdleSync();

        // check the info
        assertEquals(itemActivity.getName().getText().toString(), "testitem");
        assertEquals(itemActivity.getCategory().getText().toString(), "Books");
        assertEquals(itemActivity.getPrice().getText().toString(), "$5.0");
        assertEquals(itemActivity.getDescription().getText().toString(), "test");
        assertEquals(itemActivity.getQuantity().getText().toString(), "1");
        assertEquals(itemActivity.getQuality().getText().toString(), "New");
    }
}