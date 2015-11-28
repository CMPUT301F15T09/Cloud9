package com.example.yunita.tradiogc.inventory;

import android.test.ActivityInstrumentationTestCase2;

import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.inventory.Item;

public class InventoryTestCase extends ActivityInstrumentationTestCase2 {

    public InventoryTestCase() {
        super(com.example.yunita.tradiogc.MainActivity.class);
    }

    /**
     * Use Case 3
     * 01.01.01
     * Test for adding an item to the user's inventory.
     */
    public void testAddItem() {
        Inventory inventory = new Inventory();
        Item item = new Item("Chapters", 0, 50.00, "chapters gc", true, 1, 0);
        inventory.add(item);
        assertEquals(inventory.size(), 1);
    }

    /**
     * Use Case 4
     * 01.01.01, 01.04.01
     * Test for editing an item in the user's inventory.
     */
    public void testEditItem() {
        Inventory inventory = new Inventory();
        Item item = new Item("Chapters", 0, 50.00, "chapters gc", true, 1, 0);
        inventory.add(item);

        item.setName("Indigo Chapters");
        assertFalse(item.getName().equals("Chapters"));
    }

    /**
     * Use Case 5
     * 01.01.01, 01.05.01
     * Test for removing an item from the user's inventory.
     */
    public void testRemoveItem() {
        Inventory inventory = new Inventory();
        Item item = new Item("Chapters", 0, 50.00, "chapters gc", true, 1, 0);
        inventory.add(item);
        assertEquals(inventory.size(), 1);

        inventory.remove(item);
        assertEquals(inventory.size(), 0);
    }

    /**
     * Use Case 6
     * 01.02.01
     * Test for viewing a user's inventory.
     */
    public void testViewInventory() {
        Inventory inventory = new Inventory();
        Item item = new Item("Chapters", 0, 50.00, "chapters gc", true, 1, 0);
        inventory.add(item);
        assertEquals(inventory.size(), 1);

        assertNotNull(inventory);
        assertTrue(inventory.contains(item));
    }

    /**
     * Use Case 7
     * 01.03.01
     * Test for viewing an item's detail page.
     */
    public void testViewItem() {
        Item item = new Item("Chapters", 0, 50.00, "chapters gc", true, 1, 0);
        assertTrue(item.getName().equals("Chapters"));
        assertTrue(item.getCategory()==0);
        assertTrue(item.getPrice()==50.00);
        assertTrue(item.getDesc().equals("chapters gc"));
        assertTrue(item.getVisibility() == true);
        assertTrue(item.getQuantity() == 1);
        assertTrue(item.getQuality() == 0);
    }

    /**
     * Use Case 8
     * 01.03.01, 03.02.01
     * Test for setting an item's visibility.
     */
    public void testSetItemVisibility() {
        Item item = new Item("Chapters", 0, 50.00, "chapters gc", true, 1, 0);
        item.setVisibility(false);

        assertFalse(item.getVisibility() == true);
    }

    /**
     * Use Case 9
     * 01.06.01
     * Test for setting an item's category.
     */
    public void testSetItemCategory() {
        Item item = new Item("Chapters", 0, 50.00, "chapters gc", true, 1, 0);
        item.setCategory(1); // category starts from [0] - [9]

        assertFalse(item.getCategory() == 0);
    }

    // NEW REQUIREMENTS

    /**
     * Use Case 42
     * 13.01.01
     * Test for cloning item.
     */
    public void testCloneItem(){
        Item john_item = new Item("Chapters", 0, 50.00, "chapters gc", true, 1, 0);
        // when cloning item, ann can edit the item before it is added to her inventory.
        Item ann_item = new Item();
        ann_item.setName(john_item.getName());
        ann_item.setCategory(1);
        ann_item.setPrice(john_item.getPrice());
        ann_item.setDesc(john_item.getDesc());
        ann_item.setVisibility(john_item.getVisibility());
        ann_item.setQuality(john_item.getQuality());

        assertFalse(ann_item.getCategory() == john_item.getCategory());

    }


}