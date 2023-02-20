package com.btproject.barberise.utils;

import com.btproject.barberise.reservation.Day;
import com.btproject.barberise.reservation.Reservation;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            return dateFormat.format(selectedDate.getTime());
        }
        return null;
    }

    public ArrayList<Day> createDays(ArrayList<Reservation> reservations)
    {
        ArrayList<Day> days = new ArrayList<>();

        for(Reservation reservation : reservations)
        {
            String currentDayDate = reservation.getDate();
            String currentDayTime = reservation.getTime();

            Day day = new Day(currentDayDate,currentDayTime);
            days.add(day);
        }

        return days;
    }

    public void compareDaysInfo(ArrayList<Day> days)
    {
        ArrayList<Day> equalDateDays = new ArrayList<>();
        for(Day day : days){
            for(Day otherDay : days) {
                if (day.getDate().equals(otherDay.getDate())) {

                    equalDateDays.add(day);
                    days.remove(day);

                }
            }
        }
    }

}