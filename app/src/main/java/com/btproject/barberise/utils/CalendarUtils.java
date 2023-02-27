package com.btproject.barberise.utils;

import static com.btproject.barberise.utils.ReservationUtils.hasTimeAlreadyHappened;

import android.graphics.Color;

import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.btproject.barberise.R;
import com.btproject.barberise.reservation.Day;
import com.btproject.barberise.reservation.Reservation;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class CalendarUtils {

    // Version 1.0 -> Slow
    public static Set<Long> getPreviousDays()
    {
        // Get start date (January 1, 2020)
        Calendar startDate = Calendar.getInstance();

        startDate.set(2022, Calendar.JANUARY, 1);
        Date currentDate = new Date();
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(currentDate);

        // Create Set to hold days
        Set<Long> days = new HashSet<>();

        // Loop through each day and add to Set
        Calendar calendar = (Calendar) startDate.clone();
        while (calendar.before(endDate)) {
            long dayInMillis = calendar.getTimeInMillis();
            days.add(dayInMillis);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return days;
    }

    // Version 1.1 -> NOT WORKING
    public static Set<Long> getPreviousDates()
    {
        // Get start date (January 1, 2020)
        LocalDate startDate = LocalDate.of(2020, 1, 1);
        LocalDate endDate = LocalDate.now();

        // Create Set to hold days
        Set<Long> days = new HashSet<>();

        // Loop through each day and add to Set
        long numDays = ChronoUnit.DAYS.between(startDate, endDate);
        for (int i = 0; i <= numDays; i++) {
            LocalDate currentDate = startDate.plusDays(i);
            days.add(currentDate.toEpochDay());
        }

        return days;
    }

    public static String getDay(Calendar selectedDate)
    {
        Calendar calendar = Calendar.getInstance(); // Create a new Calendar object
        calendar.setTime(selectedDate.getTime()); // Set the time of the Calendar object to the Date object

        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day){
            case 2 :
                return "Monday";
            case 3 :
                return "Tuesday";
            case 4 :
                return "Wednesday";
            case 5 :
                return "Thursday";
            case 6 :
                return "Friday";
            case 7 :
                return "Saturday";
            default:
                return "Sunday";
        }
    }

    public static String getDateInString(Calendar selectedDate)
    {
        if(selectedDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.GERMANY);
            return dateFormat.format(selectedDate.getTime());
        }
        return null;
    }

    public static void setCalendarColors(int color, CalendarView calendarView)
    {
        calendarView.setWeekendDayTextColor(color);
        calendarView.setSelectedDayBackgroundColor(color);
        calendarView.setCurrentDayIconRes(R.drawable.red_dot);
        calendarView.setDayTextColor(Color.BLACK);
    }

    public static void disablePreviousDays(CalendarView calendarView,TreeSet<Long> alreadyReservedDates)
    {
        Set<Long> disabledDays = getPreviousDays();
        disabledDays.addAll(alreadyReservedDates);
        calendarView.setDisabledDays(disabledDays);
    }

    public static ArrayList<Day> getDays(ArrayList<Reservation> reservations, Map<String,ArrayList<String>> openingHours)
    {
        ArrayList<Day> days = new ArrayList<>();

        for(Reservation reservation : reservations){

            String date = reservation.getDate();
            String time = reservation.getTime();
            String dayString = reservation.getDay();
            ArrayList<String> forSize = openingHours.get(dayString);
            int size = forSize.size();

            // check if a Day with the same date has already been created
            boolean existingDay = false;
            for (Day day : days) {
                if (day.getDate().equals(date)) {
                    day.setTime(time);
                    //Added day
                    day.setDay(reservation.getDay());
                    existingDay = true;
                    break;
                }
            }

            // if no existing Day, create a new one and fill with null values
            if (!existingDay) {
                Day day = new Day(date,size,reservation.getTimeInMilliseconds());
                day.setTime(time);
                //Added day
                day.setDay(reservation.getDay());
                days.add(day);
            }
        }
        return days;
    }

    public static TreeSet<Long> getDisabledDates(ArrayList<Day> unavailableDays)
    {
        TreeSet<Long> disabledDates = new TreeSet<>();

        for(Day day : unavailableDays)
        {
            if(!day.isAvailable())
                disabledDates.add(day.getSelectedDate());
        }

        return disabledDates;

    }

    public static ArrayList<String> getFilteredHours(ArrayList<String> allAvailableHours, ArrayList<Day> days,String dayString)
    {
        for(Day day : days)
        {
            if(day.isAvailable() && day.getDay().equals(dayString)) {
                // iterate through both arrays
                for (int i = 0; i < allAvailableHours.size(); i++) {
                    String s1 = allAvailableHours.get(i);
                    for (int j = 0; j < day.getTimes().length; j++) {
                        String s2 = day.getTimes()[j];

                        /**Filter those time, that has already passed*/
                        removeTimeIfAlreadyHappened(s2,allAvailableHours,i);

                        /**Remove those times, that are in days(already stored reservations)
                         * and availableHours (available times for the day)*/
                        if (s1.equals(s2)) {
                            allAvailableHours.remove(i);
                            i--; // decrement i to account for the removed element
                            break; // break out of the inner loop since the element was removed
                        }
                    }
                }
            }
        }
        return allAvailableHours;
    }

    private static void removeTimeIfAlreadyHappened(String s2, ArrayList<String> allAvailableHours, int i)
    {
        try{
            if(hasTimeAlreadyHappened(s2))
                allAvailableHours.remove(i);
        }catch (Exception ignored){

        }
    }


}