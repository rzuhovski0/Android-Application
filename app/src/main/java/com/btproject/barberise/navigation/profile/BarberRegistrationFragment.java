package com.btproject.barberise.navigation.profile;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.btproject.barberise.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BarberRegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class BarberRegistrationFragment extends Fragment {

    private EditText mondayEditText,tuesdayEditText,wednesdayEditText,
            thursdayEditText,fridayEditText,saturdayEditText,sundayEditText;

    private Button openingHoursQuestionButton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BarberRegistrationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BarberRegistrationFragment newInstance(String param1, String param2) {
        BarberRegistrationFragment fragment = new BarberRegistrationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public BarberRegistrationFragment() {
        // Required empty public constructor
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

        View rootView = inflater.inflate(R.layout.fragment_barber_registration, container, false);
        initUIOpeningHours(rootView);


        return rootView;
    }

    private void initUIOpeningHours(View rootView)
    {
        mondayEditText = rootView.findViewById(R.id.mondayOpeningHoursTextView);
        tuesdayEditText = rootView.findViewById(R.id.tuesdayOpeningHoursTextView);
        wednesdayEditText = rootView.findViewById(R.id.wednesdayOpeningHoursTextView);
        thursdayEditText = rootView.findViewById(R.id.thursdayOpeningHoursTextView);
        fridayEditText = rootView.findViewById(R.id.fridayOpeningHoursTextView);
        saturdayEditText = rootView.findViewById(R.id.saturdayOpeningHoursTextView);
        sundayEditText = rootView.findViewById(R.id.sundayOpeningHoursTextView);
        openingHoursQuestionButton = rootView.findViewById(R.id.openingHoursQuestionButton);
    }
}