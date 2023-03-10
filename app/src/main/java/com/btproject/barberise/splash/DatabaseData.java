package com.btproject.barberise.splash;

import com.btproject.barberise.navigation.profile.User;
import com.btproject.barberise.users.Client;

import java.io.Serializable;
import java.util.ArrayList;

public class DatabaseData implements Serializable {

    private static DatabaseData instance;
    private DatabaseData() {}

    public static DatabaseData getInstance() {
        if (instance == null) {
            instance = new DatabaseData();
        }
        return instance;
    }

    // add your methods and properties here
    public String clientId;
    public Client client;

    public User user;

    public ArrayList<User> users;

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public String getClientId() {
        return clientId;
    }

    public Client getClient() {
        return client;
    }

    public User getUser() {
        return user;
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}
