package com.btproject.barberise.reservation;

import com.google.android.material.imageview.ShapeableImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Reservation {

    private String id;

    private String userName;

    private String serviceProviderName;

    private String categoryName;

    private String subcategoryName;

    private String time;

    private String date;

    private Calendar selectedDate;

    public Reservation()
    {}

    public Reservation(String userName,String serviceProviderName)
    {
        this.userName = userName;
        this.serviceProviderName = serviceProviderName;
    }

    public Calendar getSelectedDate() {
        return selectedDate;
    }
    public void setSelectedDate(Calendar selectedDate) {
        this.selectedDate = selectedDate;
    }
    public String getUserName() {
        return userName;
    }
    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public void setCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setSubcategory(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {

        return date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean reservationValid()
    {
        return this.getTime() != null && this.getDate() != null && this.getUserName() != null
                && this.getServiceProviderName() != null && this.getCategoryName() != null
                && this.getSubcategoryName() != null && this.getSelectedDate() != null;
    }
}