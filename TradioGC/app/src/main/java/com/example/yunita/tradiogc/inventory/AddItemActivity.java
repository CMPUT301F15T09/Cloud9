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
import android.widget.Spinner;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.login.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;

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
    private Boolean usePhoto;

    public EditText getNameEdit() {
        return nameEdit;
    }

    public EditText getPriceEdit() {
        return priceEdit;
    }

    public EditText getDescriptionEdit() {
        return descriptionEdit;
    }

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

        delItem.setVisibility(View.GONE);
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
            String photo = "";
            if (thumbnail != null) {
                photo = encodeImage(thumbnail);
                System.out.println(thumbnail.getByteCount());
            }

            if (usePhoto) {
                //save photo
            }
                Item newItem = new Item(name, category, price, description, visibility, quantity, quality);
                inventoryController.addItem(newItem);
            }

            finish();

    }

    // taken from https://github.com/abramhindle/BogoPicGen
    // (C) 2015 Abram Hindle modified by Cloud9

    /**
     * Triggers camera activity and saves the photo into /tmp folder in sdcard.
     *
     * @param view "Upload Photo" button.
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
     * @param requestCode request code for the sender that will be associated
     *                    with the result data when it is returned
     * @param resultCode the integer result code returned by the child activity
     * @param data an intent, which can return result data to the caller
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

    // taken from http://stackoverflow.com/questions/9768611/encode-and-decode-bitmap-object-in-base64-string-in-android
    // (C) 2015 Roman Truba modified by Cloud9

    /**
     * Encodes the photo into a string and returns it.
     *
     * @param photo photo taken from camera.
     * @return String.
     */
    public String encodeImage(Bitmap photo) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
