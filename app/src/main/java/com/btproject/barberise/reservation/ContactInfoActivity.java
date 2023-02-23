package com.btproject.barberise.reservation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.btproject.barberise.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactInfoActivity extends AppCompatActivity {

    private MapView mapView;
    private FusedLocationProviderClient mFusedLocationClient;

    /**TextViews of Hours*/
    private TextView mondayTextView,tuesdayTextView,wednesdayTextView,thursdayTextView,fridayTextView,saturdayTextView,sundayTextView;
    private ArrayList<TextView> openingHoursTextViewArray = new ArrayList<>();

    /**Email & phone*/
    private TextView addressTextView,emailTextView,phoneTextView;

    /**OpeningHours*/
    private HashMap<String, ArrayList<String>> openingHours = new HashMap<>();

    // Info
    private String email,phone,address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        initItems();

        getBundle();

        populateOpeningHoursTextViewArray();

        setHoursToTextViews();

        setNamePhoneEmail();
    }

    private void initItems()
    {
        mondayTextView = findViewById(R.id.mondayDisplayHours);
        tuesdayTextView = findViewById(R.id.tuesdayDisplayHours);
        wednesdayTextView = findViewById(R.id.wednesdayDisplayHours);
        thursdayTextView = findViewById(R.id.thursdayDisplayHours);
        fridayTextView = findViewById(R.id.fridayDisplayHours);
        saturdayTextView = findViewById(R.id.saturdayDisplayHours);
        sundayTextView = findViewById(R.id.sundayDisplayHours);

        emailTextView = findViewById(R.id.emailDisplay);
        phoneTextView = findViewById(R.id.phoneDisplay);
        addressTextView = findViewById(R.id.addressTextView);
    }

    private void populateOpeningHoursTextViewArray()
    {
        openingHoursTextViewArray.add(mondayTextView);
        openingHoursTextViewArray.add(tuesdayTextView);
        openingHoursTextViewArray.add(wednesdayTextView);
        openingHoursTextViewArray.add(thursdayTextView);
        openingHoursTextViewArray.add(fridayTextView);
        openingHoursTextViewArray.add(saturdayTextView);
        openingHoursTextViewArray.add(sundayTextView);

    }

    private void setHoursToTextViews()
    {
        int counter = 0;
        for (TextView textView : openingHoursTextViewArray)
        {
            ArrayList<String> weekDays = getWeekDays();
            ArrayList<String> hoursList = openingHours.get(weekDays.get(counter));
            if (hoursList != null && hoursList.size() > 0) {
                String hoursString = hoursList.get(0) + " - " + hoursList.get(hoursList.size() - 1);
                textView.setText(hoursString);
            }
            counter++;
        }
    }

    private ArrayList<String> getWeekDays()
    {
        ArrayList<String> days = new ArrayList<>();
        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");
        days.add("Saturday");
        days.add("Sunday");
        return days;
    }

    private void getBundle()
    {
        // Get the Intent that started this Activity
        Intent intent = getIntent();

        // Get the Bundle from the Intent
        Bundle userInfoBundle = intent.getExtras();

        // Check if the Bundle is not null
        if (userInfoBundle != null) {

            // Retrieve the values from the Bundle
            openingHours = (HashMap<String, ArrayList<String>>) userInfoBundle.getSerializable("openingHours");
            email = userInfoBundle.getString("email");
            phone = userInfoBundle.getString("phone");
            address = userInfoBundle.getString("address");
        }
    }

    private void setNamePhoneEmail()
    {
        emailTextView.setText(email);
        phoneTextView.setText(phone);
        addressTextView.setText(address);
    }

}