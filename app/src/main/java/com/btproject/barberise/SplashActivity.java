package com.btproject.barberise;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.btproject.barberise.navigation.MenuActivity;
import com.btproject.barberise.navigation.profile.ConfigureServicesActivity;
import com.btproject.barberise.navigation.profile.PartnerProfileActivity;
import com.btproject.barberise.navigation.profile.PartnerSignedInActivity;
import com.btproject.barberise.navigation.profile.RegistrationActivity;
import com.btproject.barberise.navigation.profile.SetUpServicesActivity;
import com.btproject.barberise.navigation.profile.SetUpServicesSecondActivity;
import com.btproject.barberise.utils.CalendarUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Set;


public class SplashActivity extends AppCompatActivity {

    private long[] previousDaysArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Objects.requireNonNull(getSupportActionBar()).hide();   //hide action bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        hideSystemUI();
//        Set<Long> previousDays = CalendarUtils.getPreviousDays();
//        previousDaysArray = getConvertedPreviousDays(previousDays);
        splashCountdown(previousDaysArray);
    }

    public long[] getConvertedPreviousDays(Set<Long> previousDays)
    {
        // Convert the set of Long values to an array of long
        long[] previousDaysArray = new long[previousDays.size()];
        int i = 0;
        for (Long day : previousDays) {
            previousDaysArray[i++] = day;
        }
        return previousDaysArray;
    }

    private void splashCountdown(long[] previousDaysArray)
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
                    String userId = firebaseUser.getUid();
                    handleUsersAccordingly(userId);
                }else{
                    startActivity(new Intent(SplashActivity.this, VerifyPhoneNoActivity.class));
                    finish();   //removes current activity from backstack
                }
            }
        }.start();  //starts countdown
    }

    private void handleUsersAccordingly(String userId)
    {
        DatabaseReference clientsRef = FirebaseDatabase.getInstance().getReference("clients");
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        clientsRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User is a client
                    Intent intent = new Intent(SplashActivity.this,MenuActivity.class);
                    startActivity(intent);
                } else {
                    usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Intent intent = new Intent(SplashActivity.this, PartnerSignedInActivity.class);
                                startActivity(intent);
                            } else {
                                // User doesn't exist in either location
                                Toast.makeText(getApplicationContext(),"Ups, something went wrong..",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle any errors that occurred while querying the database
                            Toast.makeText(getApplicationContext(),"Ups, something went wrong..",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occurred while querying the database
                Toast.makeText(getApplicationContext(),"Ups, something went wrong..",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void hideSystemUI() {
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

}