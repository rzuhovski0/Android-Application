package com.btproject.barberise.reservation;

import static com.btproject.barberise.utils.ReservationUtils.hasTimeAlreadyHappened;

import android.icu.text.DateFormatSymbols;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Reservation {

    private String id;

    private String userName;

    private String serviceProviderName;

    private String categoryName;

    private String subcategoryName;

    private String subcategoryDescription;

    private Subcategory subcategory;

    private String time;

    private String date;

    private long timeInMilliseconds;

    private String day;

    private double price;

    private String serviceProviderId;

    private String profilePicture;

    private String clientPhoneNo;

    public Reservation()
    {}

    public Reservation(String userName,String serviceProviderName)
    {
        this.userName = userName;
        this.serviceProviderName = serviceProviderName;
    }

    public boolean hasPassed() throws ParseException {
        long currentTime = System.currentTimeMillis();
        /**We check if the time has already passed as well as date*/
        return timeInMilliseconds < currentTime;
    }

    public String getClientPhoneNo() {
        return clientPhoneNo;
    }

    public void setClientPhoneNo(String clientPhoneNo) {
        this.clientPhoneNo = clientPhoneNo;
    }

    public Subcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }

    public String getId() {
        return id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

    public void setServiceProviderId(String serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getServiceProviderId() {
        return serviceProviderId;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String formatDateAndTime() throws ParseException {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDateFormat.parse(date + " " + time));

        SimpleDateFormat outputDateFormat = new SimpleDateFormat("d.MMM, yyyy HH:mm", Locale.US);
        String formattedDateTime = outputDateFormat.format(calendar.getTime());

        //formattedDateTime = formattedDateTime.replace(".", new DateFormatSymbols(Locale.US).getShortMonths()[calendar.get(Calendar.MONTH)]);

        String monthAbbreviation = new DateFormatSymbols(Locale.US).getShortMonths()[calendar.get(Calendar.MONTH)];
        formattedDateTime = formattedDateTime.replaceFirst("\\.\\d+", "." + monthAbbreviation);

        return formattedDateTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    /**Get price in string*/
    public String getPriceString()
    {
        return Double.toString(getPrice()) + " €";
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setSubcategoryDescription(String subcategoryDescription) {
        this.subcategoryDescription = subcategoryDescription;
    }

    public String getSubcategoryDescription() {
        return subcategoryDescription;
    }

    public void setTimeInMilliseconds(long timeInMilliseconds) {
        this.timeInMilliseconds = timeInMilliseconds;
    }

    public void setTimeInMillisecondsByDate(String date)
    {
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy", Locale.GERMANY);
        try{
            Date mDate1 = sdf1.parse(date);
            this.timeInMilliseconds = mDate1.getTime();
        } catch (ParseException e) {
            Log.e("DateException", e.getMessage());
        }
        System.out.println(timeInMilliseconds);
    }

    public long getTimeInMilliseconds() {
        return timeInMilliseconds;
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
                && this.getSubcategoryName() != null;/* && this.getSelectedDate() != null;*/
    }
}