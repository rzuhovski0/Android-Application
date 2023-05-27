package com.btproject.barberise.navigation.profile;

//import android.support.annotation.NonNull;

import com.btproject.barberise.reservation.Category;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class User {

    private String id;
    private String username;
    private String profile_picture;
    private String category;
    private String address;
    private String phoneNo;

    private String email;

    /**Data lists*/
    public ArrayList<Category> categories = new ArrayList<>();

    public HashMap<String,ArrayList<String>> openingHours = new HashMap<>();

    public HashMap<String,Object> reservations = new HashMap<>();
    public HashMap<String, HashMap<String,String>> favorites = new HashMap<>();
    private ArrayList<Integer> ratings = new ArrayList<>();
    private int customPriority;

    public User() {
        // default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    /**Constructor with HashMap reservations*/
    public User(String id, String username, String profilePictureUrl, String category,
                ArrayList<Category> categories,HashMap<String,ArrayList<String>> opening_hours,
                HashMap<String,Object> reservations, ArrayList<Integer> ratings) {
        this.id = id;
        this.username = username;
        this.profile_picture = profilePictureUrl;
        this.category = category;
        this.categories = categories;
        this.openingHours = opening_hours;
        this.reservations = reservations;
        this.ratings = ratings;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return this.email;
    }

    public void setPhoneNo(String phoneNo)
    {
        this.phoneNo = phoneNo;
    }

    public String getPhoneNo()
    {
        return this.phoneNo;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return this.address;
    }

    // This function is for User Adapter
    public float getOverallRating()
    {

        if(getRatings().isEmpty())
            return 0.0f;

        int overallRating = 0;
        int numOfRatings = 0;
        for (Integer rating : getRatings()) {
            overallRating += rating;
            numOfRatings++;
        }

        return (float) (overallRating / numOfRatings);
    }

    public ArrayList<Integer> getRatings() {
        return ratings;
    }

    public void setRatings(ArrayList<Integer> ratings) {
        this.ratings = ratings;
    }

    public void setCustomPriority(int customPriority) {
        this.customPriority = customPriority;
    }

    public int getCustomPriority() {
        return customPriority;
    }

    public HashMap<String, HashMap<String,String>> getFavorites() {
        return favorites;
    }

    public void setFavorites(HashMap<String, HashMap<String,String>> favorites) {
        this.favorites = favorites;
    }

    public void setReservations(HashMap<String,Object> reservations) {
        this.reservations = reservations;
    }

    public HashMap<String,Object> getReservations() {
        return reservations;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public void setOpeningHours(HashMap<String, ArrayList<String>> openingHours) {
        this.openingHours = openingHours;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public HashMap<String, ArrayList<String>> getOpeningHours() {
        return openingHours;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_picture() {
        return this.profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

}

