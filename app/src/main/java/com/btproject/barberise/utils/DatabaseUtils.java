package com.btproject.barberise.utils;

import android.content.Context;
import android.widget.Toast;

import com.btproject.barberise.navigation.profile.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DatabaseUtils {

    public static FirebaseUser getCurrentUser()
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Get a reference to the current user
        return mAuth.getCurrentUser();
    }

    public static DatabaseReference getFavoritesDbReference(FirebaseUser currentUser)
    {
        return FirebaseDatabase.getInstance().getReference()
                .child("users").child(currentUser.getUid()).child("favorites");
    }

    public static void removeUserFromFavorites(String barberShopId)
    {
        FirebaseUser currentUser = getCurrentUser();
        DatabaseReference favDatabaseReference = getFavoritesDbReference(currentUser).child(barberShopId);
        favDatabaseReference.removeValue();
    }

    public static void addUserToFavorites(User barberShop, String barberShopId, Context context)
    {

        String userName = barberShop.getUsername();
        String imageUrl = barberShop.getProfile_picture();


        // Get a reference to the current user
        FirebaseUser currentUser = getCurrentUser();

        // Check if the user is authenticated
        if (currentUser != null) {

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("favorites");


            // Create a Map object with the values you want to store
            Map<String, Object> favoriteMap = new HashMap<>();
            favoriteMap.put("barberShopId", barberShopId);
            favoriteMap.put("userName", userName);
            favoriteMap.put("imageUrl", imageUrl);

            // Add the new barber shop ID to the list of favorites
            databaseReference.child(barberShopId).setValue(favoriteMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Barber shop was successfully added to favorites
                    Toast.makeText(context, "Barber shop added to favorites successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Failed to add barber shop to favorites
                    Toast.makeText(context, "Failed to add barber shop to favorites: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // User is not authenticated, so we cannot save the reservation
            Toast.makeText(context, "You must be logged in to add a barber shop to favorites", Toast.LENGTH_SHORT).show();
        }
    }



}
