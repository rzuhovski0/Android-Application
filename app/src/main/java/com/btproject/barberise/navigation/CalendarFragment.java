package com.btproject.barberise.navigation;

import static com.btproject.barberise.utils.DatabaseUtils.getReservationsRealTime;
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





    private void initComponents(View rootView) {
        reservations = new ArrayList<>();

        pastReservationsRecyclerView = (RecyclerView)rootView.findViewById(R.id.pastReservationRecyclerView);
        pastReservationsRecyclerView.setHasFixedSize(true);
        pastReservationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
    }

    private void initAdapter(ArrayList<Reservation> reservations){
        if(isAdded()) {
            reservationCardAdapter = new ReservationCardAdapter(reservations, requireActivity().getApplicationContext());
            pastReservationsRecyclerView.setAdapter(reservationCardAdapter);
        }
    }

}