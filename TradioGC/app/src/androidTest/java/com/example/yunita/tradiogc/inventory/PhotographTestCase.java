package com.example.yunita.tradiogc.inventory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;

import com.example.yunita.tradiogc.inventory.Item;
import com.example.yunita.tradiogc.photo.Photo;

public class PhotographTestCase extends ActivityInstrumentationTestCase2 {

    public PhotographTestCase() {
        super(com.example.yunita.tradiogc.MainActivity.class);
    }

    /**
     * Use Case 30
     * 06.01.01, 06.04.01
     * Test for attaching a photograph to an item.
     */
    public void testAttachItemPhotograph(){
        Item item = new Item(1,"Chapters", 0, 50.00, "chapters gc", true, 1, 0);
        Photo photo = new Photo(item.getId());
        Bitmap bitmap = Bitmap.createBitmap(100, 200, Bitmap.Config.RGB_565);
        photo.addEncodedPhoto(bitmap);

        assertTrue(photo.getItemId() == item.getId());
        assertEquals(photo.getEncodedPhoto().size(), 1);
    }

    /**
     * Use Case 31
     * 06.02.01
     * Test for viewing an item's photograph.
     */
    public void testViewItemPhotograph(){
        Item item = new Item(1,"Chapters", 0, 50.00, "chapters gc", true, 1, 0);
        Photo photo = new Photo(item.getId());
        Bitmap bitmap = Bitmap.createBitmap(100, 200, Bitmap.Config.RGB_565);
        photo.addEncodedPhoto(bitmap);

        Bitmap thumbnail = photo.decodeImage(photo.getEncodedPhoto().get(0));
        assertNotNull(thumbnail);
    }

    /**
     * Use Case 32
     * 06.03.01
     * Test for removing an item's photograph.
     */
    public void testDeleteItemPhotograph(){
        Item item = new Item(1,"Chapters", 0, 50.00, "chapters gc", true, 1, 0);
        Photo photo = new Photo(item.getId());
        Bitmap bitmap = Bitmap.createBitmap(100, 200, Bitmap.Config.RGB_565);
        photo.addEncodedPhoto(bitmap);

        photo.getEncodedPhoto().remove(0);

        assertEquals(photo.getEncodedPhoto().size(), 0);

    }

    /**
     * Use Case 33
     * 6.05.01, 10.01.01
     * Test for enabling photograph downloads.
     */
    public void testEnablePhotographDownload(){
        Item item = new Item(1,"Chapters", 0, 50.00, "chapters gc", true, 1, 0);
        Photo photo = new Photo(item.getId());
        Bitmap bitmap = Bitmap.createBitmap(100, 200, Bitmap.Config.RGB_565);
        photo.addEncodedPhoto(bitmap);

        // do something
    }

    /**
     * Use Case 34
     * 06.05.01, 10.01.01
     * Test for disabling photograph downloads.
     */
    public void DisablePhotographDownload(){
        Item item = new Item(1,"Chapters", 0, 50.00, "chapters gc", true, 1, 0);
        Photo photo = new Photo(item.getId());
        Bitmap bitmap = Bitmap.createBitmap(100, 200, Bitmap.Config.RGB_565);
        photo.addEncodedPhoto(bitmap);

        // do something
    }

}
