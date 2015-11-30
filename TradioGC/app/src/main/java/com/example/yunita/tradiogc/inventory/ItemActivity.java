package com.example.yunita.tradiogc.inventory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.photo.Photo;
import com.example.yunita.tradiogc.photo.PhotoController;
import com.example.yunita.tradiogc.trade.TradeActivity;
import com.example.yunita.tradiogc.user.UserController;
import java.util.ArrayList;


/**
 * This activity handles viewing an item's details.
 */
public class ItemActivity extends AppCompatActivity {

    private Item item;
    private Context context = this;
    private Categories categories;
    private String perspective = "";
    private UserController userController;
    private int index;
    private PhotoController photoController;

    private LinearLayout friend_panel;  // Shown when wanting to make a trade with an item
    private ImageButton edit_button;    // Shown when the item is part of the user's inventory
    private TextView name;
    private TextView category;
    private TextView price;
    private TextView description;
    private TextView quantity;
    private TextView quality;
    private ImageView itemImage;
    private Photo photo;
    private ArrayList<String> photos;
    private int photoIndex;

    /**
     * Sets item.
     *
     * @param item item that is viewed
     */
    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * Gets the name of the item.
     *
     * @return name name of the item
     */
    public TextView getName() {
        return name;
    }

    /**
     * Gets the category of the item.
     *
     * @return category category of the item
     */
    public TextView getCategory() {
        return category;
    }

    /**
     * Gets the price of the item.
     *
     * @return price price of the item
     */
    public TextView getPrice() {
        return price;
    }

    /**
     * Gets the description of the item.
     *
     * @return description description of the item
     */
    public TextView getDescription() {
        return description;
    }

    /**
     * Gets the quantity of the item.
     *
     * @return quantity quantity of the item
     */
    public TextView getQuantity() {
        return quantity;
    }

    /**
     * Gets the quality of the item.
     *
     * @return quality quality of the item
     */
    public TextView getQuality() {
        return quality;
    }

    /**
     * Gets the "Edit" button.
     *
     * @return edit_button "Edit" button
     */
    public ImageButton getEdit_button() {
        return edit_button;
    }

    /**
     * Called when the user makes a changes and need to update the GUI details.
     *
     * @return doUpdateGUIDetails values from updating the GUI
     */
    public Runnable getDoUpdateGUIDetails() {
        return doUpdateGUIDetails;
    }

