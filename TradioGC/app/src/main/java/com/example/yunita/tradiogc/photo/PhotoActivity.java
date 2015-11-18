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
import com.example.yunita.tradiogc.market.ItemSearchActivity;

import java.io.File;

public class PhotoActivity extends Activity {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri imageFileUri;
    private Context context = this;
    private ImageView tempPhoto;
    private String imageFilePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_main);

        ImageButton button = (ImageButton) findViewById(R.id.TakeAPhoto);
        tempPhoto = (ImageView) findViewById(R.id.temp_photo_view);

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                takeAPhoto();
            }
        };
        button.setOnClickListener(listener);
    }

    public void takeAPhoto() {
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap thumbnail = BitmapFactory.decodeFile(imageFileUri.getPath());
                double tHeight = thumbnail.getHeight() * 0.3;
                double tWidth = thumbnail.getWidth() * 0.3;

                Bitmap b = Bitmap.createScaledBitmap(thumbnail, (int) tWidth, (int) tHeight, true);
                tempPhoto.setImageBitmap(b);
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
        Intent intent = new Intent(context, ItemSearchActivity.class);
        intent.putExtra("image_url", imageFileUri);
        startActivity(intent);
    }
}