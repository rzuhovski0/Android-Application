package com.btproject.barberise.navigation.profile;

import android.net.Uri;
//import android.support.annotation.NonNull;

import com.btproject.barberise.reservation.Category;
import com.btproject.barberise.reservation.Reservation;
import com.btproject.barberise.reservation.Subcategory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String id;
    private String username;
    private String profile_picture;
    private String category;

    /**Data lists*/
    public ArrayList<Category> categories = new ArrayList<>();
    public Map<String,ArrayList<String>> opening_hours  = new HashMap<>();;
    public ArrayList<Reservation> reservations  = new ArrayList<>();

    public User() {
        // default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String id, String username, String profilePictureUrl, String category,
                ArrayList<Category> categories,Map<String,ArrayList<String>> opening_hours,ArrayList<Reservation> reservations) {
        this.id = id;
        this.username = username;
        this.profile_picture = profilePictureUrl;
        this.category = category;
        this.categories = categories;
        this.opening_hours = opening_hours;
        this.reservations = reservations;
    }


    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public void setOpeningHours(Map<String, ArrayList<String>> openingHours) {
        this.opening_hours = openingHours;
    }

    public void setReservations(ArrayList<Reservation> reservations) {
        this.reservations = reservations;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public Map<String, ArrayList<String>> getOpeningHours() {
        return opening_hours;
    }

    public ArrayList<Reservation> getReservations() {
        return reservations;
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

