//package com.btproject.barberise;
//
//import static android.content.ContentValues.TAG;
//
//import androidx.annotation.FontRes;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.content.res.ResourcesCompat;
//
//import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.graphics.Typeface;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.View;
//import android.view.animation.AlphaAnimation;
//import android.view.animation.Animation;
//import android.widget.GridLayout;
//import android.widget.LinearLayout;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.applikeysolutions.cosmocalendar.utils.SelectionType;
//import com.applikeysolutions.cosmocalendar.view.CalendarView;
//import com.btproject.barberise.navigation.profile.User;
//import com.btproject.barberise.utils.CalendarUtils;
//import com.bumptech.glide.Glide;
//import com.google.android.material.imageview.ShapeableImageView;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.Set;
//
//public class ChosenUser extends AppCompatActivity {
//
//    private User serviceProvider;
//    private String serviceProviderId,username,imageURL;
//
//    private Uri imageUri;
//    private ShapeableImageView profilePictureImageView;
//    private TextView barberShopName;
//    private String[] services,categories,times;
//    private final int MAX_AMOUNT = 4;
//    private int currentStep = 1;
//    private RadioButton[] radioButtonsArrayCategory,radioButtonArrayServices;
//    private RadioButton[] timesRadioButtonUpper,timesRadioButtonMiddle,timesRadioButtonBottom;
//    private TextView currentStepTextView,saveDateTextView;
//    private CalendarView calendarView;
//    private RadioGroup categoryRadioGroup,serviceRadioGroup,
//            timeFirstRadioGroup,timeSecondRadioGroup,timeThirdRadioGroup;
//    private ArrayList<String> categoryNames,subcategoryNames1, subcategoryNames2, subcategoryNames3;
//    private String selectedText;
//    private Map<String, Map<String, Boolean>> openingHours = new HashMap<>();
//    private String day;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_choosen_user);
//
//        //Get service provider id
//        serviceProviderId = getIntent().getStringExtra("id");
//
//        // Set up the callback that will be called when the data is available
//        MyCallback userDataCallback = new MyCallback() {
//            @Override
//            public void onProfileDataLoaded(String imageURL, String username, Bitmap imageBitmap) {
//
//                // Update the UI with the imageURL
//                Glide.with(profilePictureImageView.getContext())
//                        .load(imageURL)
//                        .into(profilePictureImageView);
//
//                barberShopName.setText(username);
//            }
//
//            @Override
//            public void onCategoriesLoaded(ArrayList<String> categoryNames) {
//
//            }
//
//            @Override
//            public void onHoursLoaded() {
//
//            }
//        };
//        // Load the data and pass the callback to the method
//        fetchUser(serviceProviderId,userDataCallback);
//
//        //Initialize layout components
//        initComponents();
//
//        //set initial visibility
//        updateRadioGroupVisibility();
//
//        //-----TIME-----
//        timesRadioButtonUpper = new RadioButton[MAX_AMOUNT];
//        timesRadioButtonMiddle = new RadioButton[MAX_AMOUNT];
//        timesRadioButtonBottom = new RadioButton[MAX_AMOUNT];
//
//        //Setup Time RadioGroups, RadioButtons and create connection
//        setUpTimeRb();
//
//        setUpCalendar();
//
//        // Set up the callback that will be called when the data is available
//        MyCallback categoriesCallback = new MyCallback() {
//            @Override
//            public void onProfileDataLoaded(String imageURL, String username, Bitmap imageBitmap) {
//
//            }
//
//            @Override
//            public void onCategoriesLoaded(ArrayList<String> categoryNames) {
//                applyRbDesign(radioButtonsArrayCategory,categoryNames,categoryRadioGroup);
//            }
//
//            @Override
//            public void onHoursLoaded() {
//
//            }
//
//        };
//        getCategoriesFromDatabase(categoriesCallback);
//
//        // First-step
//        categoryRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
//            currentStep++;
//            selectCorrectTextColor(checkedId,categoryRadioGroup);
//
//            RadioButton selectedRadioButton = findViewById(checkedId);
//            selectedText = selectedRadioButton.getText().toString();
//
//            // Based on the user choice expand services (sub-categories) of the category
//            expandCategory(selectedText);
//
//            categoryRadioGroup.startAnimation(
//                    FadeOut()
//            );
//        });
//
//        // Second-step
//        serviceRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
//            currentStep++;
//            selectCorrectTextColor(checkedId,serviceRadioGroup);
//
//            serviceRadioGroup.startAnimation(
//                    FadeOut()
//            );
//        });
//
//
//        saveDateTextView.setOnClickListener(view -> {
//            currentStep++;
//            updateRadioGroupVisibility();
//
//            List<Calendar> selectedDates = calendarView.getSelectedDates();
//
//            // Iterate over the selected dates
//            for (Calendar selectedDate : selectedDates) {
////                Toast.makeText(getApplicationContext(), day, Toast.LENGTH_SHORT).show();
//                day = CalendarUtils.getDay(selectedDate);
//                calendarView.update();
//            }
//
//            MyCallback callback3 = new MyCallback() {
//                @Override
//                public void onProfileDataLoaded(String imageURL, String username, Bitmap imageBitmap) {
//
//                }
//                @Override
//                public void onCategoriesLoaded(ArrayList<String> categoryName) {
//
//                }
//                @Override
//                public void onHoursLoaded() {
//                    assignStringsToRadioButtons(timeFirstRadioGroup,timeSecondRadioGroup,timeThirdRadioGroup,getAvailableHours(day));
////                    populateOpeningHoursRadioGroup(timeFirstRadioGroup,getAvailableHours(day));
//                    updateRadioGroupVisibility();
//                }
//            };
//            getHoursFromDatabase(callback3);
//
//        });
//
//        timeFirstRadioGroup.setOnCheckedChangeListener(
//                (radioGroup, checkedId) ->
//                        selectCorrectTextColor(checkedId,timeFirstRadioGroup));
//
//        timeSecondRadioGroup.setOnCheckedChangeListener(
//                (radioGroup, checkedId) ->
//                        selectCorrectTextColor(checkedId,timeSecondRadioGroup));
//
//        timeThirdRadioGroup.setOnCheckedChangeListener(
//                (radioGroup, checkedId) ->
//                        selectCorrectTextColor(checkedId,timeThirdRadioGroup));
//    }
//
//    private void setUpTimeRb()
//    {
//        //---TIME---
//        timesRadioButtonUpper = new RadioButton[MAX_AMOUNT];
//        timesRadioButtonMiddle = new RadioButton[MAX_AMOUNT];
//        timesRadioButtonBottom = new RadioButton[MAX_AMOUNT];
//
//        timesRadioButtonUpper = getRadioButtons();
//        timesRadioButtonMiddle = getRadioButtons();
//        timesRadioButtonBottom = getRadioButtons();
//
//        addRadioButtonsToRadioGroup(timeFirstRadioGroup,timesRadioButtonUpper);
//        addRadioButtonsToRadioGroup(timeSecondRadioGroup,timesRadioButtonMiddle);
//        addRadioButtonsToRadioGroup(timeThirdRadioGroup,timesRadioButtonBottom);
//
//    }
//
//    private ArrayList<String> getAvailableHours(String day)
//    {
//        // Retrieve opening hours for Monday
//        Map<String, Boolean> dayHoursRetrieved = openingHours.get(day);
//
//        // Create an ArrayList to store the opening hours
//        ArrayList<String> availableHours = new ArrayList<>();
//
//        // Loop through the keys in the map (which are the opening hours)
//        assert dayHoursRetrieved != null;
//
//        for (String hour : dayHoursRetrieved.keySet()) {
//            // if the store is open during the corresponding time slot
//            if (Boolean.TRUE.equals(dayHoursRetrieved.get(hour))) {
//                availableHours.add(hour);
//            }
//        }
//
//        // Sort the opening hours list
//        Collections.sort(availableHours);
//
//        return availableHours;
//    }
//
//    private void getHoursFromDatabase(MyCallback callback)
//    {
//        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(serviceProviderId).child("opening_hours");
//        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    // Parse the opening hours data into a Map<String, Map<String, Boolean>> object
//                    for (DataSnapshot daySnapshot : snapshot.getChildren()) {
//                        String day = daySnapshot.getKey();
//                        Map<String, Boolean> hoursMap = new HashMap<>();
//                        for (DataSnapshot hourSnapshot : daySnapshot.getChildren()) {
//                            String hour = hourSnapshot.getKey();
//                            Boolean isOpen = hourSnapshot.getValue(Boolean.class);
//                            hoursMap.put(hour, isOpen);
//                        }
//                        openingHours.put(day, hoursMap);
//                    }
//                    callback.onHoursLoaded();
//                    // Use the openingHoursMap here...
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    private void expandCategory(String selectedText) {
//        if(selectedText.equals(categoryNames.get(0))) {
//            radioButtonArrayServices = new RadioButton[subcategoryNames1.size()];
//            applyRbDesign(radioButtonArrayServices, subcategoryNames1, serviceRadioGroup);
//        }
//        else if(selectedText.equals(categoryNames.get(1))) {
//            radioButtonArrayServices = new RadioButton[subcategoryNames2.size()];
//            applyRbDesign(radioButtonArrayServices, subcategoryNames2, serviceRadioGroup);
//        }
//        else if(selectedText.equals(categoryNames.get(2))) {
//            radioButtonArrayServices = new RadioButton[subcategoryNames3.size()];
//            applyRbDesign(radioButtonArrayServices, subcategoryNames3, serviceRadioGroup);
//        }
//    }
//
//    private void extractCategoryAndSubcategoryNames(DataSnapshot dataSnapshot,MyCallback callback) {
//        categoryNames = new ArrayList<>();
//        subcategoryNames1 = new ArrayList<>();
//        subcategoryNames2 = new ArrayList<>();
//        subcategoryNames3 = new ArrayList<>();
//
//
//        for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
//            String categoryName = categorySnapshot.child("name").getValue(String.class);
//            categoryNames.add(categoryName);
//        }
//
//        radioButtonsArrayCategory = new RadioButton[categoryNames.size()];
//
//        for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
//            String categoryName = categorySnapshot.child("name").getValue(String.class);
//            categoryNames.add(categoryName);
//
//            for (DataSnapshot subcategorySnapshot : categorySnapshot.child("subcategories").getChildren()) {
//                String subcategoryName = subcategorySnapshot.child("name").getValue(String.class);
//                String parentCategoryName = categorySnapshot.child("name").getValue(String.class);
//                double subcategoryPrice = subcategorySnapshot.child("price").getValue(Double.class);
//                if(Objects.equals(parentCategoryName, categoryNames.get(0)))
//                    subcategoryNames1.add(subcategoryName + " "+subcategoryPrice+ "€");
//                if(Objects.equals(parentCategoryName, categoryNames.get(1)))
//                    subcategoryNames2.add(subcategoryName + " "+subcategoryPrice+ "€");
//                if(Objects.equals(parentCategoryName, categoryNames.get(2)))
//                    subcategoryNames3.add(subcategoryName + " "+subcategoryPrice+ "€");
//            }
//        }
//        callback.onCategoriesLoaded(categoryNames);
//    }
//
//    private void applyRbDesign(RadioButton[] radioButtonsArray,ArrayList<String> StringArrayTest,RadioGroup radioGroup) {
//
//        radioGroup.removeAllViews();
//
//        for (int i = 0; i < radioButtonsArray.length; i++) {
//            radioButtonsArray[i] = new RadioButton(getApplicationContext());
//
//            //Set-up layout params and font params
//            radioButtonsArray[i].setLayoutParams(getLayoutParams());
//            radioButtonsArray[i].setTypeface(getTypeface(R.font.sen));
//
//            //Set-up background selector, disable Rb Button & set text properties
//            radioButtonsArray[i].setBackgroundResource(R.drawable.radio_selector);
//            radioButtonsArray[i].setButtonDrawable(R.drawable.radio_null_button);
//            radioButtonsArray[i].setTextColor(Color.BLACK);
//            radioButtonsArray[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//            radioButtonsArray[i].setTextSize(14);
//
//            //design
//            radioButtonsArray[i].setHeight(getPx(50));
//            radioButtonsArray[i].setElevation(getPx(4));
//
//            //apply testing array
//            radioButtonsArray[i].setText(StringArrayTest.get(i));
//            //add to radioGroup
//            radioGroup.addView(radioButtonsArray[i]);
//        }
//    }
//
//    public void getCategoriesFromDatabase(MyCallback callback) {
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(serviceProviderId).child("categories");
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    extractCategoryAndSubcategoryNames(dataSnapshot,callback);
//                } else {
//                    Log.d(TAG, "No categories found");
//                }
//
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e(TAG, "Failed to get categories from database", databaseError.toException());
//            }
//        });
//
//    }
//
//    private void addRadioButtonsToRadioGroup(RadioGroup radioGroup, RadioButton[] radioButtons) {
//        for(int i = 0; i < MAX_AMOUNT; i++)
//        {
//            radioGroup.addView(radioButtons[i]);
//        }
//
//    }
//
//    public void assignStringsToRadioButtons(RadioGroup radioGroup1, RadioGroup radioGroup2, RadioGroup radioGroup3, ArrayList<String> availableHours) {
//        int numStrings = availableHours.size();
//
//
//        int numRadioButtons1 = radioGroup1.getChildCount();
//        int numRadioButtons2 = radioGroup2.getChildCount();
//        int numRadioButtons3 = radioGroup3.getChildCount();
//        int i = 0;
//
//        // Assign strings to RadioButtons in RadioGroup1
//        for (int j = 0; j < numRadioButtons1; j++) {
//            if (i < numStrings) {
//                RadioButton radioButton = (RadioButton) radioGroup1.getChildAt(j);
//                radioButton.setText(availableHours.get(i));
//                i++;
//            }
//            if (i >= numStrings || j == numRadioButtons1 - 1) {
//                radioGroup1.setOnCheckedChangeListener(null); // Disable listener to prevent selection of additional RadioButtons
//                break;
//            }
//        }
//
//        // Assign remaining strings to RadioButtons in RadioGroup2
//        for (int j = 0; j < numRadioButtons2; j++) {
//            if (i < numStrings) {
//                RadioButton radioButton = (RadioButton) radioGroup2.getChildAt(j);
//                radioButton.setText(availableHours.get(i));
//                i++;
//            }
//            if (i >= numStrings || j == numRadioButtons2 - 1) {
//                radioGroup2.setOnCheckedChangeListener(null); // Disable listener to prevent selection of additional RadioButtons
//                break;
//            }
//        }
//
//        // Assign remaining strings to RadioButtons in RadioGroup3
//        for (int j = 0; j < numRadioButtons3; j++) {
//            if (i < numStrings) {
//                RadioButton radioButton = (RadioButton) radioGroup3.getChildAt(j);
//                radioButton.setText(availableHours.get(i));
//                i++;
//            }
//            if (i >= numStrings || j == numRadioButtons3 - 1) {
//                radioGroup3.setOnCheckedChangeListener(null); // Disable listener to prevent selection of additional RadioButtons
//                break;
//            }
//        }
//
//        // Handle case where string array has more than 12 strings
//        if (i < numStrings) {
//            // Alert user that not all strings could be assigned to RadioButtons
//            Toast.makeText(getApplicationContext(), "String array has more than 12 strings", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void populateOpeningHoursRadioGroup(RadioGroup radioGroup, ArrayList<String> openingHours) {
//        // Set up the GridLayout
//        GridLayout gridLayout = new GridLayout(this);
//        gridLayout.setColumnCount(4);
//        gridLayout.setRowCount((int) Math.ceil(openingHours.size() / 4.0));
//
//        // Create and add the RadioButton views
//        for (int i = 0; i < openingHours.size(); i++) {
//            RadioButton radioButton = new RadioButton(this);
//            radioButton.setText(openingHours.get(i));
//            radioButton.setId(i);
//            gridLayout.addView(radioButton);
//
//            // Set the click listener to handle selection of the RadioButton
//            radioButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int row = gridLayout.getRowCount();
//                    int col = gridLayout.getColumnCount();
//
//                    // Find the selected RadioButton
//                    RadioButton selectedRadioButton = (RadioButton) v;
//                    int selectedId = selectedRadioButton.getId();
//                    int selectedRow = selectedId / col;
//                    int selectedCol = selectedId % col;
//
//                    // Clear the check from all other RadioButtons
//                    for (int i = 0; i < row; i++) {
//                        for (int j = 0; j < col; j++) {
//                            View view = gridLayout.getChildAt(i * col + j);
//                            if (view instanceof RadioButton && view.getId() != selectedId) {
//                                ((RadioButton) view).setChecked(false);
//                            }
//                        }
//                    }
//                }
//            });
//        }
//
//        // Add the GridLayout to the RadioGroup
//        radioGroup.addView(gridLayout);
//    }
//
//    private RadioButton[] getRadioButtons()
//    {
//        RadioButton[] radioButtons = new RadioButton[MAX_AMOUNT];
//        for(int index = 0; index < MAX_AMOUNT; index++){
//            radioButtons[index] = new RadioButton(getApplicationContext());
//            radioButtons[index].setLayoutParams(getLayoutParamsForTime());
//            radioButtons[index].setTypeface(getTypeface(R.font.sen));
//            radioButtons[index].setBackgroundColor(Color.TRANSPARENT);
//            radioButtons[index].setButtonDrawable(R.drawable.radio_null_button);
//            radioButtons[index].setTextColor(Color.BLACK);
//        }
//        return radioButtons;
//    }
//
//    @Override
//    public void onBackPressed() {
//
//        if (currentStep > 1) {
//            currentStep--;
//            updateRadioGroupVisibility();
////            updateRequestTextView();
//        } else {
//            super.onBackPressed();
//        }
//
//    }
//
//    private void updateRadioGroupVisibility() {
//        updateRequestTextView();
//        switch (currentStep) {
//            case 1:
//                categoryRadioGroup.setVisibility(View.VISIBLE);
//                serviceRadioGroup.setVisibility(View.GONE);
//
//                timeFirstRadioGroup.setVisibility(View.GONE);
//                timeSecondRadioGroup.setVisibility(View.GONE);
//                timeThirdRadioGroup.setVisibility(View.GONE);
//
//                calendarView.setVisibility(View.GONE);
//                saveDateTextView.setVisibility(View.GONE);
//                break;
//            case 2:
//                categoryRadioGroup.setVisibility(View.GONE);
//                serviceRadioGroup.setVisibility(View.VISIBLE);
//
//                timeFirstRadioGroup.setVisibility(View.GONE);
//                timeSecondRadioGroup.setVisibility(View.GONE);
//                timeThirdRadioGroup.setVisibility(View.GONE);
//
//                calendarView.setVisibility(View.GONE);
//                saveDateTextView.setVisibility(View.GONE);
//
//                break;
//            case 3:
//                categoryRadioGroup.setVisibility(View.GONE);
//                serviceRadioGroup.setVisibility(View.GONE);
//
//                timeFirstRadioGroup.setVisibility(View.GONE);
//                timeSecondRadioGroup.setVisibility(View.GONE);
//                timeThirdRadioGroup.setVisibility(View.GONE);
//
//                calendarView.setVisibility(View.VISIBLE);
//                saveDateTextView.setVisibility(View.VISIBLE);
//
//                break;
//            case 4:
//                categoryRadioGroup.setVisibility(View.GONE);
//                serviceRadioGroup.setVisibility(View.GONE);
//
//                timeFirstRadioGroup.setVisibility(View.VISIBLE);
//                timeSecondRadioGroup.setVisibility(View.VISIBLE);
//                timeThirdRadioGroup.setVisibility(View.VISIBLE);
//
//                calendarView.setVisibility(View.GONE);
//                saveDateTextView.setVisibility(View.GONE);
//
//                break;
//        }
//    }
//
//    private void updateRequestTextView()
//    {
//        Drawable reservation_circle;
//        switch(currentStep)
//        {
//            case 1:
//                currentStepTextView.setText(R.string.choose_category);
//                reservation_circle = ResourcesCompat.getDrawable(getResources(),R.drawable.reservation_circle_one,getTheme());
//                currentStepTextView.setCompoundDrawablesWithIntrinsicBounds(reservation_circle,null,null,null);
//                break;
//            case 2:
//                currentStepTextView.setText(R.string.choose_service);
//                reservation_circle = ResourcesCompat.getDrawable(getResources(),R.drawable.reservation_circle_two,getTheme());
//                currentStepTextView.setCompoundDrawablesWithIntrinsicBounds(reservation_circle,null,null,null);
//                break;
//            case 3:
//                currentStepTextView.setText(R.string.choose_date);
//                reservation_circle = ResourcesCompat.getDrawable(getResources(),R.drawable.reservation_circle_three,getTheme());
//                currentStepTextView.setCompoundDrawablesWithIntrinsicBounds(reservation_circle,null,null,null);
//                break;
//            case 4:
//                currentStepTextView.setText(R.string.choose_time);
//                reservation_circle = ResourcesCompat.getDrawable(getResources(),R.drawable.reservation_circle_four,getTheme());
//                currentStepTextView.setCompoundDrawablesWithIntrinsicBounds(reservation_circle,null,null,null);
//                break;
//            default:
//                break;
//        }
//
//    }
//
//
//    private Animation FadeOut()
//    {
//        // Apply fade-out animation to the RadioGroup
//        Animation fadeOut = new AlphaAnimation(1, 0);
//        fadeOut.setDuration(1000);
//        fadeOut.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                // Do nothing
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                updateRadioGroupVisibility();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//                // Do nothing
//            }
//        });
//        return fadeOut;
//    }
//
//    private void initComponents()
//    {
//        currentStepTextView = findViewById(R.id.currentStepTextView);
//        saveDateTextView = findViewById(R.id.saveDateTextView);
//
//        categoryRadioGroup = findViewById(R.id.categoryRadioGroup);
//        serviceRadioGroup = findViewById(R.id.serviceRadioGroup);
//        timeFirstRadioGroup = findViewById(R.id.timeFirstRowRadioGroup);
//        timeSecondRadioGroup = findViewById(R.id.timeSecondRowRadioGroup);
//        timeThirdRadioGroup = findViewById(R.id.timeThirdRowRadioGroup);
//
//        calendarView = findViewById(R.id.cosmo_calendar);
//        profilePictureImageView = findViewById(R.id.backgroundImageView);
//        barberShopName = findViewById(R.id.usernameTextView);
//    }
//
//    private Typeface getTypeface(@FontRes int id)
//    {
//        //example id -> R.font.sen
//        return ResourcesCompat.getFont(getApplicationContext(), id);
//    }
//
//    private RadioGroup.LayoutParams getLayoutParamsForTime()
//    {
//        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
//                RadioGroup.LayoutParams.WRAP_CONTENT,
//                RadioGroup.LayoutParams.WRAP_CONTENT
//        );
//        params.setMargins(getPx(16), 0, getPx(16), 0);
//        params.weight = 1.0f;
//        params.gravity = Gravity.CENTER;
//
//        return params;
//    }
//
//    private RadioGroup.LayoutParams getLayoutParams()
//    {
//        //Layout params for radio buttons
//        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
//                getPx(270),
//                RadioGroup.LayoutParams.WRAP_CONTENT
//        );
//        params.setMargins(getPx(8), 0, getPx(8), getPx(16));
//        params.weight = 1.0f;
//        params.gravity = Gravity.CENTER;
//
//        return params;
//    }
//
//    private void setUpCalendar()
//    {
//        // Initially hide the CalendarView
//        calendarView.setVisibility(View.GONE);
//
//        //Set First day of the week
//        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
//
//        //Set Orientation 0 = Horizontal | 1 = Vertical
//        calendarView.setCalendarOrientation(0);
//
//        //add Weekends
//        calendarView.setWeekendDays(new HashSet() {{
//            add(Calendar.SATURDAY);
//            add(Calendar.SUNDAY);
//        }});
//
//        //Range Selection
//        calendarView.setSelectionType(SelectionType.SINGLE);
//
//        int orangeColor = Color.parseColor("#FF7235");
//
//        //Set palette-colors
//        setColoring(orangeColor);
//
//        // Disable all dates past today
//        disablePreviousDays();
//
//        calendarView.update();
//    }
//
//    private void disablePreviousDays()
//    {
//        Set<Long> disabledDays = CalendarUtils.getPreviousDays();
//        calendarView.setDisabledDays(disabledDays);
//    }
//
//    private void setColoring(int color)
//    {
//        calendarView.setWeekendDayTextColor(color);
//        calendarView.setSelectedDayBackgroundColor(color);
//        calendarView.setCurrentDayIconRes(R.drawable.red_dot);
//        calendarView.setDayTextColor(Color.BLACK);
//    }
//
//    public int getPx(int dp)
//    {
//        float scale = getResources().getDisplayMetrics().density;
//        return((int) (dp * scale + 0.5f));
//    }
//
//    private void selectCorrectTextColor(int checkedId,RadioGroup radioGroup)
//    {
//        for (int i = 0; i < radioGroup.getChildCount(); i++) {
//            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
//            if (radioButton.getId() == checkedId) {
//
//                //checking if radioGroup is for Time or not
//                if(radioGroup.getOrientation() == LinearLayout.HORIZONTAL)
//                    radioButton.setTextColor(Color.GRAY);
//                else
//                    radioButton.setTextColor(Color.WHITE);
//
//            } else {
//                radioButton.setTextColor(Color.BLACK);
//            }
//        }
//    }
//
//    private void fetchUser(String userId, MyCallback callback)
//    {
//        // Load the data from the database
//        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
//
//        // Add a listener to the reference to fetch the user data
//        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                if (dataSnapshot.exists()) {
//
//                    serviceProvider = dataSnapshot.getValue(User.class);
//
//                    assert serviceProvider != null;
//                    serviceProvider.setId(dataSnapshot.getKey());
//                    imageURL = serviceProvider.getProfile_picture();
//                    username = serviceProvider.getUsername();
//
//                    // Pass the data to the callback
//                    callback.onProfileDataLoaded(imageURL,username,null);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Failed to read value
////                Log.w("db_error", "Failed to read value.", error.toException());
//                Toast.makeText(getApplicationContext(),"db_fetching_error",Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//}

package com.btproject.barberise;

import static android.content.ContentValues.TAG;

import androidx.annotation.FontRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
//import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.applikeysolutions.cosmocalendar.utils.SelectionType;
import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.btproject.barberise.navigation.profile.User;
import com.btproject.barberise.utils.CalendarUtils;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
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

public class ChosenUser extends AppCompatActivity {

    private User serviceProvider;
    private String serviceProviderId,username,imageURL;

    private Uri imageUri;
    private ShapeableImageView profilePictureImageView;
    private TextView barberShopName;
    private String[] services,categories,times;
    private final int MAX_AMOUNT = 4;
    private int currentStep = 1;
    private RadioButton[] radioButtonsArrayCategory,radioButtonArrayServices;
    private RadioButton[] timesRadioButtonUpper,timesRadioButtonMiddle,timesRadioButtonBottom;
    private TextView currentStepTextView,saveDateTextView;
    private CalendarView calendarView;
    private RadioGroup categoryRadioGroup,serviceRadioGroup,
            timeFirstRadioGroup,timeSecondRadioGroup,timeThirdRadioGroup;
    private ArrayList<String> categoryNames,subcategoryNames1, subcategoryNames2, subcategoryNames3;
    private String selectedText;
    private Map<String, Map<String, Boolean>> openingHours = new HashMap<>();
    private String day;

    RadioGroup opening_hour_radio_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosen_user);
        profilePictureImageView = findViewById(R.id.backgroundImageView);
        barberShopName = findViewById(R.id.usernameTextView);
        opening_hour_radio_group = findViewById(R.id.opening_hour_radio_group);

        serviceProviderId = getIntent().getStringExtra("id");

        // Set up the callback that will be called when the data is available
        MyCallback callback = new MyCallback() {
            @Override
            public void onProfileDataLoaded(String imageURL, String username, Bitmap imageBitmap) {

                // Update the UI with the imageURL
                Glide.with(profilePictureImageView.getContext())
                        .load(imageURL)
                        .into(profilePictureImageView);

                barberShopName.setText(username);
            }

            @Override
            public void onCategoriesLoaded(ArrayList<String> categoryNames) {

            }

            @Override
            public void onHoursLoaded() {

            }
        };
        // Load the data and pass the callback to the method
        fetchUser(serviceProviderId,callback);

        initComponents();

        updateRadioGroupVisibility();
        setUpCalendar();



        // Set up the callback that will be called when the data is available
        MyCallback callback2 = new MyCallback() {
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

        };
        getCategoriesFromDatabase(callback2);

        // First-step// First-step// First-step// First-step// First-step// First-step// First-step// First-step
        categoryRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            currentStep++;
            selectCorrectTextColor(checkedId,categoryRadioGroup);

            RadioButton selectedRadioButton = findViewById(checkedId);
            selectedText = selectedRadioButton.getText().toString();

            expandCategory(selectedText);

            categoryRadioGroup.startAnimation(
                    FadeOut()
            );

        });
        //First-step// First-step// First-step// First-step// First-step// First-step// First-step// First-step// First-step

        // Second-step
        serviceRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            currentStep++;
            selectCorrectTextColor(checkedId,serviceRadioGroup);

            serviceRadioGroup.startAnimation(
                    FadeOut()
            );
        });



        saveDateTextView.setOnClickListener(view -> {
            currentStep++;
            updateRadioGroupVisibility();

            List<Calendar> selectedDates = calendarView.getSelectedDates();

            // Iterate over the selected dates
            for (Calendar selectedDate : selectedDates) {
//                Toast.makeText(getApplicationContext(), day, Toast.LENGTH_SHORT).show();
                day = CalendarUtils.getDay(selectedDate);
                calendarView.update();
            }

            MyCallback callback3 = new MyCallback() {
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
            };
            getHoursFromDatabase(callback3);

        });
    }

