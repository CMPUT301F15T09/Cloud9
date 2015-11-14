package com.example.yunita.tradiogc.inventory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by JustinWong on 2015-11-13.
 */
public class ImageConverter {

    public static String imageToStringConverter(Bitmap image){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String imageToString = Base64.encodeToString(byteArray, Base64.NO_WRAP);
        return imageToString;
    }

    public static Bitmap stringToimageConverter(String imageString){
        byte[] stringTobyte = Base64.decode(imageString, Base64.NO_WRAP);
        Bitmap bmp = BitmapFactory.decodeByteArray(stringTobyte, 0, stringTobyte.length);
        return bmp;
    }

}

//code used from http://stackoverflow.com/questions/28881981/facing-image-uploading-and-retrieving-difficulties-to-datastore-in-android
//not used but might come in handy for very large image files