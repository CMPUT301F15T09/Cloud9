package com.example.yunita.tradiogc.inventory;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.yunita.tradiogc.R;

public class AddItemActivity extends AppCompatActivity {
    private InventoryController inventoryController;
    private Context mContext = this;
    private EditText nameEdit;
    private EditText priceEdit;
    private EditText quantityEdit;
    private EditText descriptionEdit;
    private RadioButton privateChoice;
    private Spinner categoriesChoice;
    private Spinner qualityChoice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_inventory);
        inventoryController = new InventoryController(mContext);

        privateChoice = (RadioButton) findViewById(R.id.private_radio_button);
        nameEdit = (EditText) findViewById(R.id.item_name_textEdit);
        priceEdit = (EditText) findViewById(R.id.price_edit_text);
        quantityEdit = (EditText) findViewById(R.id.quantity_edit_text);
        descriptionEdit = (EditText) findViewById(R.id.description_text_edit);
        categoriesChoice = (Spinner) findViewById(R.id.categories_spinner);
        qualityChoice = (Spinner) findViewById(R.id.quality_spinner);
    }

    /**
     * Manipulates the Category and Quality drop down menu.
     */
    @Override
    protected void onStart() {
        super.onStart();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesChoice.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.quality_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qualityChoice.setAdapter(adapter2);
    }

    /**
     * Called when the user clicks the "Add Item" button.
     * <p>This method is used to check the user's input.
     * If all fields are filled, it creates a new item and adds
     * it into the user's inventory.
     *
     * @param view "Add Item" button in the Inventory page
     */
    public void addNewItem(View view) {
        String name = nameEdit.getText().toString();
        String price_str = priceEdit.getText().toString();
        String description = descriptionEdit.getText().toString();

        if (TextUtils.isEmpty(name)) {
            nameEdit.setError("Name cannot be empty.");
        } else if (TextUtils.isEmpty(priceEdit.getText().toString())) {
            priceEdit.setError("Price cannot be empty.");
        } else if (TextUtils.isEmpty(description)) {
            descriptionEdit.setError("Description cannot be empty.");
        } else {
            double price = Double.parseDouble(price_str);
            int category = categoriesChoice.getSelectedItemPosition();
            Boolean visibility = true;
            if (privateChoice.isChecked()) {
                visibility = false;
            }
            int quantity = Integer.parseInt(quantityEdit.getText().toString());
            int quality = qualityChoice.getSelectedItemPosition();

            Item newItem = new Item(name, category, price, description, visibility, quantity, quality);
            inventoryController.addItem(newItem);

            finish();
        }
    }

}
