package com.btproject.barberise.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserDAO {

    private static FirebaseDatabase database = null;

    private static DatabaseReference dbReference = null;
    private static FirebaseUser user;

    public static FirebaseUser getUser()
    {
        if(user != null)
            return user;

        user = FirebaseAuth.getInstance().getCurrentUser();
        return user;
    }

    private static FirebaseDatabase getDatabaseInstance()
    {
        return FirebaseDatabase.getInstance();
    }

    private static DatabaseReference getDbReferenceInstance()
    {
        if(database == null)
            return FirebaseDatabase.getInstance().getReference();
        else
            return database.getReference();
    }

}