//    private void populateOpeningHoursRadioGroup(RadioGroup radioGroup, ArrayList<String> openingHours) {
//        // Set up the GridLayout
//        GridLayout gridLayout = new GridLayout(this);
//        gridLayout.setColumnCount(4);
//        gridLayout.setRowCount((int) Math.ceil(openingHours.size() / 4.0));
//
//        // Create and add the RadioButton views
//        for (int i = 0; i < openingHours.size(); i++) {
//            RadioButton radioButton = getRadioButton();
//            radioButton.setText(openingHours.get(i));
//            radioButton.setId(i);
//            gridLayout.addView(radioButton);
//
//            // Set the click listener to handle selection of the RadioButton
//            radioButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int row = gridLayout.getRowCount();
//                    int col = gridLayout.getColumnCount();
//
//                    // Find the selected RadioButton
//                    RadioButton selectedRadioButton = (RadioButton) v;
//                    int selectedId = selectedRadioButton.getId();
//                    int selectedRow = selectedId / col;
//                    int selectedCol = selectedId % col;
//                    selectedRadioButton.setTextColor(Color.GRAY);
//
//                    // Clear the check from all other RadioButtons
//                    for (int i = 0; i < row; i++) {
//                        for (int j = 0; j < col; j++) {
//                            View view = gridLayout.getChildAt(i * col + j);
//                            if (view instanceof RadioButton && view.getId() != selectedId) {
//                                ((RadioButton) view).setChecked(false);
//                            }
//                        }
//                    }
//                }
//            });
//        }
//
//        // Add the GridLayout to the RadioGroup
//        radioGroup.addView(gridLayout);
//    }

    private void populateOpeningHoursRadioGroup(RadioGroup radioGroup, ArrayList<String> openingHours) {
        // Set up the GridLayout
        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(4);
        gridLayout.setRowCount((int) Math.ceil(openingHours.size() / 4.0));

        // Create and add the RadioButton views
        for (int i = 0; i < openingHours.size(); i++) {
            RadioButton radioButton = getRadioButton();
            radioButton.setText(openingHours.get(i));
            radioButton.setId(i);
            gridLayout.addView(radioButton);

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
                }
            });
        }

        // Add the GridLayout to the RadioGroup
        radioGroup.addView(gridLayout);
    }


    private ArrayList<String> getAvailableHours(String day)
    {
        // Retrieve opening hours for Monday
        Map<String, Boolean> dayHoursRetrieved = openingHours.get(day);

        // Create an ArrayList to store the opening hours
        ArrayList<String> availableHours = new ArrayList<>();

        // Loop through the keys in the map (which are the opening hours)
        assert dayHoursRetrieved != null;

        for (String hour : dayHoursRetrieved.keySet()) {
            // if the store is open during the corresponding time slot
            if (Boolean.TRUE.equals(dayHoursRetrieved.get(hour))) {
                availableHours.add(hour);
            }
        }

        // Sort the opening hours list
        Collections.sort(availableHours);

        return availableHours;
    }

    private void getHoursFromDatabase(MyCallback callback)
    {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(serviceProviderId).child("opening_hours");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    // Parse the opening hours data into a Map<String, Map<String, Boolean>> object
                    for (DataSnapshot daySnapshot : snapshot.getChildren()) {
                        String day = daySnapshot.getKey();
                        Map<String, Boolean> hoursMap = new HashMap<>();
                        for (DataSnapshot hourSnapshot : daySnapshot.getChildren()) {
                            String hour = hourSnapshot.getKey();
                            Boolean isOpen = hourSnapshot.getValue(Boolean.class);
                            hoursMap.put(hour, isOpen);
                        }
                        openingHours.put(day, hoursMap);
                    }
                    callback.onHoursLoaded();
                    // Use the openingHoursMap here...
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
        radioButton.setLayoutParams(getLayoutParamsForTime());
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
                break;
            case 2:
                categoryRadioGroup.setVisibility(View.GONE);
                serviceRadioGroup.setVisibility(View.VISIBLE);

                opening_hour_radio_group.setVisibility(View.GONE);


                calendarView.setVisibility(View.GONE);
                saveDateTextView.setVisibility(View.GONE);

                break;
            case 3:
                categoryRadioGroup.setVisibility(View.GONE);
                serviceRadioGroup.setVisibility(View.GONE);

                opening_hour_radio_group.setVisibility(View.GONE);

                calendarView.setVisibility(View.VISIBLE);
                saveDateTextView.setVisibility(View.VISIBLE);

                break;
            case 4:
                categoryRadioGroup.setVisibility(View.GONE);
                serviceRadioGroup.setVisibility(View.GONE);

                opening_hour_radio_group.setVisibility(View.VISIBLE);

                calendarView.setVisibility(View.GONE);
                saveDateTextView.setVisibility(View.GONE);

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
        currentStepTextView = findViewById(R.id.currentStepTextView);
        saveDateTextView = findViewById(R.id.saveDateTextView);

        categoryRadioGroup = findViewById(R.id.categoryRadioGroup);
        serviceRadioGroup = findViewById(R.id.serviceRadioGroup);
        timeFirstRadioGroup = findViewById(R.id.timeFirstRowRadioGroup);
        timeSecondRadioGroup = findViewById(R.id.timeSecondRowRadioGroup);
        timeThirdRadioGroup = findViewById(R.id.timeThirdRowRadioGroup);

        calendarView = findViewById(R.id.cosmo_calendar);
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
        params.setMargins(getPx(16), 0, getPx(16), getPx(32));
        params.weight = 1.0f;
        params.gravity = Gravity.CENTER;

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

    private void fillTestArrays()
    {
        //Testing array of strings
        categories = new String[3];
        categories[0] = "Brada";
        categories[1] = "Vlasy";
        categories[2] = "Komplet";

        services = new String[3];
        services[0] = "Strih dlhe vlasy";
        services[1] = "Strih kratke vlasy";
        services[2] = "Strih vlasy + brada";

        times = new String[16];
        times[0] = "9:30";
        times[1] = "10:00";
        times[2] = "10:30";
        times[3] = "11:00";
        times[4] = "12:00";
        times[5] = "13:00";
        times[6] = "13:00";
        times[7] = "13:00";
        times[8] = "13:00";
        times[10] = "13:00";
        times[11] = "13:00";
        times[12] = "13:00";
        times[13] = "13:00";
        times[14] = "13:00";
        times[15] = "13:00";
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