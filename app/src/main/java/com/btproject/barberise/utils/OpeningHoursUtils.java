package com.btproject.barberise.utils;

import static com.btproject.barberise.utils.CalendarUtils.getFilteredHours;
import static com.btproject.barberise.utils.CalendarUtils.getHoursWithoutPassedHours;
import static com.btproject.barberise.utils.DayUtils.CurrentDayEqualsDayOfReservation;

import com.btproject.barberise.reservation.Day;
import com.btproject.barberise.reservation.Reservation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class OpeningHoursUtils {

    public static ArrayList<String> getAvailableHours(String day, HashMap<String,ArrayList<String>> openingHours,
        Reservation reservation,ArrayList<Day> allDays)
    {
        ArrayList<String> availableHoursForDay = openingHours.get(day);

        /**Filter available hours*/
        ArrayList<String> availableHours = getFilteredHours(availableHoursForDay,allDays,day);

        //TODO implement if currentDay is also "day" of reservation
        //        availableHours = getHoursWithoutPassedHours(availableHours);
        if(CurrentDayEqualsDayOfReservation(day,reservation)) {
            availableHours = getHoursWithoutPassedHours(availableHours);
        }
        assert availableHours != null;

        // Sort hours chronologically
        Collections.sort(availableHours);

        //TODO, it can happen, that availableHours might be completely empty due to all available times have already passed -> Needs fix
        //TODO -> times are already displayed, even when should be unavailable if user press "Choose date" too fast -> Needs callback
        return availableHours;
    }

}
