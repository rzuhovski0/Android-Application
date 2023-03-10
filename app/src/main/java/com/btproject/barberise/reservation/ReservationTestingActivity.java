package com.btproject.barberise.reservation;

import static com.btproject.barberise.utils.CalendarUtils.getDisabledDates;
import static com.btproject.barberise.utils.CalendarUtils.getDays;
import static com.btproject.barberise.utils.CalendarUtils.setUpCalendar;
import static com.btproject.barberise.utils.CategoryUtils.getSubcategoriesFromCategories;
import static com.btproject.barberise.utils.CategoryUtils.getSubcategoryPriceString;
import static com.btproject.barberise.utils.DatabaseUtils.addUserToFavorites;
import static com.btproject.barberise.utils.DatabaseUtils.removeUserFromFavorites;
import static com.btproject.barberise.utils.LayoutUtils.getEmptyButton;
import static com.btproject.barberise.utils.LayoutUtils.getGridLayoutParams;
import static com.btproject.barberise.utils.LayoutUtils.prettifyButton;
import static com.btproject.barberise.utils.OpeningHoursUtils.getAvailableHours;
import static com.btproject.barberise.utils.ReservationUtils.getReservationsFromDatabase;
import static com.btproject.barberise.utils.ReservationUtils.selectCorrectTextColor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.btproject.barberise.R;
import com.btproject.barberise.SplashActivity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

public class ReservationTestingActivity extends AppCompatActivity {

    private int currentStep = 1;
    private User barberShop;
    private String barberShopId;
    /** UI Components*/
    private ShapeableImageView profilePictureImageView;
    private TextView barberShopName;
    private TextView currentStepTextView,saveDateTextView;
    private CalendarView calendarView;
    private RadioGroup categories_radio_group, subcategories_radio_group;
    private RadioGroup opening_hour_radio_group;
    private Button reserveButton;

    /**Data lists*/
    private ArrayList<Category> categories;
    private ArrayList<HashMap<String,ArrayList<Subcategory>>> subcategories;
    private HashMap<String,ArrayList<String>> openingHours;
    private ArrayList<Reservation> reservationsArray;
    private HashMap<String,Object> reservationsMap;

    // Helper array
    private ArrayList<String> finalAvailableHours;

    /** Reservation */
    private Reservation reservation;
    private String selectedDayString;

    /** RadioButtons[] */
    private RadioButton[] radioButtonsArrayCategory,radioButtonArrayServices;

    /**For Layout utils*/
    private Resources resources;
    private Context context;

    /**Filtering*/
    ArrayList<Day> allDays;

    /**TextViews for ContactInfo & AboutUs*/
    private TextView ContactInfoTextView;
    private TextView AboutUsTextView;

    private TextView addToFavTextView;

    private void setContactInfoAboutUsListeners()
    {
        AboutUsTextView.setOnClickListener(view -> {
            Intent intent = new Intent(ReservationTestingActivity.this,AboutUsActivity.class);
            startActivity(intent);
            /**Without finish() to not close current Activity*/
        });

        ContactInfoTextView.setOnClickListener(view -> {
            Intent intent = new Intent(ReservationTestingActivity.this,ContactInfoActivity.class);

            Bundle userInfoBundle = getUserInfoBundle();
//
            if(!userInfoBundle.isEmpty()) {
                intent.putExtras(userInfoBundle);
                startActivity(intent);
            }
        });

        addToFavTextView.setOnClickListener(view -> {
            if(alreadyIsAdded) {
                removeUserFromFavorites(barberShopId);
                alreadyIsAdded = false;
                setHeartIcon(false);
            } else {
                addUserToFavorites(barberShop, barberShopId, getApplicationContext());
                alreadyIsAdded = true;
                setHeartIcon(true);
            }
        });
    }

