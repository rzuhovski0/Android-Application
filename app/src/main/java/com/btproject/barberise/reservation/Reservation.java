package com.btproject.barberise.reservation;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.Calendar;

public class Reservation {

    private String userName;

    private String serviceProviderName;

    private Category category;

    private Subcategory subcategory;

    public Reservation(String userName,String serviceProviderName)
    {
        this.userName = userName;
        this.serviceProviderName = serviceProviderName;
    }

    public Category getCategory() {
        return category;
    }

    public Subcategory getSubcategory() {
        return subcategory;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }
}
