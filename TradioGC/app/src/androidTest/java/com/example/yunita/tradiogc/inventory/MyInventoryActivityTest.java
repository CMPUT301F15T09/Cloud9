package com.example.yunita.tradiogc.inventory;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.yunita.tradiogc.login.LoginActivity;



public class MyInventoryActivityTest extends ActivityInstrumentationTestCase2 {
    private MyInventoryActivity myInventoryActivity;
    private AddItemActivity addItemActivity;

    public MyInventoryActivityTest() {
        super(com.example.yunita.tradiogc.inventory.MyInventoryActivity.class);
    }

    /**
     * test for adding an item
     */
    public void testAddItem() {
        LoginActivity.USERLOGIN.setUsername("test");

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
                //RadioButton privateChoice = addItemActivity.getPrivateChoice();
                //Spinner categoriesChoice = addItemActivity.getCategoriesChoice();
                //Spinner qualityChoice = addItemActivity.getQualityChoice();
                Button add = addItemActivity.getAdd();
                add.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        // delete the item
        myInventoryActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EditText query = myInventoryActivity.getQuery_et();
                //while (myInventoryActivity.getItemList().getChildCount() == 0);
                ListView itemList = myInventoryActivity.getItemList();
                View v = itemList.getChildAt(0);
                v.performLongClick();
            }
        });
        getInstrumentation().waitForIdleSync();






        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);

    }
}