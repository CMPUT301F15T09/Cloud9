package com.example.yunita.tradiogc;

import android.test.ActivityInstrumentationTestCase2;

import com.example.yunita.tradiogc.inventory.Item;

public class PhotographsUseCaseTest extends ActivityInstrumentationTestCase2 {

    public PhotographsUseCaseTest() {
        super(com.example.yunita.tradiogc.MainActivity.class);
    }

    // 06.01.01, 06.04.01
    public void testAttachItemPhotograph(){
        Item item = new Item("Chapters", 0, 50.00, "chapters gc", true, 1, 0, "");
        item.setPhotos("image");

        assertTrue(!item.getPhotos().equals(""));
    }

    // 06.02.01
    public void testViewItemPhotograph(){
        Item item = new Item("Chapters", 0, 50.00, "chapters gc", true, 1, 0, "");
        item.setPhotos("image");

        assertTrue(item.getPhotos().equals("image"));
    }

    // 06.03.01
    public void testDeleteItemPhotograph(){
        Item item = new Item("Chapters", 0, 50.00, "chapters gc", true, 1, 0, "image");
        // removing photo means setting photo attributes into empty string.
        item.setPhotos("");

        assertTrue(item.getPhotos().equals(""));
    }

    // 06.05.01, 10.01.01
    public void testEnablePhotographDownload(){

    }

    // 06.05.01, 10.01.01
    public void DisablePhotographDownload(){

    }

}
