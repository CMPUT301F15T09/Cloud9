package com.example.yunita.tradiogc.inventory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.yunita.tradiogc.R;

import java.io.File;

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

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri imageFileUri;
    private ImageView tempPhoto;
    private String imageFilePath;

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
        tempPhoto = (ImageView) findViewById(R.id.temp_photo_view);
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
            return;
        } else if (TextUtils.isEmpty(priceEdit.getText().toString())) {
            priceEdit.setError("Price cannot be empty.");
            return;
        } else if (TextUtils.isEmpty(description)) {
            descriptionEdit.setError("Description cannot be empty.");
            return;
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

    /**
     *
     * @param view
     */
    public void takeAPhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String folder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp";
        File folderF = new File(folder);
        if (!folderF.exists()) {
            folderF.mkdir();
        }

        imageFilePath = folder + "/" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        File imageFile = new File(imageFilePath);
        imageFileUri = Uri.fromFile(imageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap thumbnail = BitmapFactory.decodeFile(imageFileUri.getPath());
                double tHeight = thumbnail.getHeight() * 0.3;
                double tWidth = thumbnail.getWidth()* 0.3;

                Bitmap b = Bitmap.createScaledBitmap(thumbnail, (int)tWidth, (int)tHeight, true);
                tempPhoto.setImageBitmap(b);
            }
        }
    }

    public void cancelImage(View view){
        File file = new File(imageFilePath);
        if(file.exists()) {
            file.delete();
        }
        tempPhoto.setImageResource(R.mipmap.ic_launcher);
    }

}
