package com.example.yunita.tradiogc.inventory;

import android.support.v7.app.AppCompatActivity;
import java.io.FileNotFoundException;
import java.io.InputStream;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yunita.tradiogc.R;


/**
 * Created by JustinWong on 2015-11-12.
 */
public class UploadPhotoActivity extends AppCompatActivity {
    private final int SELECT_PHOTO = 1;
    //private ImageView imageView;
    private ImageView tempPhoto;
    private Bitmap thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_inventory);

        tempPhoto = (ImageView) findViewById(R.id.temp_photo_view);
        //imageView = (ImageView) findViewById(R.id.imageView);

        Button pickImage = (Button) findViewById(R.id.SelectAPhoto);
        pickImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                //The commented out code below selects images from anywhere. The above selects from only from the gallery.
                //Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                        double tHeight = selectedImage.getHeight() * 0.2;
                        double tWidth = selectedImage.getWidth() * 0.2;
                        thumbnail = Bitmap.createScaledBitmap(selectedImage, (int) tWidth, (int) tHeight, true);

                        tempPhoto.setImageBitmap(thumbnail);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
*/
}

//code used from http://javatechig.com/android/writing-image-picker-using-intent-in-android