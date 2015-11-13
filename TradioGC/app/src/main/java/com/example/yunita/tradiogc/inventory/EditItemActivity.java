package com.example.yunita.tradiogc.inventory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.login.LoginActivity;


public class EditItemActivity extends AppCompatActivity {
    private InventoryController inventoryController;
    private Context mContext = this;
    private EditText nameEdit;
    private EditText priceEdit;
    private EditText descriptionEdit;
    private RadioGroup radioVisibility;
    private RadioButton privateChoice;
    private Spinner categoriesChoice;
    private EditText quantityEdit;
    private Spinner qualityChoice;
    private Button uploadPhotoButton;
    private Item item;
    private Button add;
    private Button save;
    private Inventory inventory = LoginActivity.USERLOGIN.getInventory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_inventory);
        inventoryController = new InventoryController(mContext);

        // get the index of the item in the inventory.
        // index is the identifier of the item in inventory.
        Intent intent = getIntent();
        int index = intent.getExtras().getInt("index");
        item = inventory.get(index);

        radioVisibility = (RadioGroup) findViewById(R.id.radioVisibility);
        privateChoice = (RadioButton) findViewById(R.id.private_radio_button);
        nameEdit = (EditText) findViewById(R.id.item_name_textEdit);
        priceEdit = (EditText) findViewById(R.id.price_edit_text);
        quantityEdit = (EditText) findViewById(R.id.quantity_edit_text);
        qualityChoice = (Spinner) findViewById(R.id.quality_spinner);
        descriptionEdit = (EditText) findViewById(R.id.description_text_edit);
        categoriesChoice = (Spinner) findViewById(R.id.categories_spinner);
        uploadPhotoButton = (Button) findViewById(R.id.upload_photo_button);
        add = (Button) findViewById(R.id.add_item_button);
        save = (Button) findViewById(R.id.save_item_button);
    }

    /**
     * Sets the view with the current item information.
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

        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(EditItemActivity.this, UploadPhotoActivity.class));
            }
        });

        add.setVisibility(View.GONE);
        save.setVisibility(View.VISIBLE);


        if (!item.getVisibility()) {
            radioVisibility.check(R.id.private_radio_button);
        }
        nameEdit.setText(item.getName());
        categoriesChoice.setSelection(item.getCategory());
        priceEdit.setText(Double.toString(item.getPrice()));
        descriptionEdit.setText(item.getDesc());
        qualityChoice.setSelection(item.getQuality());
        quantityEdit.setText(Integer.toString(item.getQuantity()));
    }

    /**
     * Called when the user clicks the "Save" button on the Edit Item page.
     * This method is used to run the update item thread and closes
     * this activity after the thread updates the item information
     * into the webserver.
     *
     * @param view: "Save" button in the Edit Item page
     */
    public void saveItem(View view) {
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

            item.setName(name);
            item.setDesc(description);
            item.setVisibility(visibility);
            item.setPrice(price);
            item.setCategory(category);
            item.setQuantity(quantity);
            item.setQuality(quality);

            inventoryController.updateItem(item);
            finish();
        }
    }
}
