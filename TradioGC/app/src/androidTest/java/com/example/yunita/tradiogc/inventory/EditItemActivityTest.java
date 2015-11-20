package com.example.yunita.tradiogc.inventory;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.yunita.tradiogc.login.LoginActivity;


public class EditItemActivityTest extends ActivityInstrumentationTestCase2 {
    private EditItemActivity editItemActivity;

    public EditItemActivityTest() {
        super(com.example.yunita.tradiogc.inventory.EditItemActivity.class);
    }

    /**
     * Use Case 5, 9, 10
     * test for editting an item
     */
    public void testEditItem() {
        LoginActivity.USERLOGIN.setUsername("test");

        // start an EditItemActivity
        editItemActivity = (EditItemActivity) getActivity();

        // edit the info and click save button
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

        // check the item info
        assertTrue(LoginActivity.USERLOGIN.getInventory().get(0) != null);
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getName(), "edittest");
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getPrice(), 5.0);
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getDesc(), "edittest");
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getCategory(), 1);
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getQuality(), 1);
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getQuantity(), 5);
        assertFalse(LoginActivity.USERLOGIN.getInventory().get(0).getVisibility());

        // delete the item
        InventoryController inventoryController = new InventoryController(editItemActivity.getApplicationContext());
        inventoryController.removeExistingItem(LoginActivity.USERLOGIN.getInventory().get(0));
    }

}