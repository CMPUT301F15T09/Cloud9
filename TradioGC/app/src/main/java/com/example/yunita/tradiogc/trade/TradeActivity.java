package com.example.yunita.tradiogc.trade;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.inventory.Item;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.user.User;
import com.example.yunita.tradiogc.user.UserController;

public class TradeActivity extends AppCompatActivity {

    private Context context = this;

    private TextView tradeWith;
    private TextView ownerItemName;
    private TextView ownerItemPrice;
    private TextView ownerItemDescription;
    private ImageView ownerItemPhoto;

    private ListView borrowerInventoryListView;
    private ArrayAdapter<Item> borrowerInventoryArrayAdapter;
    private Inventory borrowerInventory;

    private ListView borrowerOfferListView;
    private ArrayAdapter<Item> borrowerOfferArrayAdapter;
    private Inventory borrowerOffer = new Inventory();

    private Item ownerItem;
    private String ownerName;
    private User owner;
    private User borrower = LoginActivity.USERLOGIN;
    private OfferedTrade offeredTrade;
    private PendingTrade pendingTrade;

    private UserController userController;

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

        borrowerInventory = new Inventory(LoginActivity.USERLOGIN.getInventory());

        borrowerInventoryArrayAdapter = new ArrayAdapter<Item>(this, R.layout.inventory_list_item, borrowerInventory);
        borrowerInventoryListView.setAdapter(borrowerInventoryArrayAdapter);

        borrowerOfferArrayAdapter = new ArrayAdapter<Item>(this, R.layout.inventory_list_item, borrowerOffer);
        borrowerOfferListView.setAdapter(borrowerOfferArrayAdapter);

        Intent ItemSearchIntent = getIntent();
        ownerItem = (Item) ItemSearchIntent.getExtras().getSerializable("item_for_trade");
        ownerName = ItemSearchIntent.getExtras().getString("owner_name");

        // set trade with
        tradeWith.setText("Trade with " + ownerName);
        tradeWith.setTypeface(null, Typeface.BOLD);

        // set item photo and information
        Bitmap itemPhoto = decodeImage(ownerItem.getPhotos());
        ownerItemPhoto.setImageBitmap(itemPhoto);
        ownerItemName.setText(ownerItem.getName());
        ownerItemPrice.setText(Double.toString(ownerItem.getPrice()));
        ownerItemDescription.setText(ownerItem.getDesc());

        // taken from http://stackoverflow.com/questions/6210895/listview-inside-scrollview-is-not-scrolling-on-android
        // (C) 2014 Moisés Olmedo, bwest
        // this is to handle list view inside scroll view
        borrowerInventoryListView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

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
    }

    public void offerTrade(View view){
        // owner has offered trade
        offeredTrade = new OfferedTrade(ownerName, borrower.getUsername(),ownerItem, borrowerOffer);

        // borrower has pending trade
        pendingTrade = new PendingTrade(ownerName, borrower.getUsername(), ownerItem, borrowerOffer);

        // save the pending trade in borrower trades
        SavePendingTradeThread savePendingTradeThread = new SavePendingTradeThread(borrower.getUsername());
        savePendingTradeThread.start();

        // send notification to owner
        sendOwnerNotifThread sendOwnerNotifThread = new sendOwnerNotifThread(ownerName);
        sendOwnerNotifThread.start();

        // after offer a trade, should the borrower item is on hold?
        // so that no one can offer a trade to that borrower item.

        finish();
    }

    // taken from http://stackoverflow.com/questions/4837110/how-to-convert-a-base64-string-into-a-bitmap-image-to-show-it-in-a-imageview
    // (C) 2011 user432209

    /**
     * Decodes the encoded string into an image and returns it.
     *
     * @param encoded encoded image in string format.
     * @return Bitmap.
     */
    public Bitmap decodeImage(String encoded) {
        byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    /**
     * for owner
     */
    class sendOwnerNotifThread extends Thread {
        private String username;

        public sendOwnerNotifThread(String username) {
            this.username = username;
        }

        @Override
        public void run() {
            owner = userController.getUser(username);
            owner.getTrades().add(offeredTrade);
            // notify owner
            Thread updateTradeThread = userController.new UpdateUserThread(owner);
            updateTradeThread.start();
        }
    }

    /**
     * for borrower
     */
    class SavePendingTradeThread extends Thread {
        private String username;

        public SavePendingTradeThread(String username) {
            this.username = username;
        }

        @Override
        public void run() {
            borrower.getTrades().add(pendingTrade);
            // notify owner
            Thread updateTradeThread = userController.new UpdateUserThread(borrower);
            updateTradeThread.start();
        }
    }

}
