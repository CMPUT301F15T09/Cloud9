package com.example.yunita.tradiogc.photo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class defines photos of an item.
 */
public class Photo {

    private ArrayList<String> encodedPhoto = new ArrayList<>();
    private int itemId;

    /**
     * Class constructor for photos.
     */
    public Photo(){}

    /**
     * Class constructor specifying the details of photos.
     *
     * @param itemId id number for the item the photos are associated with
     */
    public Photo(int itemId){
        this.itemId = itemId;
    }

    /**
     * Gets the id of the item that the photos belong to.
     *
     * @return itemId id of the item
     */
    public int getItemId() {
        return itemId;
    }

    /**
     * Changes the id of the item that the photos belong to.
     *
     * @param itemId id of the item
     */
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    /**
     * Gets the array list of encoded photos of an item.
     *
     * @return encodedPhoto list of encoded pictures
     */
    public ArrayList<String> getEncodedPhoto() {
        return encodedPhoto;
    }

    /**
     * Adds an encoded photo to an item's list of photos.
     *
     * @param bitmap bitmap image to be added to the list of photos.
     */
    public void addEncodedPhoto(Bitmap bitmap){
        this.encodedPhoto.add(encodeImage(bitmap));
    }

    /**
     * Encodes the photo into a string and returns it.
     * Code taken from:
     * http://stackoverflow.com/questions/9768611/encode-and-decode-bitmap-object-in-base64-string-in-android
     * (C) 2015 Roman Truba, modified by Cloud9
     *
     * @param photo         photo taken from camera
     * @return encodedImage image that has been encoded into a string byte array
     */
    public String encodeImage(Bitmap photo) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    /**
     * Decodes the photo from a string into a photo and returns it.
     *
     * @param encoded       encoded string of a photo
     * @return decodedByte  decoded Bitmap photo
     */
    public Bitmap decodeImage(String encoded){
        byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
