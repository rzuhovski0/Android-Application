package com.btproject.barberise.reservation;

import static android.content.ContentValues.TAG;

import androidx.annotation.FontRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.applikeysolutions.cosmocalendar.utils.SelectionType;
import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.btproject.barberise.MyCallback;
import com.btproject.barberise.R;
import com.btproject.barberise.navigation.MenuActivity;
import com.btproject.barberise.navigation.profile.User;
import com.btproject.barberise.utils.CalendarUtils;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ReservationActivity extends AppCompatActivity {

    private User serviceProvider;
    private String serviceProviderId,username,imageURL;

    private Uri imageUri;
    private ShapeableImageView profilePictureImageView;
    private TextView barberShopName;
    private String[] services,categories,times;
    private int currentStep = 1;
    private RadioButton[] radioButtonsArrayCategory,radioButtonArrayServices;
    private TextView currentStepTextView,saveDateTextView;
    private CalendarView calendarView;
    private RadioGroup categoryRadioGroup,serviceRadioGroup;
    private ArrayList<String> categoryNames,subcategoryNames1, subcategoryNames2, subcategoryNames3;
    private String selectedCategory;
    private Map<String,ArrayList<String>> openingHours;
    private String day;
    private Button reserveButton;
    RadioGroup opening_hour_radio_group;
    Reservation reservation;
    ArrayList<Reservation> reservationsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);


        serviceProviderId = getIntent().getStringExtra("id");
        openingHours = new HashMap<>();
        reservationsList = new ArrayList<>();

        // Set up the callback that will be called when the data is available
        MyCallback userDataCallback = new MyCallback() {
            @Override
            public void onProfileDataLoaded(String imageURL, String username, Bitmap imageBitmap) {

                // Update the UI with the imageURL
                Glide.with(profilePictureImageView.getContext())
                        .load(imageURL)
                        .into(profilePictureImageView);

                barberShopName.setText(username);

                /**RESERVATION*/
                reservation = new Reservation("Filip Rzuhovsky",username);
            }

            @Override
            public void onCategoriesLoaded(ArrayList<String> categoryNames) {

            }

            @Override
            public void onHoursLoaded() {

            }

            @Override
            public void onReservationsLoaded(ArrayList<Reservation> reservationsList) {

            }
        };
        // Load the data and pass the callback to the method
        fetchUser(serviceProviderId,userDataCallback);

        initComponents();

        updateRadioGroupVisibility();

        setUpCalendar();

        // Set up the callback that will be called when the data is available
        MyCallback categoriesCallback = new MyCallback() {
            @Override
            public void onProfileDataLoaded(String imageURL, String username, Bitmap imageBitmap) {

            }

            @Override
            public void onCategoriesLoaded(ArrayList<String> categoryNames) {
                setRadioButtonText(radioButtonsArrayCategory,categoryNames,categoryRadioGroup);
            }

            @Override
            public void onHoursLoaded() {

            }

            @Override
            public void onReservationsLoaded(ArrayList<Reservation> reservationsList) {

            }

        };
        getCategoriesFromDatabase(categoriesCallback);

        // First-step
        categoryRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            currentStep++;
            selectCorrectTextColor(checkedId,categoryRadioGroup);

            RadioButton selectedRadioButton = findViewById(checkedId);
            selectedCategory = selectedRadioButton.getText().toString();

            expandCategory(selectedCategory);

            /**RESERVATION Category*/
            reservation.setCategoryName(selectedCategory);

            categoryRadioGroup.startAnimation(
                    FadeOut()
            );

        });
        //First-step// First-step// First-step// First-step// First-step// First-step// First-step// First-step// First-step

        // Second-step
        serviceRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            currentStep++;
            selectCorrectTextColor(checkedId,serviceRadioGroup);

            RadioButton selectedRadioButton = findViewById(checkedId);
            String selectedSubcategory = selectedRadioButton.getText().toString();

            /**RESERVATION SubCategory*/
            reservation.setSubcategoryName(selectedSubcategory);

            serviceRadioGroup.startAnimation(
                    FadeOut()
            );
        });

        MyCallback callback = new MyCallback() {
            @Override
            public void onProfileDataLoaded(String imageURL, String username, Bitmap imageBitmap) {

            }

            @Override
            public void onCategoriesLoaded(ArrayList<String> categoryName) {

            }

            @Override
            public void onHoursLoaded() {

            }

            @Override
            public void onReservationsLoaded(ArrayList<Reservation> reservationsList) {

            }
        };
        getReservationsFromDatabase(callback);


        saveDateTextView.setOnClickListener(view -> {
            currentStep++;
            updateRadioGroupVisibility();

            List<Calendar> selectedDates = calendarView.getSelectedDates();

            // Iterate over the selected dates
            for (Calendar selectedDate : selectedDates) {
//                Toast.makeText(getApplicationContext(), day, Toast.LENGTH_SHORT).show();
                /** RESERVATION Date*/
//                reservation.setSelectedDate(selectedDate);
                String dateStringFormat = CalendarUtils.getDateInString(selectedDate);
                reservation.setDate(dateStringFormat);

                day = CalendarUtils.getDay(selectedDate);
                calendarView.update();
            }

            MyCallback openingHoursCallback = new MyCallback() {
                @Override
                public void onProfileDataLoaded(String imageURL, String username, Bitmap imageBitmap) {

                }
                @Override
                public void onCategoriesLoaded(ArrayList<String> categoryName) {

                }
                @Override
                public void onHoursLoaded() {
                    opening_hour_radio_group.removeAllViews();
                    populateOpeningHoursRadioGroup(opening_hour_radio_group,getAvailableHours(day));
                    updateRadioGroupVisibility();
                }

                @Override
                public void onReservationsLoaded(ArrayList<Reservation> reservationsList) {

                }
            };
            getHoursFromDatabase(openingHoursCallback);

        });

        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(),reservation.getCategoryName()
                        +reservation.getTime()
                        +reservation.getDate(),Toast.LENGTH_LONG).show();
                if(reservation.reservationValid())
                    try {
                        // Code to save the reservation to Firebase
                        makeReservationWithAuth(reservation);
//                        new SaveReservationTask().execute(reservation);
                    } catch (Exception e) {
                        // Handle the exception, for example, by logging an error message
                        Toast.makeText(getApplicationContext(),"Error saving reservation: " + e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                else{
                    Toast.makeText(getApplicationContext(),"Please make sure that all reservation fields are valid",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getReservationsFromDatabase(MyCallback callback)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference().child("users").child(serviceProviderId).child("reservations");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot reservationSnapshot : snapshot.getChildren()) {
                    Reservation reservation = reservationSnapshot.getValue(Reservation.class);
                    reservationsList.add(reservation);
                }
                callback.onReservationsLoaded(reservationsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });
    }

    private void makeReservation(Reservation reservation)
    {
        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(serviceProviderId);

        // Create a reservationMap containing the reservation properties
        HashMap<String, Object> reservationMap = new HashMap<>();
        reservationMap.put("date", reservation.getDate());
        reservationMap.put("time", reservation.getTime());
        reservationMap.put("username",reservation.getUserName());
        reservationMap.put("service_provider_name",reservation.getServiceProviderName());
        reservationMap.put("category",reservation.getCategoryName());
        reservationMap.put("subcategory",reservation.getSubcategoryName());
//        reservationMap.put("selected_date",reservation.getSelectedDate());

        // Create a new child node with a unique ID under the "reservations" node
        DatabaseReference reservationsRef = dbRef.child("reservations").push();

        // Set the value of the child node to the reservationMap
        reservationsRef.setValue(reservationMap).addOnSuccessListener(aVoid -> {
            // Reservation saved successfully
            Toast.makeText(getApplicationContext(),"Reservation saved successfully",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ReservationActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        }).addOnFailureListener(e -> {
            // Reservation saving failed
            Toast.makeText(getApplicationContext(),"Error saving reservation: " + e.getMessage(),Toast.LENGTH_SHORT).show();
        });
    }

    private void makeReservationWithAuth(Reservation reservation)
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Get a reference to the current user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Check if the user is authenticated
        if (currentUser != null) {
            // User is authenticated, so we can save the reservation
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(serviceProviderId);
            DatabaseReference reservationsRef = userRef.child("reservations").push();

            HashMap<String, Object> reservationMap = new HashMap<>();
            reservationMap.put("reservationID", reservationsRef.getKey());
            reservationMap.put("date", reservation.getDate());
            reservationMap.put("time", reservation.getTime());
            reservationMap.put("username",reservation.getUserName());
            reservationMap.put("service_provider_name",reservation.getServiceProviderName());
            reservationMap.put("category",reservation.getCategoryName());
            reservationMap.put("subcategory",reservation.getSubcategoryName());
//            reservationMap.put("selected_date",reservation.getSelectedDate());
            reservationMap.put("day",day);

            reservationsRef.setValue(reservationMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Reservation saved successfully
                    Toast.makeText(getApplicationContext(), "Reservation saved successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ReservationActivity.this, ReservationSuccessfulActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Reservation saving failed
                    Toast.makeText(getApplicationContext(), "Error saving reservation: "
                            + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // User is not authenticated, so we cannot save the reservation
            Toast.makeText(this, "You must be logged in to make a reservation", Toast.LENGTH_SHORT).show();
        }
    }

    private class SaveReservationTask extends AsyncTask<Reservation, Void, Void>
    {
        private Exception exception;

        @Override
        protected Void doInBackground(Reservation... reservations) {
            try {
                // Save the reservation to the database
                makeReservationWithAuth(reservations[0]);
            } catch (Exception e) {
                exception = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (exception == null) {
                // Reservation saved successfully
                Toast.makeText(getApplicationContext(),"Reservation saved successfully",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ReservationActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Reservation saving failed
                Toast.makeText(getApplicationContext(),"Error saving reservation: " + exception.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private GridLayout.LayoutParams getGridLayoutParams()
    {
        // Set the horizontal gravity of the layout params to center the row of elements
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.width = GridLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.setGravity(Gravity.CENTER_HORIZONTAL);
        layoutParams.setMargins(getPx(12), 0, getPx(12), getPx(20));

        return layoutParams;
    }

    private void populateOpeningHoursRadioGroup(RadioGroup radioGroup, ArrayList<String> openingHours) {
        // Set up the GridLayout
        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(5);
        gridLayout.setRowCount((int) Math.ceil(openingHours.size() / 5.0));

        // Create and add the RadioButton views
        for (int i = 0; i < openingHours.size(); i++) {
            RadioButton radioButton = getRadioButton();
            radioButton.setText(openingHours.get(i));
            radioButton.setId(i);
            gridLayout.addView(radioButton, getGridLayoutParams());

            // Set the click listener to handle selection of the RadioButton
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int row = gridLayout.getRowCount();
                    int col = gridLayout.getColumnCount();

                    // Find the selected RadioButton
                    RadioButton selectedRadioButton = (RadioButton) v;
                    int selectedId = selectedRadioButton.getId();
                    int selectedRow = selectedId / col;
                    int selectedCol = selectedId % col;

                    // Clear the check from all other RadioButtons
                    for (int i = 0; i < row; i++) {
                        for (int j = 0; j < col; j++) {
                            View view = gridLayout.getChildAt(i * col + j);
                            if (view instanceof RadioButton && view.getId() != selectedId) {
                                ((RadioButton) view).setChecked(false);
                                ((RadioButton) view).setTextColor(Color.BLACK);
                            }
                        }
                    }

                    // Change the text color of the selected RadioButton to orange
                    selectedRadioButton.setTextColor(getResources().getColor(R.color.orange,getTheme()));

                    /** RESERVATION Time*/
                    reservation.setTime(selectedRadioButton.getText().toString());
                }
            });
        }

        // Add the GridLayout to the RadioGroup
        radioGroup.addView(gridLayout);
    }

    private ArrayList<String> getAvailableHours(String day)
    {
        ArrayList<String> availableHours = openingHours.get(day);
        assert availableHours != null;
        Collections.sort(availableHours);
        return availableHours;
    }

    private void getHoursFromDatabase(MyCallback callback)
    {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(serviceProviderId).child("opening_hours");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    ArrayList<String> availableHours;

                    for (DataSnapshot daySnapshot : snapshot.getChildren()) {
                        String day = daySnapshot.getKey();
                        availableHours = new ArrayList<>();
                        for (DataSnapshot hourSnapshot : daySnapshot.getChildren()){
                            String hour = hourSnapshot.getValue(String.class);
                            availableHours.add(hour);
                        }
                        openingHours.put(day,availableHours);
                    }
                    callback.onHoursLoaded();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void expandCategory(String selectedText) {
        if(selectedText.equals(categoryNames.get(0))) {
            radioButtonArrayServices = new RadioButton[subcategoryNames1.size()];
            setRadioButtonText(radioButtonArrayServices, subcategoryNames1, serviceRadioGroup);
        }
        else if(selectedText.equals(categoryNames.get(1))) {
            radioButtonArrayServices = new RadioButton[subcategoryNames2.size()];
            setRadioButtonText(radioButtonArrayServices, subcategoryNames2, serviceRadioGroup);
        }
        else if(selectedText.equals(categoryNames.get(2))) {
            radioButtonArrayServices = new RadioButton[subcategoryNames3.size()];
            setRadioButtonText(radioButtonArrayServices, subcategoryNames3, serviceRadioGroup);
        }
    }

    private void extractCategoryAndSubcategoryNames(DataSnapshot dataSnapshot,MyCallback callback) {
        categoryNames = new ArrayList<>();
        subcategoryNames1 = new ArrayList<>();
        subcategoryNames2 = new ArrayList<>();
        subcategoryNames3 = new ArrayList<>();


        for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
            String categoryName = categorySnapshot.child("name").getValue(String.class);
            categoryNames.add(categoryName);
        }

        radioButtonsArrayCategory = new RadioButton[categoryNames.size()];

        for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
            String categoryName = categorySnapshot.child("name").getValue(String.class);
            categoryNames.add(categoryName);

            for (DataSnapshot subcategorySnapshot : categorySnapshot.child("subcategories").getChildren()) {
                String subcategoryName = subcategorySnapshot.child("name").getValue(String.class);
                String parentCategoryName = categorySnapshot.child("name").getValue(String.class);
                double subcategoryPrice = subcategorySnapshot.child("price").getValue(Double.class);
                if(Objects.equals(parentCategoryName, categoryNames.get(0)))
                    subcategoryNames1.add(subcategoryName + " "+subcategoryPrice+ "€");
                if(Objects.equals(parentCategoryName, categoryNames.get(1)))
                    subcategoryNames2.add(subcategoryName + " "+subcategoryPrice+ "€");
                if(Objects.equals(parentCategoryName, categoryNames.get(2)))
                    subcategoryNames3.add(subcategoryName + " "+subcategoryPrice+ "€");
            }
        }
        callback.onCategoriesLoaded(categoryNames);
    }

    private void setRadioButtonText(RadioButton[] radioButtonsArray, ArrayList<String> StringArrayTest, RadioGroup radioGroup) {

        radioGroup.removeAllViews();

        for (int i = 0; i < radioButtonsArray.length; i++) {
            radioButtonsArray[i] = new RadioButton(getApplicationContext());

            //Set-up layout params and font params
            radioButtonsArray[i].setLayoutParams(getLayoutParams());
            radioButtonsArray[i].setTypeface(getTypeface(R.font.sen));

            //Set-up background selector, disable Rb Button & set text properties
            radioButtonsArray[i].setBackgroundResource(R.drawable.radio_selector);
            radioButtonsArray[i].setButtonDrawable(R.drawable.radio_null_button);
            radioButtonsArray[i].setTextColor(Color.BLACK);
            radioButtonsArray[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            radioButtonsArray[i].setTextSize(14);

            //design
            radioButtonsArray[i].setHeight(getPx(50));
            radioButtonsArray[i].setElevation(getPx(4));

            //apply testing array
            radioButtonsArray[i].setText(StringArrayTest.get(i));
            //add to radioGroup
            radioGroup.addView(radioButtonsArray[i]);
        }
    }

    public void getCategoriesFromDatabase(MyCallback callback) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(serviceProviderId).child("categories");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    extractCategoryAndSubcategoryNames(dataSnapshot,callback);
                } else {
                    Log.d(TAG, "No categories found");
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to get categories from database", databaseError.toException());
            }
        });

    }

    private RadioButton getRadioButton()
    {
        RadioButton radioButton = new RadioButton(getApplicationContext());
        radioButton.setTypeface(getTypeface(R.font.sen));
        radioButton.setBackgroundColor(Color.TRANSPARENT);
        radioButton.setButtonDrawable(R.drawable.radio_null_button);
        radioButton.setTextColor(Color.BLACK);
        return radioButton;
    }

    @Override
    public void onBackPressed() {

        if (currentStep > 1) {
            currentStep--;
            updateRadioGroupVisibility();
//            updateRequestTextView();
        } else {
            super.onBackPressed();
        }

    }

    private void updateRadioGroupVisibility() {
        updateRequestTextView();
        switch (currentStep) {
            case 1:
                categoryRadioGroup.setVisibility(View.VISIBLE);
                serviceRadioGroup.setVisibility(View.GONE);

                opening_hour_radio_group.setVisibility(View.GONE);

                calendarView.setVisibility(View.GONE);
                saveDateTextView.setVisibility(View.GONE);
                reserveButton.setVisibility(View.GONE);

                break;
            case 2:
                categoryRadioGroup.setVisibility(View.GONE);
                serviceRadioGroup.setVisibility(View.VISIBLE);

                opening_hour_radio_group.setVisibility(View.GONE);


                calendarView.setVisibility(View.GONE);
                saveDateTextView.setVisibility(View.GONE);
                reserveButton.setVisibility(View.GONE);


                break;
            case 3:
                categoryRadioGroup.setVisibility(View.GONE);
                serviceRadioGroup.setVisibility(View.GONE);

                opening_hour_radio_group.setVisibility(View.GONE);

                calendarView.setVisibility(View.VISIBLE);
                saveDateTextView.setVisibility(View.VISIBLE);
                reserveButton.setVisibility(View.GONE);


                break;
            case 4:
                categoryRadioGroup.setVisibility(View.GONE);
                serviceRadioGroup.setVisibility(View.GONE);

                opening_hour_radio_group.setVisibility(View.VISIBLE);

                calendarView.setVisibility(View.GONE);
                saveDateTextView.setVisibility(View.GONE);
                reserveButton.setVisibility(View.VISIBLE);

                break;
        }
    }

    private void updateRequestTextView()
    {
        Drawable reservation_circle;
        switch(currentStep)
        {
            case 1:
                currentStepTextView.setText(R.string.choose_category);
                reservation_circle = ResourcesCompat.getDrawable(getResources(),R.drawable.reservation_circle_one,getTheme());
                currentStepTextView.setCompoundDrawablesWithIntrinsicBounds(reservation_circle,null,null,null);
                break;
            case 2:
                currentStepTextView.setText(R.string.choose_service);
                reservation_circle = ResourcesCompat.getDrawable(getResources(),R.drawable.reservation_circle_two,getTheme());
                currentStepTextView.setCompoundDrawablesWithIntrinsicBounds(reservation_circle,null,null,null);
                break;
            case 3:
                currentStepTextView.setText(R.string.choose_date);
                reservation_circle = ResourcesCompat.getDrawable(getResources(),R.drawable.reservation_circle_three,getTheme());
                currentStepTextView.setCompoundDrawablesWithIntrinsicBounds(reservation_circle,null,null,null);
                break;
            case 4:
                currentStepTextView.setText(R.string.choose_time);
                reservation_circle = ResourcesCompat.getDrawable(getResources(),R.drawable.reservation_circle_four,getTheme());
                currentStepTextView.setCompoundDrawablesWithIntrinsicBounds(reservation_circle,null,null,null);
                break;
            default:
                break;
        }

    }

    private Animation FadeOut()
    {
        // Apply fade-out animation to the RadioGroup
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(1000);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Do nothing
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                updateRadioGroupVisibility();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Do nothing
            }
        });
        return fadeOut;
    }

    private void initComponents()
    {
        profilePictureImageView = findViewById(R.id.backgroundImageView);
        barberShopName = findViewById(R.id.usernameTextView);
        opening_hour_radio_group = findViewById(R.id.opening_hour_radio_group);

        currentStepTextView = findViewById(R.id.currentStepTextView);
        saveDateTextView = findViewById(R.id.saveDateTextView);

        categoryRadioGroup = findViewById(R.id.categoryRadioGroup);
        serviceRadioGroup = findViewById(R.id.serviceRadioGroup);

        calendarView = findViewById(R.id.cosmo_calendar);
        reserveButton = findViewById(R.id.reserveButton);
    }

    private Typeface getTypeface(@FontRes int id)
    {
        //example id -> R.font.sen
        return ResourcesCompat.getFont(getApplicationContext(), id);
    }

    private RadioGroup.LayoutParams getLayoutParamsForTime()
    {
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(getPx(12), 0, getPx(4), getPx(32));
        params.weight = 1.0f;
        params.gravity = Gravity.CENTER_HORIZONTAL;

        return params;
    }

    private RadioGroup.LayoutParams getLayoutParams()
    {
        //Layout params for radio buttons
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                getPx(270),
                RadioGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(getPx(8), 0, getPx(8), getPx(16));
        params.weight = 1.0f;
        params.gravity = Gravity.CENTER;

        return params;
    }

    private void setUpCalendar()
    {
        // Initially hide the CalendarView
        calendarView.setVisibility(View.GONE);

        //Set First day of the week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);

        //Set Orientation 0 = Horizontal | 1 = Vertical
        calendarView.setCalendarOrientation(0);

        //add Weekends
        calendarView.setWeekendDays(new HashSet() {{
            add(Calendar.SATURDAY);
            add(Calendar.SUNDAY);
        }});

        //Range Selection
        calendarView.setSelectionType(SelectionType.SINGLE);

        int orangeColor = Color.parseColor("#FF7235");

        //Set palette-colors
        setCalendarColors(orangeColor);

        // Disable all dates past today
        disablePreviousDays();

        calendarView.update();
    }

    private void disablePreviousDays()
    {
        Set<Long> disabledDays = CalendarUtils.getPreviousDays();
        calendarView.setDisabledDays(disabledDays);
    }

    private void setCalendarColors(int color)
    {
        calendarView.setWeekendDayTextColor(color);
        calendarView.setSelectedDayBackgroundColor(color);
        calendarView.setCurrentDayIconRes(R.drawable.red_dot);
        calendarView.setDayTextColor(Color.BLACK);
    }

    public int getPx(int dp)
    {
        float scale = getResources().getDisplayMetrics().density;
        return((int) (dp * scale + 0.5f));
    }

    private void selectCorrectTextColor(int checkedId,RadioGroup radioGroup)
    {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            if (radioButton.getId() == checkedId) {

                //checking if radioGroup is for Time or not
                if(radioGroup.getOrientation() == LinearLayout.HORIZONTAL)
                    radioButton.setTextColor(Color.GRAY);
                else
                    radioButton.setTextColor(Color.WHITE);

            } else {
                radioButton.setTextColor(Color.BLACK);
            }
        }
    }

    private void fetchUser(String userId, MyCallback callback)
    {
        // Load the data from the database
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        // Add a listener to the reference to fetch the user data
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.exists()) {

                    serviceProvider = dataSnapshot.getValue(User.class);

                    assert serviceProvider != null;
                    serviceProvider.setId(dataSnapshot.getKey());
                    imageURL = serviceProvider.getProfile_picture();
                    username = serviceProvider.getUsername();

                    // Pass the data to the callback
                    callback.onProfileDataLoaded(imageURL,username,null);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
//                Log.w("db_error", "Failed to read value.", error.toException());
                Toast.makeText(getApplicationContext(),"db_fetching_error",Toast.LENGTH_SHORT).show();
            }
        });
    }






}