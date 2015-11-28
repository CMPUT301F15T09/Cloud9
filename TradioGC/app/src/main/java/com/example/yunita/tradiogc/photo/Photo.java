package com.example.yunita.tradiogc.photo;

import java.util.Random;

/**
 * Created by drblake on 11/28/15.
 */
public class Photo {

    private String photo;
    private int photoId;

    public Photo(String photo, int itemId) {
        Random random = new Random();
        photoId = random.nextInt(999999999);
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }
}