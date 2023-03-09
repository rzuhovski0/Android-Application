package com.btproject.barberise.database;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class clientDAO {
    public static FirebaseUser getAuthUser()
    {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static void clientDataValid(String userId, DataExistsCallback callback) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("clients").child(userId).child("name");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean dataExists = dataSnapshot.exists();
                callback.onDataInvalid(dataExists);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    public interface DataExistsCallback {
        void onDataInvalid(boolean userExists);
    }




}
