package com.btproject.barberise.navigation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.btproject.barberise.MyCallback;
import com.btproject.barberise.R;
import com.btproject.barberise.adapters.MyUsersAdapter;
import com.btproject.barberise.navigation.profile.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recommendedRecView,ratedRecView,discountRecView,otherRecView;
    private TextView recommendedTextView,ratedTextView, discountTextView;

    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String>ratings = new ArrayList<>();
    private ArrayList<Integer>images = new ArrayList<>();
    //    ShopsViewAdapter shopsViewAdapter;
//    UsersAdapter usersAdapter;
    MyUsersAdapter myUsersAdapter;
    MyUsersAdapter bestRatedAdapter;
    MyUsersAdapter availableTodayAdapter;
    MyUsersAdapter otherAdapter;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    FirebaseDatabase database;
    String userName;

    List<User> recommendedUsers,ratedUsers,availableUsers,otherUsers;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        fillAttrib();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        fillAttrib();
        initComponents(rootView);
//        initializeItems(rootView);
        return rootView;
    }

    private void initComponents(View rootView)
    {
        //Recommended Recycler & TextView
        recommendedTextView = (TextView)rootView.findViewById(R.id.recommendedTextView);//textView
        recommendedRecView = (RecyclerView)rootView.findViewById(R.id.recommendedRecyclerView);
        recommendedRecView.setHasFixedSize(true);
        recommendedRecView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));

        //Best Rated Recycler & TextView
        ratedTextView = (TextView)rootView.findViewById(R.id.ratedTextView);//textView
        ratedRecView = (RecyclerView)rootView.findViewById(R.id.ratedRecycleView);
        ratedRecView.setHasFixedSize(true);
        ratedRecView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));

        //Best Rated Recycler & TextView
        discountTextView = (TextView)rootView.findViewById(R.id.availableTodayTextView);//textView
        discountRecView = (RecyclerView)rootView.findViewById(R.id.availableTodayRecyclerView);
        discountRecView.setHasFixedSize(true);
        discountRecView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));

        //Best Rated RecyclerView
        otherRecView = (RecyclerView) rootView.findViewById(R.id.otherRecycleView);
        otherRecView.setHasFixedSize(true);
        otherRecView.setLayoutManager(new CustomGridLayoutManager(getContext()));

        recommendedUsers = new ArrayList<>();
        ratedUsers = new ArrayList<>();
        availableUsers = new ArrayList<>();
        otherUsers = new ArrayList<>();

        fetchUsers();

        myUsersAdapter = new MyUsersAdapter(recommendedUsers,requireActivity().getApplicationContext());
        otherAdapter = new MyUsersAdapter(otherUsers,requireActivity().getApplicationContext());
        availableTodayAdapter = new MyUsersAdapter(availableUsers,requireActivity().getApplicationContext());
        bestRatedAdapter = new MyUsersAdapter(ratedUsers,requireActivity().getApplicationContext());

        recommendedRecView.setAdapter(myUsersAdapter);
        otherRecView.setAdapter(otherAdapter);
        discountRecView.setAdapter(availableTodayAdapter);
        ratedRecView.setAdapter(bestRatedAdapter);

    }

    private void fetchUsers()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    assert user != null;

                    user.setId(dataSnapshot.getKey());

                    switch(user.getCategory()){
                        case "recommended":
                            recommendedUsers.add(user);
                            myUsersAdapter.notifyItemInserted(getItemIndex(user,recommendedUsers));
                            break;
                        case "best_rated":
                            ratedUsers.add(user);
                            myUsersAdapter.notifyItemInserted(getItemIndex(user,ratedUsers));
                            break;
                        case "available_today":
                            availableUsers.add(user);
                            myUsersAdapter.notifyItemInserted(getItemIndex(user,availableUsers));
                            break;
                        default:
                            otherUsers.add(user);
                            myUsersAdapter.notifyItemInserted(getItemIndex(user,otherUsers));
                    }


                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                user.setId(dataSnapshot.getKey());
                int index;
                switch (user.getCategory()){
                    case "recommended":
                        index = getItemIndex(user,recommendedUsers);
                        break;
                    case "best_rated":
                        index = getItemIndex(user,ratedUsers);
                        break;
                    case "available_today":
                        index = getItemIndex(user,availableUsers);
                        break;
                    default:
                        index = getItemIndex(user,otherUsers);
                }
                notifyCorrectList(user,index,"changed");
                myUsersAdapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                user.setId(dataSnapshot.getKey());
                int index;
                switch (user.getCategory()){
                    case "recommended":
                        index = getItemIndex(user,recommendedUsers);
                        break;
                    case "best_rated":
                        index = getItemIndex(user,ratedUsers);
                        break;
                    case "available_today":
                        index = getItemIndex(user,availableUsers);
                        break;
                    default:
                        index = getItemIndex(user,otherUsers);
                }
                notifyCorrectList(user,index,"removed");
                myUsersAdapter.notifyItemRemoved(index);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                // TODO: handle moved items
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // TODO: handle error
            }
        });

    }

    private void notifyCorrectList(User user,int index,String operation)
    {
        if(operation.equals("removed")){
            switch(user.getCategory()){
                case "recommended":
                    recommendedUsers.remove(index);
                    break;
                case "best_rated":
                    ratedUsers.remove(index);
                    break;
                case "available_today":
                    availableUsers.remove(index);
                    break;
                default:
                    otherUsers.remove(index);
            }
        }else if(operation.equals("changed")){
            switch(user.getCategory()){
                case "recommended":
                    recommendedUsers.set(index,user);
                    break;
                case "best_rated":
                    ratedUsers.set(index,user);
                    break;
                case "available_today":
                    availableUsers.set(index,user);
                    break;
                default:
                    otherUsers.set(index,user);
            }
        }


    }

    private int getItemIndex(User user,List<User> userList)
    {
        int index = -1;
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getId().equals(user.getId())) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void fillAttrib()
    {
        ratings.add("4.6");
        ratings.add("4.1");
        ratings.add("5.2");
        ratings.add("3.9");
        ratings.add("0.0");

        names.add("BARBER1");
        names.add("BARBER2");
        names.add("BARBER3");
        names.add("BARBERSHOPKOSICE");
        names.add("BarberClub");

        images.add(R.drawable.background);
        images.add(R.drawable.background);
        images.add(R.drawable.background);
        images.add(R.drawable.background);
        images.add(R.drawable.background);
    }

    public class CustomGridLayoutManager  extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomGridLayoutManager (Context context) {
            super(context);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            return false;
        }
    }

}