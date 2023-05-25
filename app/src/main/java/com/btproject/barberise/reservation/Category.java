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

    public void setSubcategories(ArrayList<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }

    public void addSubcategory(Subcategory subcategory)
    {
        if(this.subcategories != null)
            this.subcategories.add(subcategory);
        else{
            this.subcategories = new ArrayList<>();
            this.subcategories.add(subcategory);
        }
    }

    public String getName() {
        return name;
    }

    public ArrayList<Subcategory> getSubcategories() {
        return subcategories;
    }

}
