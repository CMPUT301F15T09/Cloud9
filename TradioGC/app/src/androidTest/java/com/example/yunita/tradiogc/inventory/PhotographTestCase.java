package com.example.yunita.tradiogc.inventory;

import android.test.ActivityInstrumentationTestCase2;

import com.example.yunita.tradiogc.inventory.Item;

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
        Item item = new Item("Chapters", 0, 50.00, "chapters gc", true, 1, 0, "");
        item.setPhotos("image");

        assertTrue(!item.getPhotos().equals(""));
    }

    /**
     * Use Case 31
     * 06.02.01
     * Test for viewing an item's photograph.
     */
    public void testViewItemPhotograph(){
        Item item = new Item("Chapters", 0, 50.00, "chapters gc", true, 1, 0, "");
        item.setPhotos("image");

        assertTrue(item.getPhotos().equals("image"));
    }

    /**
     * Use Case 32
     * 06.03.01
     * Test for removing an item's photograph.
     */
    public void testDeleteItemPhotograph(){
        Item item = new Item("Chapters", 0, 50.00, "chapters gc", true, 1, 0, "image");
        // removing photo means setting photo attributes into empty string.
        item.setPhotos("");

        assertTrue(item.getPhotos().equals(""));
    }

    /**
     * Use Case 33
     * 6.05.01, 10.01.01
     * Test for enabling photograph downloads.
     */
    public void testEnablePhotographDownload(){

    }

    /**
     * Use Case 34
     * 06.05.01, 10.01.01
     * Test for disabling photograph downloads.
     */
    public void DisablePhotographDownload(){

    }

}
