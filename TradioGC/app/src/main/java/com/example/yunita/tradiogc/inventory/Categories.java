package com.example.yunita.tradiogc.inventory;

import java.util.ArrayList;

/**
 * This class defines an array of ten categories that the item can be placed in.
 */
public class Categories {
    private ArrayList<String> categories = new ArrayList<>();

    /**
     * Class constructor specifying the categories available to choose from.
     */
    public Categories() {
        categories.add("Books");
        categories.add("Electronics");
        categories.add("Sports");
        categories.add("Music");
        categories.add("Clothing");
        categories.add("Hobby");
        categories.add("Food");
        categories.add("Grocery");
        categories.add("Specialty");
        categories.add("Other");
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

}
