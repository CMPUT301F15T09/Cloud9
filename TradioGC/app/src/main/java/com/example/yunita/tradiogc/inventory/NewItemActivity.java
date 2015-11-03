package com.example.yunita.tradiogc.inventory;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.login.LoginActivity;

public class NewItemActivity extends ActionBarActivity {
    private InventoryController inventoryController;
    private Inventory inventory;
    private Context context = this;
    private EditText nameEdit;
    private EditText priceEdit;
    private EditText descriptionEdit;
    private RadioButton privateChoice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_inventory);
        inventoryController = new InventoryController(context);
        //need to pass in an inventory
    }

    @Override
    protected void onStart(){
        super.onStart();

        Spinner categoriesChoice = (Spinner) findViewById(R.id.categories_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.categories_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesChoice.setAdapter(adapter);

        Thread thread = inventoryController.new LoadInventoryThread(LoginActivity.USERLOGIN, inventory);
        // Update thread
        thread.start();
        onClickListeners(categoriesChoice);
    }



    public void onClickListeners(Spinner categoriesChoice) {
        Button addItem = (Button) findViewById(R.id.add_item_button);
        final Spinner categ_Choice = categoriesChoice;

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                privateChoice = (RadioButton) findViewById(R.id.private_radio_button);

                nameEdit = (EditText) findViewById(R.id.name_textEdit);
                priceEdit = (EditText) findViewById(R.id.price_edit_text);
                descriptionEdit = (EditText) findViewById(R.id.description_text_edit);
                String name = nameEdit.getText().toString();
                //Wont work right now
                int category = categ_Choice.getSelectedItemPosition();
                Boolean visibility = true;
                if (privateChoice.isChecked()) {
                    visibility = false;
                }



                double price = Double.parseDouble(priceEdit.getText().toString());
                String description = descriptionEdit.getText().toString();

                inventoryController.createItem(inventory, name, category, price, description, visibility);


                Thread thread = inventoryController.new UpdateInventoryThread(LoginActivity.USERLOGIN, inventory);
                thread.start();
            }
        });

    }
}
