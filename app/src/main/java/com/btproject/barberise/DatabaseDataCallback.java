package com.btproject.barberise;

import android.graphics.Bitmap;

import com.btproject.barberise.reservation.Reservation;

import java.util.ArrayList;

public interface MyCallback {

    void onProfileDataLoaded(String imageURL, String username, Bitmap imageBitmap);

    void onCategoriesLoaded(ArrayList<String> categoryName);

    void onHoursLoaded();

    void onReservationsLoaded(ArrayList<Reservation> reservationsList);

}