package com.example.yunita.tradiogc.inventory;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.yunita.tradiogc.login.LoginActivity;


public class MyInventoryUITestCase extends ActivityInstrumentationTestCase2 {
    private MyInventoryActivity myInventoryActivity;
    private AddItemActivity addItemActivity;
    private ItemActivity itemActivity;

    public MyInventoryUITestCase() {
        super(com.example.yunita.tradiogc.inventory.MyInventoryActivity.class);
    }

    /**
     * Use Cases 5, 6, and 7
     * 01.01.01, 01.02.01, 01.03.01, 01.05.01
     * Test for viewing a user's inventory, viewing an item's detail page,
     * and removing an item from a user's inventory.
     */
    public void testMyInventoryUI() {
        LoginActivity.USERLOGIN.setUsername("test");

        // Add a new item
        // Start a MyInventoryActivity
        myInventoryActivity = (MyInventoryActivity) getActivity();

        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(AddItemActivity.class.getName(), null, false);

        // Click the "add new item" button
        myInventoryActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button addItem = myInventoryActivity.getAddItem();
                addItem.performClick();
            }
        });

        getInstrumentation().waitForIdleSync();

        // Copy from https://developer.android.com/training/activity-testing/activity-functional-testing.html
        // Validate that ReceiverActivity has started
        addItemActivity = (AddItemActivity) receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", addItemActivity);
        assertEquals("Monitor for ReceiverActivity has not been called", 1, receiverActivityMonitor.getHits());
        assertEquals("Activity is of wrong type", AddItemActivity.class, addItemActivity.getClass());

        // Edit the information and click the "add item" button
        addItemActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EditText nameEdit = addItemActivity.getNameEdit();
                nameEdit.setText("test");
                EditText priceEdit = addItemActivity.getPriceEdit();
                priceEdit.setText("10");
                EditText descriptionEdit = addItemActivity.getDescriptionEdit();
                descriptionEdit.setText("test");

                Button add = addItemActivity.getAdd();
                add.performClick();
            }
        });

        getInstrumentation().waitForIdleSync();

        // Check the item information
        assertTrue(LoginActivity.USERLOGIN.getInventory().get(0) != null);
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getName(), "test");
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getPrice(), 10.0);
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getDesc(), "test");
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getCategory(), 0);
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getQuality(), 0);
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getQuantity(), 1);
        assertTrue(LoginActivity.USERLOGIN.getInventory().get(0).getVisibility());

        // View the item detail page
        // Start a new MyInventoryActivity
        myInventoryActivity = (MyInventoryActivity) getActivity();

        // Set up an ActivityMonitor
        receiverActivityMonitor = getInstrumentation().addMonitor(ItemActivity.class.getName(), null, false);

        // Click on the item to open the ItemActivity
        myInventoryActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myInventoryActivity.setInventory(LoginActivity.USERLOGIN.getInventory());
                myInventoryActivity.notifyUpdated();
                while (myInventoryActivity.getItemList().getChildCount() == 0) ;
                ListView itemList = myInventoryActivity.getItemList();
                View v = itemList.getChildAt(0);
                itemList.performItemClick(v, 0, v.getId());
            }
        });
        getInstrumentation().waitForIdleSync();

        // Copy from https://developer.android.com/training/activity-testing/activity-functional-testing.html
        // Validate that ReceiverActivity has started
        itemActivity = (ItemActivity) receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", itemActivity);
        assertEquals("Monitor for ReceiverActivity has not been called", 1, receiverActivityMonitor.getHits());
        assertEquals("Activity is of wrong type", ItemActivity.class, itemActivity.getClass());


        // Remove the item
        // Start a new MyInventoryActivity
        myInventoryActivity = (MyInventoryActivity) getActivity();

        // Long click on the item to remove it
        myInventoryActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myInventoryActivity.setInventory(LoginActivity.USERLOGIN.getInventory());
                myInventoryActivity.notifyUpdated();
                while (myInventoryActivity.getItemList().getChildCount() == 0) ;
                ListView itemList = myInventoryActivity.getItemList();
                View v = itemList.getChildAt(0);
                v.performLongClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        // Check if the inventory is empty
        assertTrue(LoginActivity.USERLOGIN.getInventory().isEmpty());

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);

    }
}