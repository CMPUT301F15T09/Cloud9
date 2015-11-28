package com.example.yunita.tradiogc.photo;

import java.util.Random;

/**
 * Created by drblake on 11/28/15.
 */
public class Photo {

    private String photo;
    private int itemId;
    private int photoId;

    public Photo(String photo, int itemId) {
        Random random = new Random();
        photoId = random.nextInt(999999999);

        this.photo = photo;
        this.itemId = itemId;
    }

    public void setphotoId(int i){
        photoId = i;
    }
    public int getphotoId(){
        return photoId;
    }
    public void setitemId(int i){
        itemId = i;
    }
    public int getitemId() {
        return itemId;
    }

    public void setPhoto(String p){
        photo = p;
    }
    public String getPhoto(){
        return photo;
    }

}