    private void isUserInFav()
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Get a reference to the current user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Check if the user is authenticated
        if (currentUser != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("clients").child(currentUser.getUid())
                    .child("favorites").child(barberShopId);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean dataExists = dataSnapshot.exists();
                    if (dataExists) {
                        // Data exists in the database
                        setHeartIcon(true);
                    } else {
                        // Data does not exist in the database
                        setHeartIcon(false);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle errors here
                }
            });
        }
    }

    private void setHeartIcon(boolean isAddedToFavorites)
    {
        if (isAddedToFavorites) {
            addToFavTextView.setBackgroundResource(R.drawable.heart_fill);
            alreadyIsAdded = true;
        } else {
            addToFavTextView.setBackgroundResource(R.drawable.heart_orange);
            alreadyIsAdded = false;
        }
    }

    private boolean alreadyIsAdded = false;

    private String performLongOperation() {
        DataFetchCallback callback = new DataFetchCallback() {
            @Override
            public void onDataLoaded(User barberShop) {
                updateUIComponents(barberShop);


                categories = barberShop.getCategories();
                subcategories = getSubcategoriesFromCategories(categories);
                openingHours = barberShop.getOpeningHours();
                reservationsMap = barberShop.getReservations();

                /**Apply desired size on RadioButtons for category*/
                radioButtonsArrayCategory = new RadioButton[categories.size()];

                /**Apply categories to radioButtons*/
                getRadioButtonsReady(radioButtonsArrayCategory,categories,categories_radio_group);

//                reservations = barberShop.getReservations();
                //TODO implement reservations
                reservation = new Reservation("Filip Rzuhovsky",barberShop.getUsername());
                reservation.setProfilePicture(barberShop.getProfile_picture());
                reservation.setServiceProviderId(barberShopId);

                /**Add listeners for ContactInfo & AboutUs*/
                setContactInfoAboutUsListeners();

                /**Check if the barber is already in favorites*/
                isUserInFav();

                /**Start the reservation process if all the attributes are loaded*/
                createReservationSession();


            }
            @Override
            public void onReservationsLoaded(ArrayList<Reservation> reservations) {

                /**Get days which are fully reserved*/
                allDays = getDays(reservations,openingHours);

                /**Get dates in Long format from all days which are fully reserved*/
                TreeSet<Long> disabledDates = new TreeSet<>();
                disabledDates = getDisabledDates(allDays);

                // Disable all dates past today
                CalendarUtils.disablePreviousDays(calendarView,disabledDates);
            }
        };
        getUserFromDatabase(callback);

        /**Initially, the reservations had to be fetched from User, however, the only possible options to get them from User.class afterwards
         * would be in HashMap format, with key value of reservation Id. Since the Id is randomly generated, there is no way to get any values
         * from HashMap using keys. Thus, the only option is to fetch the reservations again (even though they are already fetched) and using
         * datasnapshot.getChildren() method to fill the ArrayList<Reservation> reservations from children of the node.
         */
        getReservationsFromDatabase(callback,barberShopId,reservationsArray);
        return "Result of long operation";
    }

    private void updateUI(String result) {
        // update your UI here with the result
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_testing);

        barberShopId = getIntent().getStringExtra("id");
        resources = getResources();
        context = getApplicationContext();

        /**Initialize UI components*/
        initUIComponents();

        /**Initialize lists*/
        initLists();

        /**Set iInitial visibility*/
        updateRadioGroupVisibility();

        // create a new thread for the long operation
        new Thread(() -> {
            // perform the long operation here
            final String result = performLongOperation();

            // update the UI on the main thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // update the UI here with the result
                    updateUI(result);
                }
            });
        }).start();



        reserveButton.setOnClickListener(view -> {

            if(reservation.reservationValid())
                try {
                    makeReservationWithAuth(reservation);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"Error saving reservation: " + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            else{
                Toast.makeText(getApplicationContext(),"Please make sure that all reservation fields are valid",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private Bundle getUserInfoBundle()
    {
        Bundle bundle = new Bundle();

        bundle.putSerializable("openingHours",openingHours);
        bundle.putString("email","+421 09 999 999");
        bundle.putString("phone","barbercontact@gmail.com");
        bundle.putString("address","Alžbetina 9,Košice");
        return bundle;
    }



    private void makeReservationWithAuth(Reservation reservation)
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Get a reference to the current user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Check if the user is authenticated
        if (currentUser != null) {
            // User is authenticated, so we can save the reservation
            DatabaseReference barberRef = FirebaseDatabase.getInstance().getReference().child("users").child(barberShopId);
            DatabaseReference barberResRef = barberRef.child("reservations");

            // Create a new child node with a unique ID generated by push() and get its key
            String reservationId = barberResRef.push().getKey();

            reservation.setId(reservationId);

            /**Store to barber database*/
            assert reservationId != null;
            barberResRef.child(reservationId).setValue(reservation).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Reservation saved successfully
                    Toast.makeText(getApplicationContext(), "Reservation saved successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ReservationTestingActivity.this, ReservationSuccessfulActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Reservation saving failed
                    Toast.makeText(getApplicationContext(), "Error saving reservation: "
                            + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            /**Store to user database*/
            DatabaseReference userResRef = FirebaseDatabase.getInstance().getReference().child("clients").child(currentUser.getUid())
                    .child("reservations").child(reservationId);

            userResRef.setValue(reservation);
            //TODO implement check for user storing

        } else {
            // User is not authenticated, so we cannot save the reservation
            Toast.makeText(this, "You must be logged in to make a reservation", Toast.LENGTH_SHORT).show();
        }
    }

    private void createReservationSession()
    {
        /**First step -> Get user choice of category*/
        requestCategoryFromUser();

        /**Second step -> Get user choice of subcategory*/
        requestSubcategoryFromUser();

        /**Third step -> Get user choice of date*/
        requestDateFromUser();
    }

    @Override
    public void onBackPressed()
    {
        if (currentStep > 1) {
            currentStep--;
            updateRadioGroupVisibility();
        } else {
            super.onBackPressed();
        }

    }
    /**Requesters for Reservation attributes*/
    private void requestCategoryFromUser()
    {
        categories_radio_group.setOnCheckedChangeListener((group, checkedId) -> {
            currentStep++;
            selectCorrectTextColor(checkedId,categories_radio_group);

            RadioButton selectedRadioButton = findViewById(checkedId);
            String categoryKey = selectedRadioButton.getText().toString();

            expandCategory(categoryKey);

            /**RESERVATION Category*/
            reservation.setCategoryName(categoryKey);

            categories_radio_group.startAnimation(
                    FadeOut()
            );

        });
    }
    private void requestSubcategoryFromUser()
    {
        // Second-step
        subcategories_radio_group.setOnCheckedChangeListener((group, checkedId) -> {
            currentStep++;
            selectCorrectTextColor(checkedId,subcategories_radio_group);

            RadioButton selectedRadioButton = findViewById(checkedId);
            String selectedSubcategory = selectedRadioButton.getText().toString();

            /**RESERVATION SubCategory*/
            reservation.setSubcategoryName(selectedSubcategory);

            /**RESERVATION Price*/
            reservation.setPrice(getSubcategoryPriceString(categories,reservation.getCategoryName(),selectedSubcategory));

            //TODO implement storing subcategory description

            subcategories_radio_group.startAnimation(
                    FadeOut()
            );
        });
    }
    private void requestDateFromUser()
    {
        saveDateTextView.setOnClickListener(view -> {
            currentStep++;
            updateRadioGroupVisibility();

            List<Calendar> selectedDates = calendarView.getSelectedDates();

            // Iterate over the selected dates
            for (Calendar selectedDate : selectedDates) {

                // Method to get a dd.MM.yyyy format from milliseconds
                String dateStringFormat = CalendarUtils.getDateInString(selectedDate);

                /** Store date in dd-MM-yyyy format*/
                reservation.setDate(dateStringFormat);

                /** Save selected date in milliseconds format*/
                reservation.setTimeInMillisecondsByDate(dateStringFormat);

                selectedDayString = CalendarUtils.getDay(selectedDate);

                /** Store Day to reservation*/
                reservation.setDay(selectedDayString);


                /**Fourth step -> Get user choice of time*/
                /**Request TIME only if date is selected, otherwise selectedDayString = null*/
                requestTimeFromUser();
            }
        });
    }
    private void requestTimeFromUser()
    {
        opening_hour_radio_group.removeAllViews();
        finalAvailableHours = getAvailableHours(selectedDayString,openingHours,reservation,allDays);
        populateOpeningHoursRadioGroup(opening_hour_radio_group,finalAvailableHours);
        updateRadioGroupVisibility();
    }

    private void populateOpeningHoursRadioGroup(RadioGroup radioGroup, ArrayList<String> openingHours) {

        // Set up the GridLayout
        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(5);
        gridLayout.setRowCount((int) Math.ceil(openingHours.size() / 5.0));

        // Create and add the RadioButton views
        for (int i = 0; i < openingHours.size(); i++) {
            RadioButton radioButton = getEmptyButton(context);
            radioButton.setText(openingHours.get(i));
            radioButton.setId(i);
            gridLayout.addView(radioButton, getGridLayoutParams(resources));

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
                    selectedRadioButton.setTextColor(getResources().getColor(R.color.orange, getTheme()));

                    /** RESERVATION Time*/
                    reservation.setTime(selectedRadioButton.getText().toString());
                }
            });
        }

        // Add the GridLayout to the RadioGroup
        radioGroup.addView(gridLayout);

    }

    private void getRadioButtonsReady(RadioButton[] radioButtonsArray, ArrayList<? extends Options> options, RadioGroup radioGroup)
    {

        //Remove all view if going forth and back in current level of reservation process -> to clean up subcategories radioButtons
        radioGroup.removeAllViews();

        int counter = 0;
        for(RadioButton radioButton : radioButtonsArray){
            /**Apply margin, design, font etc*/
            radioButton = prettifyButton(context,resources);

            /**Apply text from available option*/
            radioButton.setText(options.get(counter).getName());

            radioGroup.addView(radioButton);
            counter++;
        }

    }

    private void expandCategory(String selectedText)
    {
        if(selectedText.equals(categories.get(0).getName())) {

            ArrayList<Subcategory> subcategories = categories.get(0).getSubcategories();

            radioButtonArrayServices = new RadioButton[subcategories.size()];
            getRadioButtonsReady(radioButtonArrayServices, subcategories, subcategories_radio_group);
        }
        else if(selectedText.equals(categories.get(1).getName())) {
            ArrayList<Subcategory> subcategories = categories.get(1).getSubcategories();

            radioButtonArrayServices = new RadioButton[subcategories.size()];
            getRadioButtonsReady(radioButtonArrayServices, subcategories, subcategories_radio_group);
        }
        else if(selectedText.equals(categories.get(2).getName())) {
            ArrayList<Subcategory> subcategories = categories.get(2).getSubcategories();

            radioButtonArrayServices = new RadioButton[subcategories.size()];
            getRadioButtonsReady(radioButtonArrayServices, subcategories, subcategories_radio_group);
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





    private void getUserFromDatabase(DataFetchCallback callback)
    {
        // Load the data from the database
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(barberShopId);

        // Add a listener to the reference to fetch the user data
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.exists()) {
                    barberShop = dataSnapshot.getValue(User.class);
                    // Pass the data to the callback
                    callback.onDataLoaded(barberShop);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Toast.makeText(getApplicationContext(),"db_fetching_error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUIComponents(User barberShop)
    {
        String ImageUrl = barberShop.getProfile_picture();
        String userName = barberShop.getUsername();

        // Update the UI with the imageURL
        Glide.with(profilePictureImageView.getContext())
                .load(ImageUrl)
                .into(profilePictureImageView);

        barberShopName.setText(userName);
        //TODO address.setText
    }

    private void initUIComponents()
    {
        //Default components
        profilePictureImageView = findViewById(R.id.backgroundImageView);
        barberShopName = findViewById(R.id.usernameTextView);
        //TODO add address -> (update User.class)

        //RadioGroups
        opening_hour_radio_group = findViewById(R.id.opening_hour_radio_group);
        categories_radio_group = findViewById(R.id.categoryRadioGroup);
        subcategories_radio_group = findViewById(R.id.serviceRadioGroup);

        //Process components
        currentStepTextView = findViewById(R.id.currentStepTextView);
        saveDateTextView = findViewById(R.id.saveDateTextView);
        reserveButton = findViewById(R.id.reserveButton);

        //CosmoCalendarView
        calendarView = findViewById(R.id.cosmo_calendar);

        //AboutUs & ContactInfo
        AboutUsTextView = findViewById(R.id.aboutUserTextView);
        ContactInfoTextView = findViewById(R.id.contactInfoTextView);
        addToFavTextView = findViewById(R.id.addToFavTextView);
    }

    private void initLists()
    {
        categories = new ArrayList<>();
        subcategories = new ArrayList<>();
        openingHours = new HashMap<>();
        reservationsArray = new ArrayList<>();
        reservationsMap = new HashMap<>();
        allDays = new ArrayList<>();

        //Helper
        finalAvailableHours = new ArrayList<>();
    }

    private void updateRadioGroupVisibility()
    {
        /**If no hours are available, this is helper TextView*/
        TextView no_hours_availableTextView = findViewById(R.id.no_hours_availableTextView);

        updateRequestTextView();
        switch (currentStep) {
            case 1:
                categories_radio_group.setVisibility(View.VISIBLE);
                subcategories_radio_group.setVisibility(View.GONE);

                opening_hour_radio_group.setVisibility(View.GONE);

                calendarView.setVisibility(View.GONE);
                saveDateTextView.setVisibility(View.GONE);
                reserveButton.setVisibility(View.GONE);

                break;
            case 2:
                categories_radio_group.setVisibility(View.GONE);
                subcategories_radio_group.setVisibility(View.VISIBLE);

                opening_hour_radio_group.setVisibility(View.GONE);


                calendarView.setVisibility(View.GONE);
                saveDateTextView.setVisibility(View.GONE);
                reserveButton.setVisibility(View.GONE);


                break;
            case 3:
                categories_radio_group.setVisibility(View.GONE);
                subcategories_radio_group.setVisibility(View.GONE);

                opening_hour_radio_group.setVisibility(View.GONE);

                calendarView.setVisibility(View.VISIBLE);
                saveDateTextView.setVisibility(View.VISIBLE);
                reserveButton.setVisibility(View.GONE);

                /**Hide helper TextView*/
                no_hours_availableTextView.setVisibility(View.GONE);

                break;
            case 4:
                categories_radio_group.setVisibility(View.GONE);
                subcategories_radio_group.setVisibility(View.GONE);

                opening_hour_radio_group.setVisibility(View.VISIBLE);

                calendarView.setVisibility(View.GONE);
                saveDateTextView.setVisibility(View.GONE);
                reserveButton.setVisibility(View.VISIBLE);

                /**If available hours after all filtration are empty, set visibility VISIBLE for no_hours_availableTextView*/
                if(finalAvailableHours.isEmpty()){
                    no_hours_availableTextView.setText(R.string.no_hours_available);
                    no_hours_availableTextView.setVisibility(View.VISIBLE);
                    reserveButton.setVisibility(View.GONE);
                }else{
                    no_hours_availableTextView.setVisibility(View.GONE);
                    reserveButton.setVisibility(View.VISIBLE);
                }

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
}