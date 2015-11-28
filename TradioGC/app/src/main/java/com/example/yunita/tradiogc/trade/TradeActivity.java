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

    public Button getOfferTradeButton(){
        return (Button) findViewById(R.id.offer_trade_button);
    }

    public ListView getBorrowerInventoryListView() {
        return borrowerInventoryListView;
    }

    public ListView getBorrowerOfferListView() {
        return borrowerOfferListView;
    }

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

        // set trade with
        tradeWith.setText("Trade with " + ownerName);
        tradeWith.setTypeface(null, Typeface.BOLD);

        // set item photo and information
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
     *
     * @param view "offer trade" button.
     */
    public void offerTrade(View view){
        // owner has offered trade
        offeredTrade = new Trade(ownerName, borrower.getUsername(),ownerItem, borrowerOffer);
        offeredTrade.setStatus("offered");

        // borrower has pending trade
        pendingTrade = new Trade(ownerName, borrower.getUsername(), ownerItem, borrowerOffer);
        pendingTrade.setId(offeredTrade.getId());
        pendingTrade.setStatus("pending");

        // save the pending trade in borrower trades
        SavePendingTradeThread savePendingTradeThread = new SavePendingTradeThread();
        savePendingTradeThread.start();

        // send notification to owner
        sendOwnerNotifThread sendOwnerNotifThread = new sendOwnerNotifThread(ownerName);
        sendOwnerNotifThread.start();

        // after offer a trade, should the borrower item is on hold?
        // so that no one can offer a trade to that borrower item.

        // finish the parent item detail activity
        setResult(1);

        finish();
    }

    /**
     *
     * @param view "cancel trade" button.
     */
    public void cancelTrade(View view) {
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
            User owner = userController.getUser(username);
            owner.getTrades().add(0, offeredTrade);
            System.out.println(offeredTrade.getClass());
            // notify owner
            Thread updateTradeThread = userController.new UpdateUserThread(owner);
            updateTradeThread.start();
        }
    }

    /**
     * for borrower
     */
    class SavePendingTradeThread extends Thread {
        public SavePendingTradeThread() {}

        @Override
        public void run() {
            borrower.getTrades().add(0, pendingTrade);
            // notify owner
            Thread updateTradeThread = userController.new UpdateUserThread(borrower);
            updateTradeThread.start();
        }
    }
}
