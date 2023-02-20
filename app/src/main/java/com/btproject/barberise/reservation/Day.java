package com.btproject.barberise.reservation;

public class Day {

    private String date;
    private String time;

    public Day(String date,String time)
    {
        this.date = date;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}