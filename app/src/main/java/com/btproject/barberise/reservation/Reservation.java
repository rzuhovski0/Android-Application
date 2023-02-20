package com.btproject.barberise.reservation;

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

    private String subcategoryDescription;

    public Reservation()
    {}

    public Reservation(String userName,String serviceProviderName)
    {
        this.userName = userName;
        this.serviceProviderName = serviceProviderName;
    }

    public void setSubcategoryDescription(String subcategoryDescription) {
        this.subcategoryDescription = subcategoryDescription;
    }

    public String getSubcategoryDescription() {
        return subcategoryDescription;
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

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
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