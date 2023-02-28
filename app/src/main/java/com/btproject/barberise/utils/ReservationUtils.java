package com.btproject.barberise.utils;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.btproject.barberise.R;
import com.btproject.barberise.reservation.Reservation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

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
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date time = dateFormat.parse(timeString);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);

        Calendar now = Calendar.getInstance();
        now.setTime(new Date());

        // Check if the time is in the past
        return calendar.before(now);
    }



}
