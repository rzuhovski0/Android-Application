package com.btproject.barberise.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.btproject.barberise.R;
//import com.btproject.barberise.adapters.PastReservationsAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {

    private RecyclerView pastReservationsRecyclerView;
    private TextView nameTextView,priceTextView,timeTextView;
    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String>price = new ArrayList<>();
    private ArrayList<String>time = new ArrayList<>();
    private ArrayList<Integer> image = new ArrayList<>();

//    private PastReservationsAdapter adapter;



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
        initializeItems(rootView);
        fillAttrib();
//        return inflater.inflate(R.layout.fragment_calendar, container, false);
        return rootView;
    }

    private void fillAttrib() {

            time.add("4.okt,2022 15:00");
            time.add("7.sep,2022 12:30");
//            time.add("19.jan,2022 10:00");

            name.add("BARBER1");
            name.add("BARBER2");
//            name.add("BARBER3");

//            price.add("13,00 €");
            price.add("10,00 €");
            price.add("7,00 €");

//            image.add(R.drawable.btestone);
            image.add(R.drawable.background);
            image.add(R.drawable.background);

    }

    private void initializeItems(View rootView) {
        nameTextView = (TextView)rootView.findViewById(R.id.barberNameTextView);//textView
        priceTextView = (TextView)rootView.findViewById(R.id.priceTextView);//textView
        timeTextView = (TextView)rootView.findViewById(R.id.timeTextView);//textView

        pastReservationsRecyclerView = (RecyclerView)rootView.findViewById(R.id.pastReservationRecyclerView);
        pastReservationsRecyclerView.setHasFixedSize(true);
        pastReservationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
//        adapter = new PastReservationsAdapter(name,price,time,image);
//        pastReservationsRecyclerView.setAdapter(adapter);
    }
}