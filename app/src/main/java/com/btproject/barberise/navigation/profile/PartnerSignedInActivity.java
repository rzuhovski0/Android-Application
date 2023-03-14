package com.btproject.barberise.navigation.profile;

import static com.btproject.barberise.utils.DatabaseUtils.getReservationsRealTime;
import static com.btproject.barberise.utils.ReservationUtils.sortReservations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.btproject.barberise.R;
import com.btproject.barberise.adapters.ReservationCardAdapter;
import com.btproject.barberise.adapters.UserResCardAdapter;
import com.btproject.barberise.reservation.DataFetchCallback;
import com.btproject.barberise.reservation.Reservation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PartnerSignedInActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private UserResCardAdapter userResCardAdapter;

    private ArrayList<Reservation> reservations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_signed_in);

        recyclerView = findViewById(R.id.reservationRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DataFetchCallback dataFetchCallback = new DataFetchCallback() {
            @Override
            public void onDataLoaded(User barberShop) {
                // No need to implement
            }
                // No need to implement
            @Override
            public void onReservationsLoaded(ArrayList<Reservation> reservations) {
                if(reservations == null)
                    return;
                if(reservations.isEmpty())
                    return;

                sortReservations(reservations);
                initAdapter(reservations,user);

            }
        };
        // Check if user is signed in
        if(user != null)
            getReservationsRealTime(reservations,dataFetchCallback,"users");



    }

    private void getReservationsFromDatabase(String id, DataFetchCallback dataFetchCallback) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users").child(id).child("reservations");

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Reservation> reservations = new ArrayList<>();
                for (DataSnapshot reservationSnapshot : snapshot.getChildren()) {
                    Reservation reservation = reservationSnapshot.getValue(Reservation.class);
                    reservations.add(reservation);
                }
                dataFetchCallback.onReservationsLoaded(reservations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database read error
            }
        });
    }


    private void initAdapter(ArrayList<Reservation> reservations,FirebaseUser user){
            userResCardAdapter = new UserResCardAdapter(reservations,getApplicationContext(),user.getUid());
            recyclerView.setAdapter(userResCardAdapter);
        }
}