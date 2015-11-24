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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.inventory.Item;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.user.User;
import com.example.yunita.tradiogc.user.UserController;

public class TradeDetailActivity extends AppCompatActivity {
    private TextView tradeFrom;
    private TextView ownerItemName;
    private TextView ownerItemPrice;
    private TextView ownerItemDescription;
    private ImageView ownerItemPhoto;
    private ListView itemsOfferedList;
    private LinearLayout offeredTradePanel;


    private ArrayAdapter<Item> itemsOfferedArrayAdapter;
    private Inventory itemsOffered = new Inventory();
    private UserController userController;
    private Context context = this;
    private Trade trade = new Trade();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trade_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        itemsOfferedList = (ListView) findViewById(R.id.item_offered_list_view);
        tradeFrom = (TextView) findViewById(R.id.trade_from);

        ownerItemName = (TextView) findViewById(R.id.ownerItemName);
        ownerItemPrice = (TextView) findViewById(R.id.ownerItemPrice);
        ownerItemDescription = (TextView) findViewById(R.id.ownerItemDescription);
        ownerItemPhoto = (ImageView) findViewById(R.id.ownerItemPhoto);
        offeredTradePanel = (LinearLayout) findViewById(R.id.offered_trade_panel);

        userController = new UserController(context);
    }

    @Override
    protected void onStart() {
        super.onStart();

        itemsOfferedArrayAdapter = new ArrayAdapter<Item>(this, R.layout.inventory_list_item, itemsOffered);
        itemsOfferedList.setAdapter(itemsOfferedArrayAdapter);

        // get trade by index
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getExtras() != null) {
                int tradeId = intent.getExtras().getInt("trade_id");
                trade = LoginActivity.USERLOGIN.getTrades().findTradeById(tradeId);
            }
        }

        // set trade from
        tradeFrom.setText("Trade from " + trade.getBorrower());
        tradeFrom.setTypeface(null, Typeface.BOLD);


        // set item photo and information
        Bitmap itemPhoto = decodeImage(trade.getOwnerItem().getPhotos());
        ownerItemPhoto.setImageBitmap(itemPhoto);
        ownerItemName.setText(trade.getOwnerItem().getName());
        ownerItemPrice.setText(Double.toString(trade.getOwnerItem().getPrice()));
        ownerItemDescription.setText(trade.getOwnerItem().getDesc());

        // set items offered
        itemsOffered.addAll(trade.getBorrowerItems());

        // set panel showed
        if (trade.getStatus().equals("offered")) {
            offeredTradePanel.setVisibility(View.VISIBLE);
        }
    }

    /**
     *
     * @param view "accept trade" button.
     */
    public void accept(View view){
        trade.setStatus("current");
        Thread replyThread = new ReplyThread("accepted");
        replyThread.start();
        finish();
    }

    /**
     *
     * @param view "decline trade" button.
     */
    public void decline(View view){
        LoginActivity.USERLOGIN.getTrades().remove(trade);
        Thread replyThread = new ReplyThread("declined");
        replyThread.start();
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
     * reply to the borrower
     */

    class ReplyThread extends Thread {
        private String status;

        public ReplyThread(String status) {
            this.status = status;
        }

        @Override
        public void run() {
            User borrower = userController.getUser(trade.getBorrower());
            Trade borrowerTrade = borrower.getTrades().findTradeById(trade.getId());
            if (borrowerTrade != null) {
                borrowerTrade.setStatus(status);
                // notify borrow
                Thread updateTradeThread = userController.new UpdateUserThread(borrower);
                updateTradeThread.start();
            }
        }
    }
}
