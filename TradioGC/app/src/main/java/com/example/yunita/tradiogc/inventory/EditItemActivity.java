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
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.photo.Photo;
import com.example.yunita.tradiogc.photo.PhotoController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

/**
 * This activity handles editing an item in the user's inventory.
 */
public class EditItemActivity extends AppCompatActivity {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
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
    private Item item = new Item();
    private Button add;
    private Button save;
    private Button delPhoto;
    private Uri imageFileUri;
    private ImageView tempPhoto;
    private String imageFilePath;
    private Bitmap thumbnail;
    private Boolean usePhoto;
    private PhotoController photoController;
    private Photo photo;

    /**
     * Gets the name of the item to be edited.
     *
     * @return nameEdit name of the item
     */
    public EditText getNameEdit() {
        return nameEdit;
    }

    /**
     * Gets the price of the item to be edited.
     *
     * @return priceEdit price of the item
     */
    public EditText getPriceEdit() {
        return priceEdit;
    }

    /**
     * Gets the description of the item to be edited.
     *
     * @return descriptionEdit description of the item
     */
    public EditText getDescriptionEdit() {
        return descriptionEdit;
    }

    /**
     * Gets the visibility of the item to be edited.
     *
     * @return privateChoice button to determine the visibility of the item
     */
    public RadioButton getPrivateChoice() {
        return privateChoice;
    }

    /**
     * Gets the category of the item to be edited.
     *
     * @return categoriesChoice category chosen to place the item in
     */
    public Spinner getCategoriesChoice() {
        return categoriesChoice;
    }

    /**
     * Gets the quantity of the item to be edited.
     *
     * @return quantityEdit quantity of the item
     */
    public EditText getQuantityEdit() {
        return quantityEdit;
    }

    /**
     * Gets the quality of the item to be edited.
     *
     * @return qualityEdit quality of the item
     */
    public Spinner getQualityChoice() {
        return qualityChoice;
    }

    /**
     * Gets the "Save" button on the Edit Item page.
     *
     * @return save "Save" button
     */
    public Button getSave() {
        return save;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_inventory);
        inventoryController = new InventoryController(mContext);
        photoController = new PhotoController(mContext);

        if (getIntent().getExtras()!=null) {
            item = LoginActivity.USERLOGIN.getInventory().get(getIntent().getExtras().getInt("index"));
        } else {
            item = new Item();
        }

        usePhoto = false;
        radioVisibility = (RadioGroup) findViewById(R.id.radioVisibility);
        privateChoice = (RadioButton) findViewById(R.id.private_radio_button);
        nameEdit = (EditText) findViewById(R.id.item_name_textEdit);
        priceEdit = (EditText) findViewById(R.id.price_edit_text);
        quantityEdit = (EditText) findViewById(R.id.quantity_edit_text);
        qualityChoice = (Spinner) findViewById(R.id.quality_spinner);
        descriptionEdit = (EditText) findViewById(R.id.description_text_edit);
        categoriesChoice = (Spinner) findViewById(R.id.categories_spinner);
        add = (Button) findViewById(R.id.add_item_button);
        save = (Button) findViewById(R.id.save_item_button);

        tempPhoto = (ImageView) findViewById(R.id.temp_photo_view);

        photoController.getItem(item.getId());
        photo = photoController.getPhoto();
        ArrayList<String> photoArray;

        if (photo != null) {
            photoArray = photo.getEncodedPhoto();
            tempPhoto.setImageBitmap(decodeImage(photoArray.get(0)));
        }
        else{
            photo = new Photo();
        }

        Button delPhoto = (Button) findViewById(R.id.delete_photo_button);
        Button delItem = (Button) findViewById(R.id.delete_item_button);

        delPhoto.setVisibility(View.VISIBLE);
        delItem.setVisibility(View.VISIBLE);
        add.setVisibility(View.GONE);
        save.setVisibility(View.VISIBLE);

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
        photo = new Photo();


        if (!item.getVisibility()) {
            radioVisibility.check(R.id.private_radio_button);
        }
        nameEdit.setText(item.getName());
        nameEdit.setSelection(item.getName().length());
        categoriesChoice.setSelection(item.getCategory());
        priceEdit.setText(Double.toString(item.getPrice()));
        descriptionEdit.setText(item.getDesc());
        qualityChoice.setSelection(item.getQuality());
        quantityEdit.setText(Integer.toString(item.getQuantity()));


        tempPhoto = (ImageView) findViewById(R.id.temp_photo_view);

        photoController.getItem(item.getId());
        photo = photoController.getPhoto();
        ArrayList<String> photoArray;

        if (photo != null) {
            photoArray = photo.getEncodedPhoto();
            tempPhoto.setImageBitmap(decodeImage(photoArray.get(0)));
        }
        else{
            photo = new Photo();
        }
    }


    /**
     * Called when the user presses the "Save" button on the Edit Item page.
     * This method is used to run the update item thread and closes
     * this activity after the thread updates the item information
     * into the webserver.
     *
     * @param view "Save" button in the Edit Item page
     */
    public void saveItem(View view) {
        String name = nameEdit.getText().toString();
        String price_str = priceEdit.getText().toString();
        String description = descriptionEdit.getText().toString();

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
            if (Integer.parseInt(quantityEdit.getText().toString()) > 0){
                quantity = Integer.parseInt(quantityEdit.getText().toString());
            }
            int quality = qualityChoice.getSelectedItemPosition();


            if (thumbnail != null) {
                if (usePhoto){
                    photo.addEncodedPhoto(thumbnail);
                    photo.setItemId(item.getId());
                    photoController.addPhoto(item.getId(), photo);
                }
            }

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



    /**
     * Triggers camera activity and saves the photo into /tmp folder in sdcard.
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
            } else {
                thumbnail = null;
            }
        }
    }


    /**
     * Cancels uploading the photo.
     *
     * @param view "Cancel Image" button
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

    // taken from http://stackoverflow.com/questions/9768611/encode-and-decode-bitmap-object-in-base64-string-in-android
    // (C) 2015 Roman Truba modified by Cloud9

    /**
     * Encodes the photo into a string and returns it.
     *
     * @param photo         photo taken from camera
     * @return encodedImage string of the encoded image
     */
    public String encodeImage(Bitmap photo) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public Bitmap decodeImage(String encoded){
        byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }


    public void delItem(View v){
        inventoryController.removeExistingItem(item);
        Intent intent = new Intent(mContext, MyInventoryActivity.class);
        startActivity(intent);
        finish();
    }
    public void delPhoto(View v){
        photo.removeEncodedPhoto();
        photoController.updateItemPhotos(photo.getItemId(),photo);
        Intent intent = new Intent(mContext, MyInventoryActivity.class);
        startActivity(intent);
        finish();
    }

}
