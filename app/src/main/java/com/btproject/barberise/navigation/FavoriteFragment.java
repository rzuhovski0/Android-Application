package com.btproject.barberise.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.btproject.barberise.R;
import com.btproject.barberise.adapters.FavoriteCardAdapter;
import com.btproject.barberise.navigation.profile.User;
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
import java.util.HashMap;

public class FavoriteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;

    private FavoriteCardAdapter favoriteCardAdapter;

    private ArrayList<HashMap<String,String>> favUsers;


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
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        initItems(view);

        DataFetchCallback callback = new DataFetchCallback() {
            @Override
            public void onDataLoaded(User barberShop) {
                initAdapters();
            }

            @Override
            public void onReservationsLoaded(ArrayList<Reservation> reservations) {

            }
        };
        getUserFavorites(callback);



        return view;
    }

    private void initItems(View rootView)
    {
        favUsers = new ArrayList<>();

        recyclerView = rootView.findViewById(R.id.inFavRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
    }

    private void initAdapters()
    {
        favoriteCardAdapter = new FavoriteCardAdapter(favUsers,requireActivity().getApplicationContext());
        recyclerView.setAdapter(favoriteCardAdapter);
    }

    private void getUserFavorites(DataFetchCallback callback) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
            DatabaseReference favRef = ref.child("favorites");

            favRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot favSnapshot : snapshot.getChildren()) {

                        // Get the values of the child nodes
                        HashMap<String,String> favMap = new HashMap<>();

                        String barberShopId = favSnapshot.child("barberShopId").getValue(String.class);
                        favMap.put("barberShopId",barberShopId);

                        String userName = favSnapshot.child("userName").getValue(String.class);
                        favMap.put("userName",userName);

                        String imageUrl = favSnapshot.child("imageUrl").getValue(String.class);
                        favMap.put("imageUrl",imageUrl);

                        favUsers.add(favMap);
                    }
                    callback.onDataLoaded(null);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

}