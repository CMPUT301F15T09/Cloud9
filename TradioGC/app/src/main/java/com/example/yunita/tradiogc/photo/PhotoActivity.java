/*
 * taken from https://github.com/abramhindle/BogoPicGen
 * (C) 2015 Abram Hindle modified by Cloud9
 */

package com.example.yunita.tradiogc.photo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.yunita.tradiogc.R;

import java.io.File;

public class PhotoActivity extends Activity {

    Uri imageFileUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_main);

        ImageButton button = (ImageButton) findViewById(R.id.TakeAPhoto);
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v){
                takeAPhoto();
            }
        };
        button.setOnClickListener(listener);
    }

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    public void takeAPhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String folder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp";
        File folderF = new File(folder);
        if (!folderF.exists()) {
            folderF.mkdir();
        }

        String imageFilePath = folder + "/" + String.valueOf(System.currentTimeMillis()) + "jpg";
        File imageFile = new File(imageFilePath);
        imageFileUri = Uri.fromFile(imageFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            TextView tv = (TextView) findViewById(R.id.status);
            if (resultCode == RESULT_OK) {
                tv.setText("Photo OK!");
                ImageButton button = (ImageButton) findViewById(R.id.TakeAPhoto);
                button.setImageDrawable(Drawable.createFromPath(imageFileUri.getPath()));
            } else if (resultCode == RESULT_CANCELED) {
                tv.setText("Photo canceled");
            } else {
                tv.setText("Not sure what happened!" + resultCode);
            }
        }
    }
}