package com.btproject.barberise.navigation;

import static com.btproject.barberise.utils.DatabaseUtils.getReservationsFromDatabase;
import static com.btproject.barberise.utils.ReservationUtils.sortReservations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.btproject.barberise.R;
import com.btproject.barberise.adapters.ReservationCardAdapter;
import com.btproject.barberise.navigation.profile.User;
import com.btproject.barberise.reservation.DataFetchCallback;
import com.btproject.barberise.reservation.Reservation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
//import com.btproject.barberise.adapters.PastReservationsAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {

    private RecyclerView pastReservationsRecyclerView;

    /**Oncoming reservation*/
    private TextView nameTextView,priceTextView,timeTextView;
    private FrameLayout buttonCancelFrameLayout;
    private TextView seeAllOncomingTextView;

    /**Reservation array*/
    private ArrayList<Reservation> reservations;

    /**Adapter*/
    private ReservationCardAdapter reservationCardAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        /**Initialize components*/
        initComponents(rootView);

        /**Fetch reservations from database*/
        DataFetchCallback callback = new DataFetchCallback() {
            @Override
            public void onDataLoaded(User barberShop) {

            }
            @Override
            public void onReservationsLoaded(ArrayList<Reservation> reservations) {
                sortReservations(reservations);
                initAdapter(reservations);
            }
        };
        getReservationsRealTime(reservations,callback);

        return rootView;
    }

    public static void getReservationsRealTime(ArrayList<Reservation> reservations, DataFetchCallback callback) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference resRef = database.getReference().child("users").child(currentUserId).child("reservations");

            resRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Reservation reservation = snapshot.getValue(Reservation.class);
                    reservations.add(reservation);
                    callback.onReservationsLoaded(reservations);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Reservation updatedReservation = snapshot.getValue(Reservation.class);

                    // Find the index of the updated reservation in the list
                    String reservationKey = snapshot.getKey();
                    int index = -1;
                    for (int i = 0; i < reservations.size(); i++) {
                        Reservation reservation = reservations.get(i);
                        if (reservation.getId().equals(reservationKey)) {
                            index = i;
                            break;
                        }
                    }

                    // Update the reservation in the list and notify the adapter of the change
                    if (index != -1) {
                        reservations.set(index, updatedReservation);
                        callback.onReservationsLoaded(reservations);
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    String reservationKey = snapshot.getKey();

                    // Find the index of the removed reservation in the list
                    int index = -1;
                    for (int i = 0; i < reservations.size(); i++) {
                        Reservation reservation = reservations.get(i);
                        if (reservation.getId().equals(reservationKey)) {
                            index = i;
                            break;
                        }
                    }

                    // Remove the reservation from the list and notify the adapter of the change
                    if (index != -1) {
                        reservations.remove(index);
                        callback.onReservationsLoaded(reservations);
                    }
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    // Handle moved reservation
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }
    }



    private void initComponents(View rootView) {
        reservations = new ArrayList<>();

        nameTextView = (TextView)rootView.findViewById(R.id.barberNameTextView);//textView
        priceTextView = (TextView)rootView.findViewById(R.id.priceTextView);//textView
        timeTextView = (TextView)rootView.findViewById(R.id.inCalDateTimeTextView);//textView

        seeAllOncomingTextView = rootView.findViewById(R.id.inCalSeeAllTextView);
        buttonCancelFrameLayout = rootView.findViewById(R.id.inCalAgainFragmentView);

        pastReservationsRecyclerView = (RecyclerView)rootView.findViewById(R.id.pastReservationRecyclerView);
        pastReservationsRecyclerView.setHasFixedSize(true);
        pastReservationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
    }

    private void initAdapter(ArrayList<Reservation> reservations){
        if(isAdded()) {
            reservationCardAdapter = new ReservationCardAdapter(reservations, requireActivity().getApplicationContext(),getResources(), requireActivity().getTheme());
            pastReservationsRecyclerView.setAdapter(reservationCardAdapter);
        }
    }

}