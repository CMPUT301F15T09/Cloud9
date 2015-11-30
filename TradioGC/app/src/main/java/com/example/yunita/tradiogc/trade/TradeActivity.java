package com.example.yunita.tradiogc.trade;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.inventory.Item;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.user.User;
import com.example.yunita.tradiogc.user.UserController;

/**
 * This activity handles trades made between two users.
 */
public class TradeActivity extends AppCompatActivity {

    private Context context = this;

    private TextView tradeWith;
    private TextView ownerItemName;
    private TextView ownerItemPrice;
    private TextView ownerItemDescription;
    private ImageView ownerItemPhoto;

    private ListView borrowerInventoryListView;
    private ArrayAdapter<Item> borrowerInventoryArrayAdapter;
    private Inventory borrowerInventory = new Inventory();

    private ListView borrowerOfferListView;
    private ArrayAdapter<Item> borrowerOfferArrayAdapter;
    private Inventory borrowerOffer = new Inventory();

    private Item ownerItem;
    private String ownerName;
    private User borrower = LoginActivity.USERLOGIN;
    private Trade offeredTrade;
    private Trade pendingTrade;

    private UserController userController;

    /**
     * Gets the "Offer Trade" button.
     *
     * @return Button "Offer Trade" button
     */
    public Button getOfferTradeButton(){
        return (Button) findViewById(R.id.offer_trade_button);
    }

    /**
     * Gets the borrower's inventory list view.
     *
     * @return borrowerInventoryListView borrower's inventory list view
     */
    public ListView getBorrowerInventoryListView() {
        return borrowerInventoryListView;
    }

    /**
     * Gets the borrower's offered list of items.
     *
     * @return borrowerOfferListView borrower's list view of offered items
     */
    public ListView getBorrowerOfferListView() {
        return borrowerOfferListView;
    }

    /**
     * Gets the "X" button for cancelling trades.
     *
     * @return Button "X" button
     */
    public Button getCancelButton(){
        return (Button) findViewById(R.id.cancel_button);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trade_display);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        borrowerInventoryListView = (ListView) findViewById(R.id.trade_inventory_list_view);
        borrowerOfferListView = (ListView) findViewById(R.id.my_offer_list_view);
        tradeWith = (TextView) findViewById(R.id.trade_with);



        ownerItemName = (TextView) findViewById(R.id.ownerItemName);
        ownerItemPrice = (TextView) findViewById(R.id.ownerItemPrice);
        ownerItemDescription = (TextView) findViewById(R.id.ownerItemDescription);
        ownerItemPhoto = (ImageView) findViewById(R.id.ownerItemPhoto);

        userController = new UserController(context);

    }

    @Override
    protected void onStart() {
        super.onStart();

        borrowerInventory.addAll(LoginActivity.USERLOGIN.getInventory());

        borrowerInventoryArrayAdapter = new ArrayAdapter<Item>(this, R.layout.inventory_list_item, borrowerInventory);
        borrowerInventoryListView.setAdapter(borrowerInventoryArrayAdapter);

        borrowerOfferArrayAdapter = new ArrayAdapter<Item>(this, R.layout.inventory_list_item, borrowerOffer);
        borrowerOfferListView.setAdapter(borrowerOfferArrayAdapter);

        // get the owner name and trade item from intent
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                ownerItem = (Item) bundle.getSerializable("item_for_trade");
                ownerName = bundle.getString("owner_name");
            }
        }

        // Set trade with
        tradeWith.setText("Trade with " + ownerName);
        tradeWith.setTypeface(null, Typeface.BOLD);

        // Set item photo and information
        //LOAD PHOTO
        //Bitmap itemPhoto = decodeImage(ownerItem.getPhotos());
        //LOAD PHOTO
        //ownerItemPhoto.setImageBitmap(itemPhoto);
        ownerItemName.setText(ownerItem.getName()+"\n" +
                "Owner: "+ownerName);
        ownerItemPrice.setText("$" + Double.toString(ownerItem.getPrice()) + " x " + ownerItem.getQuantity());
        ownerItemDescription.setText("Description: "+ownerItem.getDesc());


        borrowerInventoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = borrowerInventory.get(position);
                borrowerInventory.remove(item);
                borrowerOffer.add(item);
                borrowerInventoryArrayAdapter.notifyDataSetChanged();
                borrowerOfferArrayAdapter.notifyDataSetChanged();
            }
        });
        borrowerOfferListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = borrowerOffer.get(position);
                borrowerOffer.remove(item);
                borrowerInventory.add(item);
                borrowerInventoryArrayAdapter.notifyDataSetChanged();
                borrowerOfferArrayAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Called when the user presses the "Offer Trade" button.
     * <p>This method creates a new trade with the owner and borrower items.
     * It sets the new trade as a pending trade for the borrower and an offered
     * trade to the owner. It also sends the owner a notification for the new trade.
     *
     * @param view "offer trade" button.
     */
    public void offerTrade(View view){
        // Owner has an offered trade
        offeredTrade = new Trade(ownerName, borrower.getUsername(),ownerItem, borrowerOffer);
        offeredTrade.setStatus("offered");

        // Borrower has a pending trade
        pendingTrade = new Trade(ownerName, borrower.getUsername(), ownerItem, borrowerOffer);
        pendingTrade.setId(offeredTrade.getId());
        pendingTrade.setStatus("pending");

        // Save the pending trade in the borrower's trades
        SavePendingTradeThread savePendingTradeThread = new SavePendingTradeThread();
        savePendingTradeThread.start();

        // Send notification to the owner
        sendOwnerNotifThread sendOwnerNotifThread = new sendOwnerNotifThread(ownerName);
        sendOwnerNotifThread.start();

        // Finish the parent item detail activity
        setResult(1);

        finish();
    }

    /**
     * Called when the user presses the "X" button.
     * <p>This method cancels the trade that is getting composed.
     *
     * @param view "X" button
     */
    public void cancelTrade(View view) {
        finish();
    }

    /**
     * Decodes the encoded string into an image and returns it.
     * Code taken from:
     * http://stackoverflow.com/questions/4837110/how-to-convert-a-base64-string-into-a-bitmap-image-to-show-it-in-a-imageview
     * (C) 2011 user432209
     *
     * @param encoded       encoded image in string format
     * @return decodedByte  decoded image in a Bitmap format
     */
    public Bitmap decodeImage(String encoded) {
        byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    /**
     * Sends the owner a notification for the new trade.
     */
    class sendOwnerNotifThread extends Thread {
        private String username;

        public sendOwnerNotifThread(String username) {
            this.username = username;
        }

        @Override
        public void run() {
            User owner = userController.getUser(username);
            owner.getTrades().add(0, offeredTrade);
            System.out.println(offeredTrade.getClass());
            // Notifies owner
            Thread updateTradeThread = userController.new UpdateUserThread(owner);
            updateTradeThread.start();
        }
    }

    /**
     * Saves the new trade as a pending trade for the borrower.
     */
    class SavePendingTradeThread extends Thread {
        public SavePendingTradeThread() {}

        @Override
        public void run() {
            borrower.getTrades().add(0, pendingTrade);
            Thread updateTradeThread = userController.new UpdateUserThread(borrower);
            updateTradeThread.start();
        }
    }
}
