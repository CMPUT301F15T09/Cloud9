package com.example.yunita.tradiogc.photo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class Photo {

    private ArrayList<String> encodedPhoto = new ArrayList<>();
    private int itemId;

    public Photo(){

    }

    public Photo(int itemId){
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }

        public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public ArrayList<String> getEncodedPhoto() {
        return encodedPhoto;
    }

    public void addEncodedPhoto(Bitmap bitmap){
        this.encodedPhoto.add(encodeImage(bitmap));
    }

    // taken from http://stackoverflow.com/questions/9768611/encode-and-decode-bitmap-object-in-base64-string-in-android
    // (C) 2015 Roman Truba modified by Cloud9

    /**
     * Encodes the photo into a string and returns it.
     *
     * @param photo photo taken from camera.
     * @return String.
     */
    public String encodeImage(Bitmap photo) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    /**
     *
     * @param encoded
     * @return
     */
    public Bitmap decodeImage(String encoded){
        byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

}
