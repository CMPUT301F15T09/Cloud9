package com.example.yunita.tradiogc.trade;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.example.yunita.tradiogc.email.GMailSender;

import org.apache.commons.lang3.text.WordUtils;

public class TradeDetailActivity extends AppCompatActivity {
    private TextView tradeFrom;
    private TextView ownerItemName;
    private TextView ownerItemPrice;
    private TextView ownerItemDescription;
    private ImageView ownerItemPhoto;
    private ListView itemsOfferedList;
    private LinearLayout offeredTradePanel;
    private TextView status;

    private ArrayAdapter<Item> itemsOfferedArrayAdapter;
    private Inventory itemsOffered = new Inventory();
    private UserController userController;
    private Context context = this;
    private Trade trade = new Trade();
    private boolean counterTrade = false;
    private User user = new User();

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
        status = (TextView) findViewById(R.id.status);

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

        if (LoginActivity.USERLOGIN.getUsername().equals(trade.getBorrower())){
            counterTrade = true;
        }

        // set trade from
        if (!counterTrade) {
            tradeFrom.setText("Trade with " + trade.getBorrower());
        } else {
            tradeFrom.setText("Trade with " + trade.getOwner());
        }

        tradeFrom.setTypeface(null, Typeface.BOLD);


        // set item photo and information
        Bitmap itemPhoto = decodeImage(trade.getOwnerItem().getPhotos());
        ownerItemPhoto.setImageBitmap(itemPhoto);
        ownerItemName.setText(trade.getOwnerItem().getName() + "\n" +
                "Owner: " + trade.getOwner());
        ownerItemPrice.setText("$"+Double.toString(trade.getOwnerItem().getPrice()) + " x " + trade.getOwnerItem().getQuantity());
        ownerItemDescription.setText(trade.getOwnerItem().getDesc());

        //TODO: remove wordutil
        // set trade status
        status.setText(WordUtils.capitalizeFully(trade.getStatus()));

        // set items offered
        itemsOffered.addAll(trade.getBorrowerItems());

        // set panel showed
        if (trade.getStatus().equals("offered")) {
            offeredTradePanel.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Activity finishes automatically if user offers a counter trade
     *
     * @param requestCode request code for the sender that will be associated
     *                    with the result data when it is returned
     * @param resultCode the integer result code returned by the child activity
     * @param data an intent, which can return result data to the caller
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 1){
            finish();
        }
    }

    /**
     *
     * @param view "accept trade" button.
     */
    public void accept(View view){
        // create a dialog asking for comments
        AlertDialog builder = new AlertDialog.Builder(this).create();
        final EditText comments_et = new EditText(this);
        comments_et.setHint("say something here...");

        builder.setTitle("Comments:");
        builder.setView(comments_et);
        builder.setButton(AlertDialog.BUTTON_NEGATIVE, "SEND", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                trade.setStatus("accepted");
                LoginActivity.USERLOGIN.getNotifications().remove(LoginActivity.USERLOGIN.getNotifications().findNotificationById(trade.getId()));
                Thread updateUserThread = userController.new UpdateUserThread(LoginActivity.USERLOGIN);
                updateUserThread.start();

                synchronized (updateUserThread) {
                    try {
                        updateUserThread.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                Thread replyThread = new ReplyThread("accepted");
                replyThread.start();
                synchronized (replyThread) {
                    try {
                        replyThread.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                String comments = comments_et.getText().toString();
                dialog.dismiss();

                Thread emailThread = new EmailThread(comments, user.getEmail(), LoginActivity.USERLOGIN.getEmail());
                emailThread.start();
                synchronized (emailThread) {
                    try {
                        emailThread.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                finish();
            }
        });
        builder.setButton(AlertDialog.BUTTON_POSITIVE, "CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    /**
     *
     * @param view "decline trade" button.
     */
    public void decline(View view){
        LoginActivity.USERLOGIN.getNotifications().remove(LoginActivity.USERLOGIN.getNotifications().findNotificationById(trade.getId()));
        LoginActivity.USERLOGIN.getTrades().remove(trade);
        Thread updateUserThread = userController.new UpdateUserThread(LoginActivity.USERLOGIN);
        updateUserThread.start();

        synchronized (updateUserThread) {
            try {
                updateUserThread.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Thread replyThread = new ReplyThread("declined");
        replyThread.start();

        // if it is a counter trade, back to notification directly
        // else show a dialog
        if (counterTrade) {
            finish();
        } else {
            // create a dialog asking for counter trade
            AlertDialog builder = new AlertDialog.Builder(this).create();
            builder.setMessage("Do you want to offer a counter trade?");
            builder.setButton(AlertDialog.BUTTON_NEGATIVE, "YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    // call another intent
                    Intent intent = new Intent(context, CounterTradeActivity.class);
                    intent.putExtra("item_for_trade", trade.getOwnerItem());
                    intent.putExtra("borrower_name", trade.getBorrower());
                    int result = 0;

                    startActivityForResult(intent, result);
                    finish();

                }
            });
            builder.setButton(AlertDialog.BUTTON_POSITIVE, "NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();

                }
            });
            builder.show();
        }
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
     * reply to another user in trade
     */
    class ReplyThread extends Thread {
        private String status;

        public ReplyThread(String status) {
            this.status = status;
        }

        @Override
        public void run() {
            synchronized (this) {
                if (counterTrade) {
                    user = userController.getUser(trade.getOwner());
                } else {
                    user = userController.getUser(trade.getBorrower());
                }
                Trade tradeFound = user.getTrades().findTradeById(trade.getId());
                if (tradeFound != null) {
                    tradeFound.setStatus(status);
                    // notify the user
                    Thread updateTradeThread = userController.new UpdateUserThread(user);
                    updateTradeThread.start();
                }
                notify();
            }
        }
    }

    public class EmailThread extends Thread {
        private String email1;
        private String email2;
        private String comments;


        public EmailThread(String comments, String email1, String email2) {
            this.comments = comments;
            this.email1 = email1;
            this.email2 = email2;
        }

        @Override
        public void run() {
            synchronized (this) {
                try {
                    //taken from http://stackoverflow.com/questions/2020088/sending-email
                    // -in-android-using-javamail-api-without-using-the-default-built-in-a
                    // (C) 2010 Vinayak B, shridutt kothari
                    GMailSender sender = new GMailSender("tradiogc@gmail.com", "tradiogc123");

                    /*
                    // only for test
                    sender.sendMail("TradioGC: Your trade is in progress now",
                            "\nComments from " + LoginActivity.USERLOGIN.getUsername() + ": " + comments + "\n",
                            "tradiogc@gmail.com",
                            "tradiogcjunkmail@yopmail.com");
                    */

                    // TODO: make an informative message
                    sender.sendMail("TradioGC: Your trade is in progress now",
                            "\nComments from " + LoginActivity.USERLOGIN.getUsername() + ": " + comments + "\n",
                            "tradiogc@gmail.com",
                             email1+ ","+email2);


                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
                notify();
            }
        }
    }
}
