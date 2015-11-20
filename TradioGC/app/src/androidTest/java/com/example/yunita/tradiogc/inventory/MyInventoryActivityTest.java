package com.example.yunita.tradiogc.inventory;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.yunita.tradiogc.login.LoginActivity;



public class MyInventoryActivityTest extends ActivityInstrumentationTestCase2 {
    private MyInventoryActivity myInventoryActivity;
    private AddItemActivity addItemActivity;
    private ItemActivity itemActivity;

    public MyInventoryActivityTest() {
        super(com.example.yunita.tradiogc.inventory.MyInventoryActivity.class);
    }

    /**
     * Use Case 6, 7, 8
     * test for (adding an item) viewing inventory, viewing item detail,
     * and removing an item
     */
    public void testMyInventory() {
        LoginActivity.USERLOGIN.setUsername("test");

        // add a new item
        // start a MyInventoryActivity
        myInventoryActivity = (MyInventoryActivity) getActivity();

        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(AddItemActivity.class.getName(), null, false);

        // click add new item button
        myInventoryActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button addItem = myInventoryActivity.getAddItem();
                addItem.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        // Copy from https://developer.android.com/training/activity-testing/activity-functional-testing.html
        // Validate that ReceiverActivity is started
        addItemActivity = (AddItemActivity) receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", addItemActivity);
        assertEquals("Monitor for ReceiverActivity has not been called", 1, receiverActivityMonitor.getHits());
        assertEquals("Activity is of wrong type", AddItemActivity.class, addItemActivity.getClass());

        // edit the info and click add
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

        // check the item info
        assertTrue(LoginActivity.USERLOGIN.getInventory().get(0) != null);
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getName(), "test");
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getPrice(), 10.0);
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getDesc(), "test");
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getCategory(), 0);
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getQuality(), 0);
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getQuantity(), 1);
        assertTrue(LoginActivity.USERLOGIN.getInventory().get(0).getVisibility());

        // test item detail
        // start a new MyInventoryActivity
        myInventoryActivity = (MyInventoryActivity) getActivity();

        // Set up an ActivityMonitor
        receiverActivityMonitor = getInstrumentation().addMonitor(ItemActivity.class.getName(), null, false);

        // click on the item to open the ItemActivity
        myInventoryActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myInventoryActivity.setInventory(LoginActivity.USERLOGIN.getInventory());
                myInventoryActivity.notifyUpdated();
                while(myInventoryActivity.getItemList().getChildCount() == 0);
                ListView itemList = myInventoryActivity.getItemList();
                View v = itemList.getChildAt(0);
                itemList.performItemClick(v, 0, v.getId());
            }
        });
        getInstrumentation().waitForIdleSync();

        // Copy from https://developer.android.com/training/activity-testing/activity-functional-testing.html
        // Validate that ReceiverActivity is started
        itemActivity = (ItemActivity) receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", itemActivity);
        assertEquals("Monitor for ReceiverActivity has not been called", 1, receiverActivityMonitor.getHits());
        assertEquals("Activity is of wrong type", ItemActivity.class, itemActivity.getClass());


        // test removing item
        // start a new MyInventoryActivity
        myInventoryActivity = (MyInventoryActivity) getActivity();

        // long click on the item to remove it
        myInventoryActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myInventoryActivity.setInventory(LoginActivity.USERLOGIN.getInventory());
                myInventoryActivity.notifyUpdated();
                while(myInventoryActivity.getItemList().getChildCount() == 0);
                ListView itemList = myInventoryActivity.getItemList();
                View v = itemList.getChildAt(0);
                v.performLongClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        // check if the inventory is empty
        assertTrue(LoginActivity.USERLOGIN.getInventory().isEmpty());

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);

    }
}