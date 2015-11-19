package com.example.yunita.tradiogc.inventory;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.yunita.tradiogc.login.LoginActivity;


public class EditItemActivityTest extends ActivityInstrumentationTestCase2 {
    private EditItemActivity editItemActivity;

    public EditItemActivityTest() {
        super(com.example.yunita.tradiogc.inventory.EditItemActivity.class);
    }

    /**
     * test for editting an item
     */
    public void testAddItem() {
        LoginActivity.USERLOGIN.setUsername("test");

        // start an EditItemActivity
        editItemActivity = (EditItemActivity) getActivity();

        // edit the info and click add
        editItemActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EditText nameEdit = editItemActivity.getNameEdit();
                nameEdit.setText("test");
                EditText priceEdit = editItemActivity.getPriceEdit();
                priceEdit.setText("10");
                EditText descriptionEdit = editItemActivity.getDescriptionEdit();
                descriptionEdit.setText("test");
                //RadioButton privateChoice = editItemActivity.getPrivateChoice();
                //Spinner categoriesChoice = addItemActivity.getCategoriesChoice();
                //Spinner qualityChoice = addItemActivity.getQualityChoice();
                Button save = EditItemActivity.getSave();
                View v 
                save.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        assertTrue(LoginActivity.USERLOGIN.getInventory().get(0) != null);
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getName(), "test");
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getPrice(), 10.0);
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getDesc(), "test");
    }

}