package com.btproject.barberise;

import androidx.annotation.FontRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.applikeysolutions.cosmocalendar.utils.SelectionType;
import com.applikeysolutions.cosmocalendar.view.CalendarView;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

public class ChosenUserTestingActivity extends AppCompatActivity {

    //Displays current request banner (Category,Date,Time..)
    TextView currentStepTextView,saveDateTextView;
    TextView debugTV;

    //Radio-group for RadioButtons
    RadioGroup categoryRadioGroup,serviceRadioGroup,timeFirstRadioGroup,timeSecondRadioGroup,timeThirdRadioGroup;

    //CosmoCalendar View
    CalendarView calendarView;

    /*Counter of level of depth of the reservation process,
    helper for back Android button to reverse to previous request*/
    private int currentStep = 1;

    RadioButton[] radioButtonsArrayCategory;
    RadioButton[] radioButtonArrayServices;

    RadioButton[] timesRadioButtonUpper,timesRadioButtonMiddle,timesRadioButtonBottom;

    //test
    String[] services;
    String[] categories;
    String[] times;

    private final int MAX_AMOUNT = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_user_testing);

        currentStepTextView = findViewById(R.id.currentStepTextView);
        saveDateTextView = findViewById(R.id.saveDateTextView);

        debugTV = findViewById(R.id.debugTV);

        categoryRadioGroup = findViewById(R.id.categoryRadioGroup);
        serviceRadioGroup = findViewById(R.id.serviceRadioGroup);
        timeFirstRadioGroup = findViewById(R.id.timeFirstRowRadioGroup);
        timeSecondRadioGroup = findViewById(R.id.timeSecondRowRadioGroup);
        timeThirdRadioGroup = findViewById(R.id.timeThirdRowRadioGroup);

        calendarView = findViewById(R.id.cosmo_calendar);

        //Calendar design
        //TODO implement disabledDays
        setUpCalendar();

        //TODO implement dynamic array size allocation
        radioButtonsArrayCategory = new RadioButton[3];
        radioButtonArrayServices = new RadioButton[3];
        timesRadioButtonUpper = new RadioButton[MAX_AMOUNT];
        timesRadioButtonMiddle = new RadioButton[MAX_AMOUNT];
        timesRadioButtonBottom = new RadioButton[MAX_AMOUNT];



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
//        times[13] = "13:00";
//        times[14] = "13:00";
//        times[15] = "13:00";

        timesRadioButtonUpper = getRadioButtons();
        timesRadioButtonBottom = getRadioButtons();
        timesRadioButtonMiddle = getRadioButtons();

        addView(timeFirstRadioGroup,timesRadioButtonUpper);
        addView(timeSecondRadioGroup,timesRadioButtonMiddle);
        addView(timeThirdRadioGroup,timesRadioButtonBottom);


        assignStringsToRadioButtons(timeFirstRadioGroup,timeSecondRadioGroup,timeThirdRadioGroup,times);

        categoryRadioGroup.setVisibility(View.VISIBLE);
        serviceRadioGroup.setVisibility(View.GONE);

        calendarView.setVisibility(View.GONE);
        saveDateTextView.setVisibility(View.GONE);

        timeFirstRadioGroup.setVisibility(View.GONE);
        timeSecondRadioGroup.setVisibility(View.GONE);
        timeThirdRadioGroup.setVisibility(View.GONE);

        String step = String.valueOf(currentStep);
        debugTV.setText(step);

        applyRbDesign(radioButtonsArrayCategory,categories,categoryRadioGroup);
        applyRbDesign(radioButtonArrayServices,services,serviceRadioGroup);
