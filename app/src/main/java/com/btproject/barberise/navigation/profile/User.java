package com.btproject.barberise.navigation.profile;

import android.net.Uri;
//import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class User {
    private String id;
    private String username;
    private String profile_picture;
    private String category;

    public User() {
        // default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String id, String username, String profilePictureUrl, String category) {
        this.id = id;
        this.username = username;
        this.profile_picture = profilePictureUrl;
        this.category = category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_picture() {
        return this.profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

}

