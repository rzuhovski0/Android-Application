package com.btproject.barberise.reservation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.btproject.barberise.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactInfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap myMap;

    /**TextViews of Hours*/
    private TextView mondayTextView,tuesdayTextView,wednesdayTextView,thursdayTextView,fridayTextView,saturdayTextView,sundayTextView;
    private ArrayList<TextView> openingHoursTextViewArray = new ArrayList<>();

    /**Email & phone*/
    private TextView addressTextView,emailTextView,phoneTextView,barberShopNameTextView;

    /**OpeningHours*/
    private HashMap<String, ArrayList<String>> openingHours = new HashMap<>();

    // Info
    private String email,phone,address,name;

    private ImageView googleMapsLogoImageView;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        SupportMapFragment mapFragment = null;
        try {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            assert mapFragment != null;
            mapFragment.getMapAsync(this);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        initItems();

        getBundle();

        populateOpeningHoursTextViewArray();

        setHoursToTextViews();

        setNamePhoneEmail();

        googleMapsLogoImageView.setOnClickListener(view -> {
            openGoogleMaps(latLng);
        });
    }

    private void openGoogleMaps(LatLng destinationLatLng)
    {
        // Create a Uri with the destination coordinates
        String uriString = "geo:" + destinationLatLng.latitude + "," + destinationLatLng.longitude;

        // Create an Intent with the Google Maps action and the Uri
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
        mapIntent.setPackage("com.google.android.apps.maps"); // Specify the package name of Google Maps

        // Verify that an app capable of handling the intent is available
        PackageManager packageManager = getPackageManager();
        if (mapIntent.resolveActivity(packageManager) != null) {
            // Start the activity if an app is available
            startActivity(mapIntent);
        } else {
            // Handle the case when Google Maps is not installed
            Toast.makeText(this, "Google Maps is not installed", Toast.LENGTH_SHORT).show();
        }
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
        barberShopNameTextView = findViewById(R.id.barberShopNameTextView);

        googleMapsLogoImageView = findViewById(R.id.googleMapsLogoImageView);
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
            name = userInfoBundle.getString("name");
        }
    }

    private void setNamePhoneEmail()
    {
        barberShopNameTextView.setText(name);
        emailTextView.setText(email);
        phoneTextView.setText(phone);

        if(address.equals("undefined"))
            addressTextView.setText("Hlavná 1, Košice, Slovensko");
        else
            addressTextView.setText(address);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        if(address.equals("undefined"))
            address = "Hlavna 1, Kosice, Slovakia";

        //Convert address to coordinates (latitude, longitude)
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address location = addresses.get(0);
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                latLng = new LatLng(latitude, longitude);

                // Add marker and move the camera
                myMap.addMarker(new MarkerOptions().position(latLng).title(address));


                // Set the desired zoom level
                float zoomLevel = 15.0f; // Change this value as needed

                // Create a CameraPosition object with the desired location and zoom level
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(zoomLevel)
                        .build();
            // Move the camera to the desired position
            myMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            } else {
                Toast.makeText(this, "Unable to geocode the address", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}