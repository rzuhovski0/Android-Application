package com.btproject.barberise.navigation;

import static com.btproject.barberise.utils.DatabaseUtils.getCurrentUser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.btproject.barberise.R;
import com.btproject.barberise.database.clientDAO;
import com.btproject.barberise.navigation.profile.PartnerProfileActivity;
import com.btproject.barberise.settings.ProfileSettingsActivity;
import com.btproject.barberise.settings.PromoActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseUser user;
    String userUID;

    FirebaseDatabase database;
    DatabaseReference dbReference;

    private Context context;

    // Helper val for getting name and surname
    private String returnValue;
    private TextView partnerProfileTextView,promoTextView,profileTextView;
    private TextView usrNameTextView;

    private HashMap<String, String> credentials = new HashMap<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        initItems(rootView);



        clientDAO.DataExistsCallback dataExistsCallback = new clientDAO.DataExistsCallback() {
            @Override
            public void onDataInvalid(boolean userExists) {
                usrNameTextView.setText(getInitials(credentials));
            }
        };getClientInfo(dataExistsCallback);


        partnerProfileTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PartnerProfileActivity.class);
                startActivity(intent);
            }
        });

        promoTextView.setOnClickListener(view ->
        {
            Intent intent = new Intent(context, PromoActivity.class);
            startActivity(intent);
        });

        profileTextView.setOnClickListener(View ->
        {
            Intent intent = new Intent(context, ProfileSettingsActivity.class);
            startActivity(intent);
        });

        return rootView;
    }

    private String getClientInfo(clientDAO.DataExistsCallback dataExistsCallback)
    {
        FirebaseUser user = getCurrentUser();

        if(user == null)
            return "";

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("clients").child(user.getUid());
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String surname = snapshot.child("surname").getValue(String.class);

                credentials.put("name",name);
                credentials.put("surname",surname);
                dataExistsCallback.onDataInvalid(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return returnValue;
    }

    private void initItems(View rootView)
    {
        context = requireActivity().getApplicationContext();

        partnerProfileTextView = rootView.findViewById(R.id.partnerTextView);
        usrNameTextView = rootView.findViewById(R.id.usrNameTextView);
        promoTextView = rootView.findViewById(R.id.promoTextView);
        profileTextView = rootView.findViewById(R.id.profileTextView);
        /**Set the correct color for */

    }

    public String getInitials(HashMap<String,String> credentials) {

        String name = credentials.get("name");
        String surname = credentials.get("surname");

        if(name == null || surname == null)
            return "";

        // Get the first name
        String firstName = name.split("\\s+")[0];

        // Get the first letter of the surname
        String surnameInitial = surname.substring(0, 1);

        // Combine the first name and surname initial with a space in between
        return firstName + " " + surnameInitial + ".";
    }



}