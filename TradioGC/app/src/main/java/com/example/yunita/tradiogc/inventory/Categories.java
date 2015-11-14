package com.example.yunita.tradiogc.inventory;

import java.util.ArrayList;

public class Categories {
    private ArrayList<String> categories = new ArrayList<>();

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
