package com.btproject.barberise.users;

import java.util.HashMap;

public class PseudoUser {

    private String name;
    private String profilePicture;
    private HashMap<Integer,Integer> ratingsMap;

    public PseudoUser()
    {

    }

    public String getName() {
        return name;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public HashMap<Integer, Integer> getRatingsMap() {
        return ratingsMap;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setRatingsMap(HashMap<Integer, Integer> ratingsMap) {
        this.ratingsMap = ratingsMap;
    }
}
