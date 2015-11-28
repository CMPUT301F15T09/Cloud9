package com.example.yunita.tradiogc.photo;

import java.util.ArrayList;

public class Photos extends ArrayList<Photo> {

    public Photos(ArrayList<Photo> photos) {
        this.addAll(photos);
    }

    public void addPhoto (Photo photo){
        this.add(photo);
    }
    public void delPhoto(Photo photo){
        this.remove(this.indexOf(photo));
    }


}
