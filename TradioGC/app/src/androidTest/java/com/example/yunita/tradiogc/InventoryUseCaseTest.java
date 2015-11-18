package com.example.yunita.tradiogc;

import android.test.ActivityInstrumentationTestCase2;

import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.inventory.Item;

public class InventoryUseCaseTest extends ActivityInstrumentationTestCase2 {

    public InventoryUseCaseTest() {
        super(com.example.yunita.tradiogc.MainActivity.class);
    }

    // 01.01.01
    public void testAddItemToInventory() {
        Inventory inventory = new Inventory();
        Item item = new Item("Chapters", 0, 50.00, "chapters gc", true, 1, 0, "image");
        inventory.add(item);
        assertEquals(inventory.size(), 1);
    }

    // 01.01.01, 01.04.01
    public void testEditInventoryItem() {
        Inventory inventory = new Inventory();
        Item item = new Item("Chapters", 0, 50.00, "chapters gc", true, 1, 0, "image");
        inventory.add(item);

        item.setName("Indigo Chapters");
        assertFalse(item.getName().equals("Chapters"));
    }

    // 01.01.01, 01.05.01
    public void testRemoveInventoryItem() {
        Inventory inventory = new Inventory();
        Item item = new Item("Chapters", 0, 50.00, "chapters gc", true, 1, 0, "image");
        inventory.add(item);
        assertEquals(inventory.size(), 1);

        inventory.remove(item);
        assertEquals(inventory.size(), 0);
    }

    // 01.02.01
    public void testViewInventory() {
        Inventory inventory = new Inventory();
        Item item = new Item("Chapters", 0, 50.00, "chapters gc", true, 1, 0, "image");
        inventory.add(item);
        assertEquals(inventory.size(), 1);

        assertNotNull(inventory);
        assertTrue(inventory.contains(item));
    }

    // 01.03.01
    public void testViewItem() {
        Item item = new Item("Chapters", 0, 50.00, "chapters gc", true, 1, 0, "image");
        assertTrue(item.getName().equals("Chapters"));
        assertTrue(item.getCategory()==0);
        assertTrue(item.getPrice()==50.00);
        assertTrue(item.getDesc().equals("chapters gc"));
        assertTrue(item.getVisibility() == true);
        assertTrue(item.getQuantity() == 1);
        assertTrue(item.getQuality() == 0);
    }

    // 01.03.01, 03.02.01
    public void testSetItemVisibility() {
        Item item = new Item("Chapters", 0, 50.00, "chapters gc", true, 1, 0, "image");
        item.setVisibility(false);

        assertFalse(item.getVisibility() == true);
    }

    // 01.06.01
    public void testSetItemCategory() {
        Item item = new Item("Chapters", 0, 50.00, "chapters gc", true, 1, 0, "image");
        item.setCategory(1); // category starts from [0] - [9]

        assertFalse(item.getCategory() == 0);
    }

}