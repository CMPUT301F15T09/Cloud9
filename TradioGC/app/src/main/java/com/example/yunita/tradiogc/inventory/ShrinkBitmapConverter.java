package com.example.yunita.tradiogc.inventory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

import java.io.FileNotFoundException;

/**
 * Created by JustinWong on 2015-11-13.
 */
public class ShrinkBitmapConverter {
    Context context;
    public ShrinkBitmapConverter(Context c){
        context=c;
    }

    public Bitmap shrinkBitmap(Uri uri,int width,int height) throws FileNotFoundException {

        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;

        Bitmap bitmap = null;;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri),null,bmpFactoryOptions);

            int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight/(float)height);
            int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth/(float)width);

            if (heightRatio > 1 || widthRatio > 1)
            {
                if (heightRatio > widthRatio)
                {
                    bmpFactoryOptions.inSampleSize = heightRatio;
                } else {
                    bmpFactoryOptions.inSampleSize = widthRatio;
                }
            }

            bmpFactoryOptions.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri),null,bmpFactoryOptions);

        } catch (Exception e) {

            Toast.makeText(context, "Image Not Found", Toast.LENGTH_SHORT).show();
        }

        return bitmap;
    }
}

//code used from http://stackoverflow.com/questions/28881981/facing-image-uploading-and-retrieving-difficulties-to-datastore-in-android
//not used but might come in handy for very large image files