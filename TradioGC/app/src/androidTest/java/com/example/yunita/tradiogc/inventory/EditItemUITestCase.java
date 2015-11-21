package com.example.yunita.tradiogc.inventory;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.yunita.tradiogc.login.LoginActivity;


public class EditItemUITestCase extends ActivityInstrumentationTestCase2 {
    private EditItemActivity editItemActivity;

    public EditItemUITestCase() {
        super(com.example.yunita.tradiogc.inventory.EditItemActivity.class);
    }

    /**
     * Use Cases 4, 8, 9
     * 01.01.01, 01.03.01, 01.04.01, 01.06.01
     * Test for editing an item, setting its category, and setting its visibility.
     */
    public void testEditItemUI() {
        LoginActivity.USERLOGIN.setUsername("test");

        // Start an EditItemActivity
        editItemActivity = (EditItemActivity) getActivity();

        // Edit the information and click the save button
        editItemActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EditText nameEdit = editItemActivity.getNameEdit();
                nameEdit.setText("edittest");
                EditText priceEdit = editItemActivity.getPriceEdit();
                priceEdit.setText("5");
                EditText descriptionEdit = editItemActivity.getDescriptionEdit();
                descriptionEdit.setText("edittest");
                EditText quantity = editItemActivity.getQuantityEdit();
                quantity.setText("5");
                Spinner categoriesChoice = editItemActivity.getCategoriesChoice();
                categoriesChoice.setSelection(1);
                Spinner qualityChoice = editItemActivity.getQualityChoice();
                qualityChoice.setSelection(1);
                RadioButton privateChoice = editItemActivity.getPrivateChoice();
                privateChoice.setChecked(true);
                Button save = editItemActivity.getSave();
                save.performClick();
            }
        });

        getInstrumentation().waitForIdleSync();

        // Check the item information
        assertTrue(LoginActivity.USERLOGIN.getInventory().get(0) != null);
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getName(), "edittest");
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getPrice(), 5.0);
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getDesc(), "edittest");
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getCategory(), 1);
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getQuality(), 1);
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getQuantity(), 5);
        assertFalse(LoginActivity.USERLOGIN.getInventory().get(0).getVisibility());

        // Delete the item
        InventoryController inventoryController = new InventoryController(editItemActivity.getApplicationContext());
        inventoryController.removeExistingItem(LoginActivity.USERLOGIN.getInventory().get(0));
    }

}