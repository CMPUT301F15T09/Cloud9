package com.example.yunita.tradiogc.photo;

import java.util.ArrayList;

public class Photos extends ArrayList<Photo> {

    private ArrayList<Photo> Photos;

    public Photos(ArrayList<Photo> Photos) {
        this.Photos = Photos;
    }

    public void addPhoto (Photo photo){
        Photos.add(photo);
    }
    public void delPhoto(Photo photo){
        Photos.remove(Photos.indexOf(photo));
    }

    public ArrayList<Photo> getPhotosAtId(int id){
        ArrayList<Photo> relevantPhotos = new ArrayList<>();
        for (int i = 0; i<Photos.size();i++){
            if (Photos.get(i).getitemId() == id){
                relevantPhotos.add(Photos.get(i));
            }
        }
        return relevantPhotos;
    }

}
