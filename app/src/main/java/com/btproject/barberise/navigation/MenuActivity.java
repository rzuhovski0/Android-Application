package com.btproject.barberise.navigation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.btproject.barberise.R;
import com.btproject.barberise.databinding.ActivityMenuBinding;
import com.btproject.barberise.navigation.profile.User;
import com.btproject.barberise.reservation.DataFetchCallback;
import com.btproject.barberise.reservation.Reservation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    ActivityMenuBinding binding;
    FirebaseDatabase database;
    DatabaseReference reference;

    public static ArrayList<User> recommendedUsers,ratedUsers,availableUsers,otherUsers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        boolean openReservations = getIntent().getBooleanExtra("openReservations",false);

        /**If opened from ReservationSuccessfulActivity*/
        if(openReservations) {
            replaceFragment(new CalendarFragment());
        }else {
            replaceFragment(new HomeFragment());
        }

        binding.bottomNavigationView2.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
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

    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

    private void getUsersFromDatabase(DataFetchCallback callback)
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    assert user != null;

                    user.setId(snapshot.getKey());

                    switch(user.getCategory()){
                        case "recommended":
                            recommendedUsers.add(user);
                            break;
                        case "best_rated":
                            ratedUsers.add(user);
                            break;
                        case "available_today":
                            availableUsers.add(user);
                            break;
                        default:
                            otherUsers.add(user);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        callback.onDataLoaded(null);
    }

    public static ArrayList<User> getRecommendedUsers() {
        return recommendedUsers;
    }

    public static ArrayList<User> getRatedUsers() {
        return ratedUsers;
    }

    public static ArrayList<User> getAvailableUsers() {
        return availableUsers;
    }

    public static ArrayList<User> getOtherUsers() {
        return otherUsers;
    }
}