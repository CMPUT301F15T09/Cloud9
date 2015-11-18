package com.example.yunita.tradiogc.trade;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.inventory.Item;

public class TradeActivity extends AppCompatActivity {

    private Context context = this;

    private TextView tradeWith;
    private TextView ownerItemName;
    private TextView ownerItemPrice;
    private TextView ownerItemDescription;
    private ImageView ownerItemPhoto;

    private Item ownerItem;
    private String ownerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trade_display);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        tradeWith = (TextView) findViewById(R.id.trade_with);
        ownerItemName = (TextView) findViewById(R.id.ownerItemName);
        ownerItemPrice = (TextView) findViewById(R.id.ownerItemPrice);
        ownerItemDescription = (TextView) findViewById(R.id.ownerItemDescription);
        ownerItemPhoto = (ImageView) findViewById(R.id.ownerItemPhoto);

    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent ItemSearchIntent = getIntent();
        ownerItem = (Item) ItemSearchIntent.getExtras().getSerializable("item_for_trade");
        ownerName = ItemSearchIntent.getExtras().getString("owner_name");

        // set trade with
        tradeWith.setText("Trade with " + ownerName);

        // set item photo and information
        Bitmap itemPhoto = decodeImage(ownerItem.getPhotos());
        ownerItemPhoto.setImageBitmap(itemPhoto);
        ownerItemName.setText(ownerItem.getName());
        ownerItemPrice.setText(Double.toString(ownerItem.getPrice()));
        ownerItemDescription.setText(ownerItem.getDesc());

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

}
