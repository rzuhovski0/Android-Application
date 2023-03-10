package com.btproject.barberise;


import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.btproject.barberise.navigation.MenuActivity;
import com.btproject.barberise.navigation.profile.User;
import com.btproject.barberise.splash.DatabaseData;
import com.btproject.barberise.users.Client;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SplashActivity extends AppCompatActivity {

    private DatabaseData dbDataBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        hideSystemUI();

        dbDataBundle = DatabaseData.getInstance();
        DatabaseDataCallback dbCallback = new DatabaseDataCallback() {
            @Override
            public void onClientLoaded() {
                // Start MenuActivity if both client and users have been fetched
                if (dbDataBundle.getClientId() != null && dbDataBundle.getUsers() != null) {
                    Intent intent = new Intent(SplashActivity.this, MenuActivity.class);
                    intent.putExtra("DB_DATA",dbDataBundle);
                    startActivity(intent);
                    finish();//removes current activity from backstack
                }
            }

            @Override
            public void onUsersLoaded() {
                // Start MenuActivity if both client and users have been fetched
                if (dbDataBundle.getClientId() != null && dbDataBundle.getUsers() != null) {
                    Intent intent = new Intent(SplashActivity.this, MenuActivity.class);
                    intent.putExtra("DB_DATA",dbDataBundle);
                    startActivity(intent);
                    finish();//removes current activity from backstack
                }
            }
        };

        //TODO handle if user is barber not client

        /**Check, if current client is logged-in*/
        dbDataBundle.setClientId(getClientId());
        if(dbDataBundle.getClientId() != null) {
            /**Get the current user*/
            getClientFromDatabase(dbDataBundle.getClientId(),dbCallback);
        }else{
            startActivity(new Intent(SplashActivity.this, VerifyPhoneNoActivity.class));
            finish();//removes current activity from backstack
        }

        getUsersFromDatabase(dbCallback);

    }

    private String getClientId()
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null)
            return firebaseUser.getUid();
        return null;
    }

    private void hideSystemUI()
    {
        View decorView = this.getWindow().getDecorView();
        int uiOptions = decorView.getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        newUiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE;
        newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(newUiOptions);
    }

    private void getClientFromDatabase(String currentClientId,DatabaseDataCallback databaseDataCallback)
    {
        DatabaseReference clientRef = FirebaseDatabase.getInstance().getReference("clients").child(currentClientId);
        clientRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getValue() != null) {
                    Client client = snapshot.getValue(Client.class);
                    dbDataBundle.setClient(client);
                }
                databaseDataCallback.onClientLoaded();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error retrieving client data: " + error.getMessage());
            }
        });

    }

    private void getUsersFromDatabase(DatabaseDataCallback callback)
    {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<User> fetchedUsers = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    fetchedUsers.add(user);
                }
                dbDataBundle.setUsers(fetchedUsers);
                callback.onUsersLoaded();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }


    private void splashCountdown()
    {
        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long l) {    //What happens each second
                //no need to code
            }
            @Override
            public void onFinish() {    //What happens after time expires
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if(firebaseUser != null){
                    Intent intent = new Intent(SplashActivity.this,MenuActivity.class);
                    startActivity(intent);
                    finish();   //removes current activity from backstack
                }else{
                    startActivity(new Intent(SplashActivity.this, VerifyPhoneNoActivity.class));
                    finish();   //removes current activity from backstack
                }
            }
        }.start();  //starts countdown
    }
}