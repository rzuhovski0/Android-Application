package com.btproject.barberise.reservation;

public class Subcategory {

    private String name;
    private double price;

    private String description;

    public Subcategory() {
        // Required empty public constructor for Firebase Realtime Database
    }

    public Subcategory(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

}
