package com.btproject.barberise.navigation;

import static com.btproject.barberise.database.clientDAO.clientDataValid;
import static com.btproject.barberise.database.clientDAO.getAuthUser;
import static com.btproject.barberise.utils.DatabaseUtils.getReservationsRealTime;
import static com.btproject.barberise.utils.ReservationUtils.sortReservations;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.btproject.barberise.R;
import com.btproject.barberise.database.clientDAO;
import com.btproject.barberise.databinding.ActivityMenuBinding;
import com.btproject.barberise.navigation.profile.User;
import com.btproject.barberise.registration.NewUserActivity;
import com.btproject.barberise.reservation.DataFetchCallback;
import com.btproject.barberise.reservation.Reservation;
import com.btproject.barberise.utils.CalendarUtils;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MenuActivity extends AppCompatActivity {

    ActivityMenuBinding binding;
    FirebaseDatabase database;
    DatabaseReference reference;
    public static Set<Long> previousDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        boolean openReservations = getIntent().getBooleanExtra("openReservations", false);
        boolean REGISTER_FLAG = getIntent().getBooleanExtra("REGISTER_FLAG", false);
        boolean BARBER_FLAG = getIntent().getBooleanExtra("BARBER_FLAG",false);

//        // Retrieve the array of long values from the intent
//        long[] previousDaysArray = getIntent().getLongArrayExtra("previousDays");
//
//        // Create a new HashSet and add the long values from the array
//        previousDays = new HashSet<>();
//        if (previousDaysArray != null) {
//            for (long day : previousDaysArray) {
//                previousDays.add(day);
//            }
//        }
        if(BARBER_FLAG){
            replaceFragment(new ProfileFragment());
        } else {
            replaceFragment(new HomeFragment());
        }

        /**If opened from VerifyPhoneNumberActivity*/
        if (REGISTER_FLAG) {

            // Firstly, get the user who signed-in
            FirebaseUser currentUser = getAuthUser();

            if (currentUser == null)
                return;

            // Get the phone number
            String phoneNo = getIntent().getStringExtra("PHONE_NUMBER");

            // Get the user id
            String currentUserId = currentUser.getUid();

            // Check whether the user already has any name, surname, email
            clientDAO.DataExistsCallback dataExistsCallback = dataExists -> {
                if (dataExists)
                    replaceFragment(new HomeFragment());
                else {
                    launchNewUserActivity(currentUserId, phoneNo);
                }
            };
            // Call the check method
            clientDataValid(currentUserId, dataExistsCallback);
        } else {
            replaceFragment(new HomeFragment());
        }

        /**If opened from ReservationSuccessfulActivity*/
        if (openReservations) {
            replaceFragment(new CalendarFragment());
        } else {
            replaceFragment(new HomeFragment());
        }

        binding.bottomNavigationView2.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.favorite:
                    replaceFragment(new FavoriteFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    break;
                case R.id.calendar:
                    replaceFragment(new CalendarFragment());
                    break;
            }
            return true;
        });
    }



    public static Set<Long> getPreviousDays()
    {
        if(previousDays == null)
            return CalendarUtils.getPreviousDays();
        else
            return previousDays;
    }


    private void launchNewUserActivity(String currentUserId,String phoneNo)
    {
        Intent intent = new Intent(MenuActivity.this, NewUserActivity.class);
        intent.putExtra("USER_ID", currentUserId);
        intent.putExtra("PHONE_NUMBER",phoneNo);

        //To prevent user from going back to previous activity by clicking back button
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }


}