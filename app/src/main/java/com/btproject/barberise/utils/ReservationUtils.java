package com.btproject.barberise.utils;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.btproject.barberise.R;
import com.btproject.barberise.reservation.DataFetchCallback;
import com.btproject.barberise.reservation.Reservation;
import com.btproject.barberise.reservation.ReservationSuccessfulActivity;
import com.btproject.barberise.reservation.ReservationTestingActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class ReservationUtils {

    public static void selectCorrectTextColor(int checkedId, RadioGroup radioGroup)
    {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            if (radioButton.getId() == checkedId) {

                //checking if radioGroup is for Time or not
                if(radioGroup.getOrientation() == LinearLayout.HORIZONTAL)
                    radioButton.setTextColor(Color.GRAY);
                else
                    radioButton.setTextColor(Color.WHITE);

            } else {
                radioButton.setTextColor(Color.BLACK);
            }
        }
    }

    public static void getReservationsFromDatabase(DataFetchCallback callback,
                                             String barberShopId,ArrayList<Reservation> reservationsArray)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference().child("users").child(barberShopId).child("reservations");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot reservationSnapshot : snapshot.getChildren()) {
                    Reservation reservation = reservationSnapshot.getValue(Reservation.class);
                    reservationsArray.add(reservation);
                }
                callback.onReservationsLoaded(reservationsArray);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });
    }

    /**Sort reservations based on their timeInMilliseconds*/
    public static void sortReservations(ArrayList<Reservation> reservations) {
        // Define a custom Comparator for Reservation objects based on the timeInMilliseconds attribute
        Comparator<Reservation> comparator = new Comparator<Reservation>() {
            @Override
            public int compare(Reservation r1, Reservation r2) {
                // Check if either reservation has passed
                boolean r1Passed = false;
                try {
                    r1Passed = r1.hasPassed();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                boolean r2Passed = false;
                try {
                    r2Passed = r2.hasPassed();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                // If only one reservation has passed, the non-passed reservation should come first
                if (r1Passed != r2Passed) {
                    return r1Passed ? 1 : -1;
                }

                // If both reservations have not passed or both have passed, sort based on timeInMilliseconds
                return Long.compare(r1.getTimeInMilliseconds(), r2.getTimeInMilliseconds());
            }
        };

        // Sort the reservations ArrayList using the custom Comparator
        Collections.sort(reservations, comparator);
    }


    public static boolean hasTimeAlreadyHappened(@NonNull String timeString) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.GERMANY);
        Date time = dateFormat.parse(timeString);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);

        Calendar now = Calendar.getInstance();

        // Set the year, month, and day to the current date
        now.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        now.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        now.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));

        // Check if the time is in the past
        return now.after(calendar);
    }




}
