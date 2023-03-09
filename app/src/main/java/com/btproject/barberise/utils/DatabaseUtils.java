package com.btproject.barberise.utils;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.btproject.barberise.navigation.profile.User;
import com.btproject.barberise.reservation.Category;
import com.btproject.barberise.reservation.DataFetchCallback;
import com.btproject.barberise.reservation.Reservation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
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
                .child("clients").child(currentUser.getUid()).child("favorites");
    }

    public static void removeUserFromFavorites(String barberShopId)
    {
        FirebaseUser currentUser = getCurrentUser();
        DatabaseReference favDatabaseReference = getFavoritesDbReference(currentUser).child(barberShopId);
        favDatabaseReference.removeValue();
    }

    public static void removeReservation(String reservationId,String barberShopId)
    {
        FirebaseUser currentUser = getCurrentUser();

        /**Remove reservation from user db*/
        if(currentUser != null) {
            DatabaseReference resRef = FirebaseDatabase.getInstance().getReference().child("clients")
                    .child(currentUser.getUid()).child("reservations").child(reservationId);
            resRef.removeValue();
        }

        /**Remove reservation from barber db*/
        DatabaseReference resRef = FirebaseDatabase.getInstance().getReference().child("users")
                .child(barberShopId).child("reservations").child(reservationId);
        resRef.removeValue();
    }

    public static void addUserToFavorites(User barberShop, String barberShopId, Context context)
    {

        String userName = barberShop.getUsername();
        String imageUrl = barberShop.getProfile_picture();


        // Get a reference to the current user
        FirebaseUser currentUser = getCurrentUser();

        // Check if the user is authenticated
        if (currentUser != null) {

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("clients").child(currentUser.getUid()).child("favorites");


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

    public static void getReservationsRealTime(ArrayList<Reservation> reservations, DataFetchCallback callback) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference resRef = database.getReference().child("clients").child(currentUserId).child("reservations");

            resRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Reservation reservation = snapshot.getValue(Reservation.class);
                    reservations.add(reservation);
                    callback.onReservationsLoaded(reservations);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Reservation updatedReservation = snapshot.getValue(Reservation.class);

                    // Find the index of the updated reservation in the list
                    String reservationKey = snapshot.getKey();
                    int index = -1;
                    for (int i = 0; i < reservations.size(); i++) {
                        Reservation reservation = reservations.get(i);
                        if (reservation.getId().equals(reservationKey)) {
                            index = i;
                            break;
                        }
                    }

                    // Update the reservation in the list and notify the adapter of the change
                    if (index != -1) {
                        reservations.set(index, updatedReservation);
                        callback.onReservationsLoaded(reservations);
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    String reservationKey = snapshot.getKey();

                    // Find the index of the removed reservation in the list
                    int index = -1;
                    for (int i = 0; i < reservations.size(); i++) {
                        Reservation reservation = reservations.get(i);
                        if (reservation.getId().equals(reservationKey)) {
                            index = i;
                            break;
                        }
                    }

                    // Remove the reservation from the list and notify the adapter of the change
                    if (index != -1) {
                        reservations.remove(index);
                        callback.onReservationsLoaded(reservations);
                    }
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    // Handle moved reservation
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }
    }

    /**Test methods*/
    public static void addAttributesToUsers(Map<String,ArrayList<String>> openingHours,ArrayList<Category> categories)
    {
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersReference = dbReference.child("users");

        ValueEventListener usersListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Add the attributes to each user node
                    userSnapshot.getRef().child("categories").setValue(categories);
                    userSnapshot.getRef().child("opening_hours").setValue(openingHours);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadUsers:onCancelled", databaseError.toException());
            }
        };
        usersReference.addListenerForSingleValueEvent(usersListener);
    }

    /**Reservation Activity*/


}
