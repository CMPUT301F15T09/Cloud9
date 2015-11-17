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

public class TradeActivity extends AppCompatActivity{

    private Context context = this;

    private TextView ownerItemInformation;
    private ImageView ownerItemPhoto;

    private Item ownerItem;
    private String ownerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trade_display);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        ownerItemInformation = (TextView) findViewById(R.id.ownerItemInformation);
        ownerItemPhoto = (ImageView) findViewById(R.id.ownerItemPhoto);

    }

    @Override
    protected void onStart(){
        super.onStart();

        Intent ItemSearchIntent = getIntent();
        ownerItem = (Item) ItemSearchIntent.getExtras().getSerializable("item");
        ownerName = ItemSearchIntent.getExtras().getString("owner_item");

        Bitmap itemPhoto = decodeImage(ownerItem.getPhotos());
        ownerItemPhoto.setImageBitmap(itemPhoto);
    }


    // taken from http://stackoverflow.com/questions/4837110/how-to-convert-a-base64-string-into-a-bitmap-image-to-show-it-in-a-imageview
    // (C) 2011 user432209

    /**
     * Decodes the encoded string into an image and returns it.
     *
     * @param encoded encoded image in string format.
     * @return Bitmap.
     */
    public Bitmap decodeImage(String encoded){
        byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

}
