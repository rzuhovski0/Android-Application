package com.btproject.barberise.reservation;

import static com.btproject.barberise.utils.ReservationUtils.hasTimeAlreadyHappened;

public class Day {

    private String date;
    private String[] times;
    private long selectedDate;
    private int counter;

    //For filtering availableHours
    private String day;

    public Day(String date,int size,long selectedDate) {
        this.date = date;
        times = new String[size];
        this.selectedDate = selectedDate;
        counter = 0;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public long getSelectedDate() {
        return selectedDate;
    }

    public String getDate() {
        return date;
    }

    public String[] getTimes() {
        return times;
    }

    public void setTime(String time)
    {
        this.times[counter] = time;
        counter++;
    }

    public String getDay()
    {
        return this.day;
    }

    public void printTimes()
    {
        for(String time : times)
        {
            System.out.println(time);
        }
    }

    public boolean isAvailable()
    {
        for(String time : times){
            if(time == null)
                return true;
        }
        return false;
    }
}