package com.example.yunita.tradiogc.inventory;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

import com.example.yunita.tradiogc.login.LoginActivity;

public class AddItemActivityTest extends ActivityInstrumentationTestCase2 {
    private AddItemActivity addItemActivity;

    public AddItemActivityTest() {
        super(com.example.yunita.tradiogc.inventory.AddItemActivity.class);
    }

    /**
     * test for adding an item
     */
    public void testAddItem() {
        LoginActivity.USERLOGIN.setUsername("test");

        // start an AddItemActivity
        addItemActivity = (AddItemActivity) getActivity();

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

        assertTrue(LoginActivity.USERLOGIN.getInventory().get(0) != null);
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getName(), "test");
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getPrice(), 10.0);
        assertEquals(LoginActivity.USERLOGIN.getInventory().get(0).getDesc(), "test");
    }
}