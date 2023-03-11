package com.btproject.barberise.navigation.profile;

//import android.support.annotation.NonNull;

import com.btproject.barberise.reservation.Category;

        import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private String id;
    private String username;
    private String profile_picture;
    private String category;

    /**Data lists*/
    public ArrayList<Category> categories = new ArrayList<>();
    public HashMap<String,ArrayList<String>> openingHours = new HashMap<>();

    public HashMap<String,Object> reservations = new HashMap<>();
    public HashMap<String, HashMap<String,String>> favorites = new HashMap<>();

    private int customPriority;

    public User() {
        // default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    /**Constructor with HashMap reservations*/
    public User(String id, String username, String profilePictureUrl, String category,
                ArrayList<Category> categories,HashMap<String,ArrayList<String>> opening_hours,HashMap<String,Object> reservations) {
        this.id = id;
        this.username = username;
        this.profile_picture = profilePictureUrl;
        this.category = category;
        this.categories = categories;
        this.openingHours = opening_hours;
        this.reservations = reservations;
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

