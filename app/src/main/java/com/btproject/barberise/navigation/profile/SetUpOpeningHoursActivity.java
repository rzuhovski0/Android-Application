package com.btproject.barberise.navigation.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.btproject.barberise.R;
import com.btproject.barberise.reservation.ReservationActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SetUpOpeningHoursActivity extends AppCompatActivity{


    private int openingHour, openingMinute, closingHour, closingMinute;

    private TextView mondayEditText,tuesdayEditText,wednesdayEditText,
            thursdayEditText,fridayEditText,saturdayEditText,sundayEditText;

    private TextView saveChangesTextView;

    private HashMap<String, ArrayList<Integer>> hours = new HashMap<>();

    private ArrayList<Pair<Integer,Integer>> hoursFromUser = new ArrayList<>();

    private CheckBox checkBox;
    private boolean useDefaultHours = false;
    private ArrayList<TextView> openingHoursTextArray = new ArrayList<>();

    private ArrayList<String> daysName = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_opening_hours);

        // Init TextView, fill stringDays array
        initUIOpeningHours();

        // If user already defined some openingHours, load them from static HashMap
        loadOpeningHours();

        // Set time dialogs
        setTimePickerForTextViews();


        saveChangesTextView.setOnClickListener(view -> {
            /**User wants to use default hours*/
            if(useDefaultHours)
            {
                setDefaultTimeOnTextViews();
                setDefaultTimeToMap();
                RegistrationActivity.hoursConfigured = true;
                onBackPressed();
            }else{ /** Custom hours configured*/

                boolean daysCorrectlyConfigured = true;
                /** Check if all days have correctly configured opening hours*/
                for(String day : daysName)
                {
                    if(RegistrationActivity.openingHours.get(day) == null) {
                        daysCorrectlyConfigured = false;
                        break;
                    }
                }
                if(daysCorrectlyConfigured) {
                    RegistrationActivity.hoursConfigured = true;
                    onBackPressed();
                }else
                    Toast.makeText(getApplicationContext(),R.string.check_hours,Toast.LENGTH_LONG).show();
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Do something when the checkbox is checked
                    Toast.makeText(getApplicationContext(),R.string.default_time_warning,Toast.LENGTH_SHORT).show();
                    useDefaultHours = true;
                } else {
                    // Do something when the checkbox is unchecked
                    useDefaultHours = false;
                }
            }
        });

    }

    private  void setDefaultTimeToMap()
    {
        for(String day : daysName)
        {
            ArrayList<String> openingHours = getReservationSlots(9,17);
            RegistrationActivity.openingHours.put(day,openingHours);
        }
    }

    private void setDefaultTimeOnTextViews()
    {
        int counter = 0;
        for(TextView textView : openingHoursTextArray)
        {
            if(counter < 6)
                textView.setText(R.string.Monday_hours);
            else
                textView.setText(R.string.Saturday_hours);
            counter++;
        }
    }

    private void loadOpeningHours()
    {
        Map<String,ArrayList<String>> openingHoursClone = RegistrationActivity.openingHours;
        if(openingHoursClone == null)
            return;

        int counter = 0;
        for(String day : daysName)
        {
            ArrayList<String> openingHours = openingHoursClone.get(day);

            if(openingHours != null) {
                int lastIndex = openingHours.size();
                String openingHoursString = openingHours.get(0) + " - " + openingHours.get(lastIndex - 1);
                openingHoursTextArray.get(counter).setText(openingHoursString);
                counter++;
            }
        }
    }

    private void setTimePickerForTextViews()
    {
        mondayEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(mondayEditText,"Monday");
            }
        });

        tuesdayEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(tuesdayEditText,"Tuesday");
            }
        });

        wednesdayEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(wednesdayEditText,"Wednesday");
            }
        });

        thursdayEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(thursdayEditText,"Thursday");
            }
        });

        fridayEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(fridayEditText,"Friday");
            }
        });

        saturdayEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(saturdayEditText,"Saturday");
            }
        });

        sundayEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(sundayEditText,"Sunday");
            }
        });
    }

    private void showTimePickerDialog(TextView textView,String day) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(SetUpOpeningHoursActivity.this,
                (view, hourOfDay, minute) -> {

                    openingHour = hourOfDay;
                    openingMinute = minute;

                    showClosingTimePickerDialog(textView,openingHour,day);
                }, 12, 0, true);
        timePickerDialog.show();
    }

    private void showClosingTimePickerDialog(TextView textView,Integer openingHour,String dayString) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(SetUpOpeningHoursActivity.this,
                (view, hourOfDay, minute) -> {

                    closingHour = hourOfDay;
                    closingMinute = minute;

                    if (isValidTimeRange()) {

                        RegistrationActivity.openingHours.put(dayString,getReservationSlots(openingHour,closingHour));

                        updateTextView(textView);
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid time range", Toast.LENGTH_SHORT).show();
                    }

                }, 12, 0, true);
        timePickerDialog.show();
    }

    private ArrayList<String> getReservationSlots(int openingHour,int closingHour)
    {
        ArrayList<String> reservationSlots = new ArrayList<>();

        for (int hour = openingHour; hour < closingHour; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                String time = String.format("%02d:%02d", hour, minute);
                reservationSlots.add(time);
            }
        }
        return reservationSlots;
    }

    private boolean isValidTimeRange()
    {
        if (openingHour < closingHour) {
            return true;
        } else if (openingHour == closingHour && openingMinute < closingMinute) {
            return true;
        } else {
            return false;
        }
    }

    private void updateTextView(TextView textView)
    {
        String openingTime = String.format(Locale.getDefault(), "%02d:%02d", openingHour, openingMinute);
        String closingTime = String.format(Locale.getDefault(), "%02d:%02d", closingHour, closingMinute);

        String timeRange = openingTime + " - " + closingTime;
        textView.setText(timeRange);
    }

    // Init each textView and add it into array
    private void initUIOpeningHours()
    {
        checkBox = findViewById(R.id.checkBox);

        mondayEditText = findViewById(R.id.mondayOpeningHoursTextView);
        openingHoursTextArray.add(mondayEditText);
        daysName.add("Monday");

        tuesdayEditText = findViewById(R.id.tuesdayOpeningHoursTextView);
        openingHoursTextArray.add(tuesdayEditText);
        daysName.add("Tuesday");

        wednesdayEditText = findViewById(R.id.wednesdayOpeningHoursTextView);
        openingHoursTextArray.add(wednesdayEditText);
        daysName.add("Wednesday");

        thursdayEditText = findViewById(R.id.thursdayOpeningHoursTextView);
        openingHoursTextArray.add(thursdayEditText);
        daysName.add("Thursday");

        fridayEditText = findViewById(R.id.fridayOpeningHoursTextView);
        openingHoursTextArray.add(fridayEditText);
        daysName.add("Friday");

        saturdayEditText = findViewById(R.id.saturdayOpeningHoursTextView);
        openingHoursTextArray.add(saturdayEditText);
        daysName.add("Saturday");

        sundayEditText = findViewById(R.id.sundayOpeningHoursTextView);
        openingHoursTextArray.add(sundayEditText);
        daysName.add("Sunday");

        saveChangesTextView = findViewById(R.id.saveChangesTextView);
    }


}