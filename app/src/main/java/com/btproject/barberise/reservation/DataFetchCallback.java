package com.btproject.barberise.reservation;

import com.btproject.barberise.navigation.profile.User;

import java.util.ArrayList;

public interface DataFetchCallback {

    void onDataLoaded(User barberShop);

    void onReservationsLoaded(ArrayList<Reservation> reservations);

}
