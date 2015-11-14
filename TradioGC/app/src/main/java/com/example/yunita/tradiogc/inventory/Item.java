package com.example.yunita.tradiogc.inventory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;


public class Item implements Serializable {
    private String name;
    private int category;
    private double price;
    private String desc;
    private Boolean visibility;
    private int quantity;
    private int quality;

    /**
     * Class constructor.
     */
    public Item() {

    }

    /**
     * Class constructor specifying the details of the object.
     *
     * @param name       contains the name of the item
     * @param category   contains an integer that represents an index of the Categories array
     * @param price      contains a double for the price of the item
     * @param desc       contains the description/comment of the item
     * @param visibility determines whether the item is shared publicly or privately
     * @param quantity   contains an integer 1..* for the amount that the owner has of the item
     * @param quality    contains an integer that describes the item's quality; 0 is new and 1 is used
     */
    public Item(String name, int category, double price, String desc, Boolean visibility, int quantity, int quality) {
        this.category = category;
        this.name = name;
        this.price = price;
        this.desc = desc;
        this.visibility = visibility;
        this.quality = quality;
        this.quantity = quantity;
    }

    /**
     * Gets the name of this item.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the name of this item.
     *
     * @param name this item's new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the category of this item.
     * Category is specified by an integer from 0 - 9.
     *
     * @return category
     */
    public int getCategory() {
        return category;
    }

    /**
     * Changes the category of this item.
     * Category is specified by an integer from 0 - 9.
     *
     * @param category this item's new category
     */
    public void setCategory(int category) {
        this.category = category;
    }

<<<<<<< HEAD
    public String getPrice() {
        return Double.toString(round(price, 3));
    }

    //TODO: fix the price to print in two decimal points

=======
    /**
     * Gets the price of this item.
     *
     * @return price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Changes the price of this item.
     *
     * @param price this item's new price
     */
>>>>>>> origin/yunita3
    public void setPrice(double price) {
        this.price = round(price, 2);
    }

    /**
     * Gets the description of this item.
     *
     * @return description
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Changes the description of this item.
     *
     * @param desc this item's new description
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Gets the visibility of this item.
     *
     * @return visibility
     */
    public Boolean getVisibility() {
        return visibility;
    }

    /**
     * Changes the visibility of this item.
     *
     * @param visibility this item's new visibility
     */
    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    /**
     * Gets the quantity of this item.
     *
     * @return quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Changes the quantity of this item.
     *
     * @param quantity this item's new quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the quality of this item.
     * Quality is specified by an integer: 0 (new) or 1 (used).
     *
     * @return quality
     */
    public int getQuality() {
        return quality;
    }

    /**
     * Changes the quality of this item.
     * Quality is specified by an integer: 0 (new) or 1 (used).
     *
     * @param quality this item's new quality
     */
    public void setQuality(int quality) {
        this.quality = quality;
    }

    /**
     * Return the new printing format of the item.
     * <p>The new format of item is [name]\n[price].
     *
     * @return String:  item's name with its price in a new line
     */
    @Override
    public String toString() {
        return this.name + "\n$" + round(this.price, 2);
    }

    //http://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
