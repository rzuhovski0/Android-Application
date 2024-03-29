package com.btproject.barberise.navigation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.btproject.barberise.R;
import com.btproject.barberise.adapters.MyUsersAdapter;
import com.btproject.barberise.adapters.SearchAdapter;
import com.btproject.barberise.navigation.profile.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recommendedRecView,ratedRecView,discountRecView,otherRecView;
    private TextView recommendedTextView,ratedTextView, discountTextView, otherTextView;
    private TextView usrName;

    MyUsersAdapter myUsersAdapter;
    MyUsersAdapter bestRatedAdapter;
    MyUsersAdapter availableTodayAdapter;
    MyUsersAdapter otherAdapter;
    SearchAdapter searchAdapter;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    FirebaseDatabase database;
    String userName;

    private SearchView searchView;
    private RecyclerView searchRecyclerView;

    private boolean searchViewOpened = false;

    private static boolean isUsersFetched = false;

    private static ArrayList<User> recommendedUsers,ratedUsers,availableUsers,otherUsers,allUsers = new ArrayList<>();

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        initComponents(rootView);

        /**Set username of the current user*/
        OnUserNameRetrievedListener listener =
                userName -> usrName.setText(userName);
        setUserName(listener);

        return rootView;
    }

    private void setUserName(final OnUserNameRetrievedListener listener) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            // User is not logged in, return null
            listener.onUserNameRetrieved(null);
            return;
        }

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userNameRef = dbRef.child("clients").child(user.getUid()).child("name");
        userNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userName = (String) snapshot.getValue();
                listener.onUserNameRetrieved(userName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onUserNameRetrieved(null);
            }
        });
    }

    public interface OnUserNameRetrievedListener {
        void onUserNameRetrieved(String userName);
    }


    private void initComponents(View rootView)
    {
        searchView = rootView.findViewById(R.id.searchView);
        searchRecyclerView = rootView.findViewById(R.id.searchRecyclerView);

        initSearchView();

        usrName = (TextView)rootView.findViewById(R.id.usrName);
//        assert user != null;
//        usrName.setText(user.getUid());

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
        otherTextView = rootView.findViewById(R.id.otherTextView);
        otherRecView = (RecyclerView) rootView.findViewById(R.id.otherRecycleView);
        otherRecView.setHasFixedSize(true);

        // If this recyclerView should be vertical, use this CustomGridLayoutManager + disabled scroll
        //otherRecView.setLayoutManager(new RecyclerViewUtils.CustomGridLayoutManager(getContext()));

        otherRecView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));


        if(recommendedUsers == null)
            recommendedUsers = new ArrayList<>();

        if(ratedUsers == null)
            ratedUsers = new ArrayList<>();

        if(availableUsers == null)
            availableUsers = new ArrayList<>();

        if(otherUsers == null)
            otherUsers = new ArrayList<>();

        if(allUsers == null)
            allUsers = new ArrayList<>();

        if(recommendedUsers.isEmpty() || ratedUsers.isEmpty() || availableUsers.isEmpty() || otherUsers.isEmpty())
            fetchUsers();

        sortUsers(recommendedUsers);
        sortUsers(ratedUsers);
        sortUsers(availableUsers);
        sortUsers(otherUsers);


        myUsersAdapter = new MyUsersAdapter(recommendedUsers,requireActivity().getApplicationContext());
        availableTodayAdapter = new MyUsersAdapter(availableUsers,requireActivity().getApplicationContext());
        bestRatedAdapter = new MyUsersAdapter(ratedUsers,requireActivity().getApplicationContext());
        otherAdapter = new MyUsersAdapter(otherUsers,requireActivity().getApplicationContext());

        recommendedRecView.setAdapter(myUsersAdapter);
        otherRecView.setAdapter(otherAdapter);
        discountRecView.setAdapter(availableTodayAdapter);
        ratedRecView.setAdapter(bestRatedAdapter);
    }

    private void sortUsers(ArrayList<User> users)
    {
        // Define a custom Comparator for sorting User objects by their customPriority attribute
        Comparator<User> customPriorityComparator = new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                return Integer.compare(user1.getCustomPriority(), user2.getCustomPriority());
            }
        };

        // Sort the ArrayList of User objects based on their customPriority attribute
        Collections.sort(users, customPriorityComparator);
    }


    private void initSearchView()
    {

        /**Older Android APIs tend to push cursor on searchView after the initial start of the app
         * this removes the cursor*/
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // No need to implement

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchRecyclerView.setVisibility(View.VISIBLE);
                filterText(newText);
                return true;
            }
        });

    }
    private void updateVisibility(int visibility)
    {
        recommendedRecView.setVisibility(visibility);
        recommendedTextView.setVisibility(visibility);

        discountRecView.setVisibility(visibility);
        ratedTextView.setVisibility(visibility);

        ratedRecView.setVisibility(visibility);
        discountTextView.setVisibility(visibility);

        otherRecView.setVisibility(visibility);
        otherTextView.setVisibility(visibility);

    }
    private void filterText(String text)
    {
        ArrayList<User> filteredList = new ArrayList<>();
        Context context = requireActivity().getApplicationContext();

        for(User user : allUsers)
        {
            if(user.getUsername().toLowerCase().contains(text.toLowerCase()))
                filteredList.add(user);
        }

        if(!filteredList.isEmpty()) {
            searchAdapter = new SearchAdapter(filteredList, context);
            searchRecyclerView.setHasFixedSize(true);
            searchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            searchAdapter.setFilteredList(filteredList);
            searchRecyclerView.setAdapter(searchAdapter);
            updateVisibility(View.GONE);
        }
    }

    private void fetchUsers()
    {

        if (isUsersFetched) {
            return;
        }

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
                            allUsers.add(user);
                            myUsersAdapter.notifyItemInserted(getItemIndex(user,recommendedUsers));
                            break;
                        case "best_rated":
                            ratedUsers.add(user);
                            allUsers.add(user);
                            myUsersAdapter.notifyItemInserted(getItemIndex(user,ratedUsers));
                            break;
                        case "available_today":
                            availableUsers.add(user);
                            allUsers.add(user);
                            myUsersAdapter.notifyItemInserted(getItemIndex(user,availableUsers));
                            break;
                        default:
                            otherUsers.add(user);
                            allUsers.add(user);
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
        isUsersFetched = true;
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



}