//        applyTimeRadioGroupDesign();

        // First-step
        categoryRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            currentStep++;
            String step2 = String.valueOf(currentStep);
            debugTV.setText(step2);
            selectCorrectTextColor(checkedId,categoryRadioGroup);

            categoryRadioGroup.startAnimation(
                    FadeOut()
            );

        });

        // Second-step
        serviceRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            currentStep++;
            String step3 = String.valueOf(currentStep);
            debugTV.setText(step3);
            selectCorrectTextColor(checkedId,serviceRadioGroup);

            serviceRadioGroup.startAnimation(
                    FadeOut()
            );
        });

        saveDateTextView.setOnClickListener(view -> {
            currentStep++;
            String step3 = String.valueOf(currentStep);
            debugTV.setText(step3);

            updateRadioGroupVisibility();

            List<Calendar> selectedDates = calendarView.getSelectedDates();

            // Iterate over the selected dates
            for (Calendar selectedDate : selectedDates) {
                // Do something with the selected date
                Toast.makeText(getApplicationContext(), selectedDate.getTime().toString(), Toast.LENGTH_SHORT).show();
                calendarView.update();
            }
            timeFirstRadioGroup.setVisibility(View.VISIBLE);
            timeSecondRadioGroup.setVisibility(View.VISIBLE);
            timeThirdRadioGroup.setVisibility(View.VISIBLE);
        });

        timeFirstRadioGroup.setOnCheckedChangeListener(
                (radioGroup, checkedId) ->
                selectCorrectTextColor(checkedId,timeFirstRadioGroup));

        timeSecondRadioGroup.setOnCheckedChangeListener(
                (radioGroup, checkedId) ->
                        selectCorrectTextColor(checkedId,timeSecondRadioGroup));

        timeThirdRadioGroup.setOnCheckedChangeListener(
                (radioGroup, checkedId) ->
                        selectCorrectTextColor(checkedId,timeThirdRadioGroup));
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

                timeFirstRadioGroup.setVisibility(View.GONE);
                timeSecondRadioGroup.setVisibility(View.GONE);
                timeThirdRadioGroup.setVisibility(View.GONE);

                calendarView.setVisibility(View.GONE);
                saveDateTextView.setVisibility(View.GONE);

                break;
            case 2:
                categoryRadioGroup.setVisibility(View.GONE);
                serviceRadioGroup.setVisibility(View.VISIBLE);

                timeFirstRadioGroup.setVisibility(View.GONE);
                timeSecondRadioGroup.setVisibility(View.GONE);
                timeThirdRadioGroup.setVisibility(View.GONE);

                calendarView.setVisibility(View.GONE);
                saveDateTextView.setVisibility(View.GONE);

                break;
            case 3:
                categoryRadioGroup.setVisibility(View.GONE);
                serviceRadioGroup.setVisibility(View.GONE);

                timeFirstRadioGroup.setVisibility(View.GONE);
                timeSecondRadioGroup.setVisibility(View.GONE);
                timeThirdRadioGroup.setVisibility(View.GONE);

                calendarView.setVisibility(View.VISIBLE);
                saveDateTextView.setVisibility(View.VISIBLE);

                break;
            case 4:
                categoryRadioGroup.setVisibility(View.GONE);
                serviceRadioGroup.setVisibility(View.GONE);

                timeFirstRadioGroup.setVisibility(View.VISIBLE);
                timeSecondRadioGroup.setVisibility(View.VISIBLE);
                timeThirdRadioGroup.setVisibility(View.VISIBLE);

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


    private void applyRbDesign(RadioButton[] radioButtonsArray,String[] StringArrayTest,RadioGroup radioGroup) {
            for (int i = 0; i < 3; i++) {
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
                radioButtonsArray[i].setText(StringArrayTest[i]);
                //add to radioGroup
                radioGroup.addView(radioButtonsArray[i]);
            }
    }



    public void assignStringsToRadioButtons(RadioGroup radioGroup1, RadioGroup radioGroup2, RadioGroup radioGroup3, String[] strings) {
        int numStrings = strings.length;
        int numRadioButtons1 = radioGroup1.getChildCount();
        int numRadioButtons2 = radioGroup2.getChildCount();
        int numRadioButtons3 = radioGroup3.getChildCount();
        int i = 0;

        // Assign strings to RadioButtons in RadioGroup1
        for (int j = 0; j < numRadioButtons1; j++) {
            if (i < numStrings) {
                RadioButton radioButton = (RadioButton) radioGroup1.getChildAt(j);
                radioButton.setText(strings[i]);
                i++;
            }
            if (i >= numStrings || j == numRadioButtons1 - 1) {
                radioGroup1.setOnCheckedChangeListener(null); // Disable listener to prevent selection of additional RadioButtons
                break;
            }
        }

        // Assign remaining strings to RadioButtons in RadioGroup2
        for (int j = 0; j < numRadioButtons2; j++) {
            if (i < numStrings) {
                RadioButton radioButton = (RadioButton) radioGroup2.getChildAt(j);
                radioButton.setText(strings[i]);
                i++;
            }
            if (i >= numStrings || j == numRadioButtons2 - 1) {
                radioGroup2.setOnCheckedChangeListener(null); // Disable listener to prevent selection of additional RadioButtons
                break;
            }
        }

        // Assign remaining strings to RadioButtons in RadioGroup3
        for (int j = 0; j < numRadioButtons3; j++) {
            if (i < numStrings) {
                RadioButton radioButton = (RadioButton) radioGroup3.getChildAt(j);
                radioButton.setText(strings[i]);
                i++;
            }
            if (i >= numStrings || j == numRadioButtons3 - 1) {
                radioGroup3.setOnCheckedChangeListener(null); // Disable listener to prevent selection of additional RadioButtons
                break;
            }
        }

        // Handle case where string array has more than 12 strings
        if (i < numStrings) {
            // Alert user that not all strings could be assigned to RadioButtons
            Toast.makeText(getApplicationContext(), "String array has more than 12 strings", Toast.LENGTH_SHORT).show();
        }
    }


