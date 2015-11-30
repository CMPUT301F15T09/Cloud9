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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.photo.Photo;
import com.example.yunita.tradiogc.photo.PhotoController;

import java.io.File;
import java.util.Random;

/**
 * This activity handles adding an item to the user's inventory.
 */
public class AddItemActivity extends AppCompatActivity {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private InventoryController inventoryController;
    private Context mContext = this;

    private EditText nameEdit;
    private EditText priceEdit;
    private EditText quantityEdit;
    private EditText descriptionEdit;
    private RadioButton privateChoice;
    private Spinner categoriesChoice;
    private Spinner qualityChoice;
    private Uri imageFileUri;
    private ImageView tempPhoto;
    private String imageFilePath;
    private Bitmap thumbnail;
    private Button add;
    private Button delItem;
    private Button delPhoto;
    private Boolean usePhoto;

    private PhotoController photoController;
    private Photo photo = new Photo();

    /**
     * Gets the name of the new item.
     *
     * @return nameEdit name of the new item
     */
    public EditText getNameEdit() {
        return nameEdit;
    }

    /**
     * Gets the price of the new item.
     *
     * @return priceEdit price fo the new item
     */
    public EditText getPriceEdit() {
        return priceEdit;
    }

    /**
     * Gets the description of the new item.
     *
     * @return descriptionEdit description of the new item
     */
    public EditText getDescriptionEdit() {
        return descriptionEdit;
    }

    /**
     * Gets the "Add Item" button on the Add New Item page.
     *
     * @return add "Add Item" button
     */
    public Button getAdd() {
        return add;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_inventory);
        inventoryController = new InventoryController(mContext);

        usePhoto = false;
        privateChoice = (RadioButton) findViewById(R.id.private_radio_button);
        nameEdit = (EditText) findViewById(R.id.item_name_textEdit);
        priceEdit = (EditText) findViewById(R.id.price_edit_text);
        quantityEdit = (EditText) findViewById(R.id.quantity_edit_text);
        descriptionEdit = (EditText) findViewById(R.id.description_text_edit);
        categoriesChoice = (Spinner) findViewById(R.id.categories_spinner);
        qualityChoice = (Spinner) findViewById(R.id.quality_spinner);
        tempPhoto = (ImageView) findViewById(R.id.temp_photo_view);
        add = (Button) findViewById(R.id.add_item_button);
        delItem = (Button) findViewById(R.id.delete_item_button);
        delPhoto = (Button) findViewById(R.id.delete_photo_button);

        delItem.setVisibility(View.GONE);
        delPhoto.setVisibility(View.GONE);

        photoController = new PhotoController(mContext);
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

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Item cloneItem = (Item) bundle.getSerializable("clone");
                if (cloneItem != null) {
                    nameEdit.setText(cloneItem.getName());
                    nameEdit.setSelection(cloneItem.getName().length());
                    categoriesChoice.setSelection(cloneItem.getCategory());
                    priceEdit.setText(Double.toString(cloneItem.getPrice()));
                    descriptionEdit.setText(cloneItem.getDesc());
                    qualityChoice.setSelection(cloneItem.getQuality());
                    quantityEdit.setText(Integer.toString(cloneItem.getQuantity()));
                }
            }
        }
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
        Random random = new Random();
        int id = random.nextInt(999999999);

        if (TextUtils.isEmpty(name)) {
            nameEdit.setError("Name cannot be empty.");
        } else if (TextUtils.isEmpty(priceEdit.getText().toString())) {
            priceEdit.setError("Price cannot be empty.");
        } else {
            double price = Double.parseDouble(price_str);
            int category = categoriesChoice.getSelectedItemPosition();
            Boolean visibility = true;
            if (privateChoice.isChecked()) {
                visibility = false;
            }
            int quantity = 1;
            if (Integer.parseInt(quantityEdit.getText().toString()) > 0) {
                quantity = Integer.parseInt(quantityEdit.getText().toString());
            }
            int quality = qualityChoice.getSelectedItemPosition();

            if (thumbnail != null) {
                if (usePhoto) {
                    photo.setItemId(id);
                    photoController.addPhoto(id, photo);
                }
            }

            Item newItem = new Item(id, name, category, price, description, visibility, quantity, quality);
            inventoryController.addItem(newItem, LoginActivity.USERLOGIN);

            finish();
        }
    }

    /**
     * Triggers camera activity and saves the photo into the /tmp folder in the sdcard.
     * Code taken from: https://github.com/abramhindle/BogoPicGen
     * (C) 2015 Abram Hindle, modified by Cloud9
     *
     * @param view "Upload Photo" button
     */
    public void takeAPhoto(View view) {
        usePhoto = true;
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

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional data from it.
     *
     * @param requestCode   request code for the sender that will be associated
     *                      with the result data when it is returned
     * @param resultCode    the integer result code returned by the child activity
     * @param data          an intent that can return result data to the caller
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap temp = BitmapFactory.decodeFile(imageFileUri.getPath());
                double tHeight = temp.getHeight() * 0.2;
                double tWidth = temp.getWidth() * 0.2;

                thumbnail = Bitmap.createScaledBitmap(temp, (int) tWidth, (int) tHeight, true);
                tempPhoto.setImageBitmap(thumbnail);

                // Every time the user "accepts the photo", it is added to the photo list
                photo.addEncodedPhoto(thumbnail);
            } else {
                thumbnail = null;
            }
        }
    }

    /**
     * Called when the user presses the "Cancel Image" button.
     * <p>This method cancels uploading a recently taken photo to an item.
     *
     * @param view "Cancel Image" button.
     */
    public void cancelImage(View view) {
        usePhoto = false;
        if(imageFilePath != null){
            File file = new File(imageFilePath);
            if (file.exists()) {
                file.delete();
            }
            tempPhoto.setImageResource(R.mipmap.ic_launcher);
        }
    }

}
