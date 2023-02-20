package com.btproject.barberise.reservation;

import java.util.ArrayList;
import java.util.List;

public class Category extends Options{
    public ArrayList<Subcategory> subcategories;

    public Category() {
        // Required empty public constructor for Firebase Realtime Database
    }

    public Category(String name, ArrayList<Subcategory> subcategories) {
        super(name);
        this.subcategories = subcategories;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Subcategory> getSubcategories() {
        return subcategories;
    }

}
