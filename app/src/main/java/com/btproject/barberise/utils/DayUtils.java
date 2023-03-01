package com.btproject.barberise.utils;

import android.icu.text.SimpleDateFormat;

import com.btproject.barberise.reservation.Reservation;

import java.util.Date;
import java.util.Locale;

public class DayUtils {

    public static boolean CurrentDayEqualsDayOfReservation(String day, Reservation reservation)
    {
        /**Get current date in dd-MM-yyyy format*/
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());


        String pattern = "EEEE"; // "EEEE" is the pattern for getting the full name of the day of the week
        SimpleDateFormat dateFormatDay = new SimpleDateFormat(pattern, Locale.US);
        String currentDay = dateFormatDay.format(new Date());
        if(currentDay.equals(day) && currentDate.equals(reservation.getDate()))
            return true;
        return false;
    }

}