//    private void applyTimeRadioGroupDesign()
//    {
//        int index_middle = 0;
//        int index_bottom = 0;
//        for(int i = 0;i < times.length;i++){
//            if(i < max_amount) {
//                getRadioButtons(i, timesRadioButtonUpper);
//                timesRadioButtonUpper[i].setText(times[i]);
//                timeFirstRadioGroup.addView(timesRadioButtonUpper[i]);
//            }
//            if(i >= max_amount && i < max_amount*2) {
//                getRadioButtons(index_middle, timesRadioButtonMiddle);
//                timesRadioButtonMiddle[index_middle].setText(times[i]);
//                timeSecondRadioGroup.addView(timesRadioButtonMiddle[index_middle]);
//                index_middle++;
//            }
//            if(i >= max_amount*2 && i <= max_amount*3)
//            {
//                getRadioButtons(index_bottom, timesRadioButtonBottom);
//                timesRadioButtonBottom[index_bottom].setText(times[i]);
//                timeThirdRadioGroup.addView(timesRadioButtonBottom[index_bottom]);
//                index_bottom++;
//            }
//            else{
//                break;
//            }
//
//        }
//    }

    private void addView(RadioGroup radioGroup,RadioButton[] radioButtons)
    {
        for(int i = 0; i < MAX_AMOUNT; i++)
        {
            radioGroup.addView(radioButtons[i]);
        }
    }



    private RadioButton[] getRadioButtons()
    {
        RadioButton[] radioButtons = new RadioButton[MAX_AMOUNT];
        for(int index = 0; index < MAX_AMOUNT; index++){
            radioButtons[index] = new RadioButton(getApplicationContext());
            radioButtons[index].setLayoutParams(getLayoutParamsForTime());
            radioButtons[index].setTypeface(getTypeface(R.font.sen));
            radioButtons[index].setBackgroundColor(Color.TRANSPARENT);
            radioButtons[index].setButtonDrawable(R.drawable.radio_null_button);
            radioButtons[index].setTextColor(Color.BLACK);
        }
        return radioButtons;
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
        params.setMargins(getPx(16), 0, getPx(16), 0);
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

    private void setUpCalendar() {
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

        int textColor = Color.parseColor("#B3B3B3");
        int disabledTextColor = Color.parseColor("#616161");

        calendarView.setWeekendDayTextColor(orangeColor);
        calendarView.setSelectedDayBackgroundColor(orangeColor);
        calendarView.setCurrentDayIconRes(R.drawable.red_dot);

        calendarView.setDayTextColor(textColor);
        calendarView.setDisabledDayTextColor(disabledTextColor);

        calendarView.update();
    }


    public int getPx(int dp){
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


}