    /**
     *  Updates the GUI details.
     */
    private Runnable doUpdateGUIDetails = new Runnable() {
        public void run() {
            // Hasn't been tested yet
            // Need to check if the item has a photo
            // If no photo, we need to set the visibility of itemImage to "gone"
            //photo.setImage... waiting for photo to be implemented
            if (item != null) {
                name.setText(item.getName());
                category.setText(categories.getCategories().get(item.getCategory()));
                price.setText("$" + Double.toString(item.getPrice()));
                description.setText(item.getDesc());
                quantity.setText(Integer.toString(item.getQuantity()));
                if (item.getQuality() == 0) {
                    quality.setText("New");
                } else {
                    quality.setText("Used");
                }

                if (LoginActivity.USERLOGIN.getDownloadPhotos()) {
                    photoController.getItem(item.getId());
                    photo = photoController.getPhoto();
                    photos = new ArrayList<>();
                    if (photo != null) {
                        photos = photo.getEncodedPhoto();
                        itemImage.setImageBitmap(decodeImage(photos.get(photoIndex)));
                    } else {
                        photo = new Photo();
                    }
                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        friend_panel = (LinearLayout) findViewById(R.id.friend_button_panel_item);
        edit_button = (ImageButton) findViewById(R.id.edit_button);
        userController = new UserController(context);
        photoController = new PhotoController(context);

        photoIndex = 0;
        itemImage = (ImageView) findViewById(R.id.itemImage);
        name = (TextView) findViewById(R.id.itemName);
        category = (TextView) findViewById(R.id.itemCategory);
        price = (TextView) findViewById(R.id.itemPrice);
        description = (TextView) findViewById(R.id.itemDescription);
        quantity = (TextView) findViewById(R.id.itemQuantity);
        quality = (TextView) findViewById(R.id.itemQuality);
    }

    /**
     * Alters the layout depending on if this user is viewing the item as a friend or
     * as the owner of the item.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        categories = new Categories();

        photoIndex = 0;

        if (intent.getExtras() != null) {
            perspective = intent.getExtras().getString("owner");
            index = intent.getExtras().getInt("index");
            if (perspective.equals("owner")) {
                System.out.println(">>>" + LoginActivity.USERLOGIN.getInventory().size());
                item = LoginActivity.USERLOGIN.getInventory().get(index);
                System.out.println(LoginActivity.USERLOGIN.getInventory().size());
            } else {
                item = (Item) intent.getSerializableExtra("item");
            }
            // Checks to see if we are getting a username from the intent
            if (perspective.equals("friend")) {
                edit_button.setVisibility(View.GONE);
                friend_panel.setVisibility(View.VISIBLE);
            }
        }

        runOnUiThread(doUpdateGUIDetails);
    }

    /**
     * Updates this user's inventory and the list view.
     */
    @Override
    protected void onResume() {
        super.onResume();

        photoIndex = 0;
        if (perspective!=null && perspective.equals("owner")) {
            Thread getUserLoginThread = userController.new GetUserLoginThread(LoginActivity.USERLOGIN.getUsername());
            getUserLoginThread.start();
            synchronized (getUserLoginThread) {
                try {
                    getUserLoginThread.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(doUpdateGUIDetails);
            }
        }
    }

    /**
     * Activity finishes automatically if user offers a trade for this item
     *
     * @param requestCode   request code for the sender that will be associated
     *                      with the result data when it is returned
     * @param resultCode    the integer result code returned by the child activity
     * @param data          an intent that can return result data to the caller
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 1){
            finish();
        }
    }

    /**
     * Called when the user presses the "Pencil" icon in the Item Detail page.
     * <p>This method is used to send the user to the Edit Item page.
     * It passes the item to be edited.
     *
     * @param view "Pencil" icon in Item Detail page
     */
    public void editItem(View view) {
        Intent intent = new Intent(context, EditItemActivity.class);
        intent.putExtra("index", index);
        startActivity(intent);
    }

    //

    /**
     * Decodes the encoded string into an image and returns it.
     * Code taken from:
     * http://stackoverflow.com/questions/4837110/how-to-convert-a-base64-string-into-a-bitmap-image-to-show-it-in-a-imageview
     * (C) 2011 user432209
     *
     * @param encoded encoded image in string format.
     * @return decodedByte Bitmap of the decoded photo string
     */
    public Bitmap decodeImage(String encoded) {
        byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    /**
     * Called when the user presses the "Clone" button in the Item Detail page.
     * <p>This method is used to clone the friend's item and copy the item info into AddItemActivity.
     *
     * @param view "Clone" button
     */
    public void cloneItem(View view) {
        Intent intent = new Intent(this, AddItemActivity.class);
        intent.putExtra("clone",item);
        startActivity(intent);
    }

    /**
     * Called when the user presses the "Create Trade" button in the Item Detail page.
     * <p>This method is used to send the user to the Trade page.
     *
     * @param view "Create Trade" button
     */
    public void makeTrade(View view) {
        Intent searchItemIntent = getIntent();
        String ownerName = searchItemIntent.getExtras().getString("owner_name");
        Item itemForTrade = (Item) searchItemIntent.getExtras().getSerializable("item");

        // call another intent
        Intent intent = new Intent(context, TradeActivity.class);
        intent.putExtra("owner_name", ownerName);
        intent.putExtra("item_for_trade", itemForTrade);
        intent.putExtra("borrower_name", LoginActivity.USERLOGIN.getUsername());
        int result = 0;
        startActivityForResult(intent, result);
    }
    public void nextPhoto (View v){
        if (photo != null) {
            photoIndex++;
            if (photoIndex >= photos.size()) {
                photoIndex = 0;
            }
            itemImage.setImageBitmap(decodeImage(photos.get(photoIndex)));
        }
    }
}
