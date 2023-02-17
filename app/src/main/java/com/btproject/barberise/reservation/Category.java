package com.btproject.barberise.reservation;

import java.util.List;

public class Category {

    private String name;
    private List<Subcategory> subcategories;

    public Category() {
        // Required empty public constructor for Firebase Realtime Database
    }

    public Category(String name, List<Subcategory> subcategories) {
        this.name = name;
        this.subcategories = subcategories;
    }

    public String getName() {
        return name;
    }

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

}
