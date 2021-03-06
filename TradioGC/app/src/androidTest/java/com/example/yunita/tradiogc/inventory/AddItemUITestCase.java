package com.example.yunita.tradiogc.inventory;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

import com.example.yunita.tradiogc.login.LoginActivity;

public class AddItemUITestCase extends ActivityInstrumentationTestCase2 {
    private AddItemActivity addItemActivity;

    public AddItemUITestCase() {
        super(com.example.yunita.tradiogc.inventory.AddItemActivity.class);
    }

    /**
     * Use Case 3
     * 01.01.01
     * Test for adding an item to the user's inventory.
     */
    public void testAddItemUI() {
        LoginActivity.USERLOGIN.setUsername("test");

        // Start an AddItemActivity
        addItemActivity = (AddItemActivity) getActivity();

        // Edit the information and click add
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

        // Clear the test data
        InventoryController inventoryController = new InventoryController(addItemActivity.getApplicationContext());
        inventoryController.removeExistingItem(LoginActivity.USERLOGIN.getInventory().get(0));
    }
}