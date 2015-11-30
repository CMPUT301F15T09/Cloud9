/*
 * taken from https://github.com/abramhindle/BogoPicGen
 * (C) 2015 Abram Hindle modified by Cloud9
 */

package com.example.yunita.tradiogc.photo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.inventory.Item;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.market.ItemSearchActivity;

import java.io.File;

/**
 * This activity handles
 */
public class PhotoActivity extends Activity {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri imageFileUri;
    private Context context = this;
    private ImageView tempPhoto;
    private String imageFilePath;
    private PhotoController photoController;

    private Photo photo = new Photo();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_main);

        photoController = new PhotoController(context);

        ImageButton button = (ImageButton) findViewById(R.id.TakeAPhoto);
        tempPhoto = (ImageView) findViewById(R.id.temp_photo_view);

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                takeAPhoto();
            }
        };
        button.setOnClickListener(listener);

//        GetPhotoThread getPhotoThread = new GetPhotoThread(1);
//        getPhotoThread.start();
//        synchronized (getPhotoThread) {
//            try {
//                getPhotoThread.wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        System.out.println("ON CREATE PHOTO");
    }

    public void takeAPhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String folder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tradiogc";
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap thumbnail = BitmapFactory.decodeFile(imageFileUri.getPath());
                double tHeight = thumbnail.getHeight() * 0.2;
                double tWidth = thumbnail.getWidth() * 0.2;

                Bitmap scaledPhoto = Bitmap.createScaledBitmap(thumbnail, (int) tWidth, (int) tHeight, true);
                tempPhoto.setImageBitmap(scaledPhoto);

                photo.addEncodedPhoto(scaledPhoto);

                photoController.addPhoto(1, photo);
            }
        }
    }

    public void cancelImage(View view) {
        File file = new File(imageFilePath);
        if (file.exists()) {
            file.delete();
        }
        tempPhoto.setImageResource(R.mipmap.ic_launcher);
    }

    public void attachPhotoToItem(View view) {
    }

    public class GetPhotoThread extends Thread {
        private int itemId;

        public GetPhotoThread(int itemId) {
            this.itemId = itemId;
        }

        @Override
        public void run() {
            synchronized (this) {
                photo = photoController.getItemPhoto(itemId);
                notify();
            }
        }
    }

}