package com.btproject.barberise.users;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.HashMap;

public class Client implements Serializable {

    private String id;
    private String name;
    private String surname;
    private String email;
    @PropertyName("name")
    private String phoneNo;

    public HashMap<String,Object> reservations;

    public HashMap<String, HashMap<String,String>> favorites;

    public Client(){}

    public Client(String id,String name, String surname, String email, String phoneNo,HashMap<String,Object> reservations,HashMap<String, HashMap<String,String>> favorites) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNo = phoneNo;
        this.reservations = reservations;
        this.favorites = favorites;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setReservations(HashMap<String, Object> reservations) {
        this.reservations = reservations;
    }

    public HashMap<String, Object> getReservations() {
        return reservations;
    }

    public HashMap<String, HashMap<String, String>> getFavorites() {
        return favorites;
    }

    public void setFavorites(HashMap<String, HashMap<String, String>> favorites) {
        this.favorites = favorites;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }
}
