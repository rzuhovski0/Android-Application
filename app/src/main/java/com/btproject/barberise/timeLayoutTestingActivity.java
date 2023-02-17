package com.btproject.barberise;

import androidx.annotation.FontRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.GridLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class timeLayoutTestingActivity extends AppCompatActivity {

    RadioGroup openingHoursRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_layout_testing);
        openingHoursRadioGroup = findViewById(R.id.opening_hours_radiogroup);

        // Populate the RadioGroup with the opening hours
        populateOpeningHoursRadioGroup(openingHoursRadioGroup, getOpeningHours());

        // Set a listener for the RadioGroup to uncheck all other radio buttons when one is checked
        openingHoursRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton radioButton = (RadioButton) group.getChildAt(i);
                    if (radioButton.getId() != checkedId) {
                        radioButton.setChecked(false);
                    }
                }
            }
        });

    }

//    private void populateOpeningHoursRadioGroup(RadioGroup radioGroup, List<String> openingHours) {
//        // Set up the GridLayout
//        GridLayout gridLayout = new GridLayout(this);
//        gridLayout.setColumnCount(4);
//        gridLayout.setRowCount((int) Math.ceil(openingHours.size() / 4.0));
//
//        // Create and add the RadioButton views
//        for (String openingHour : openingHours) {
//            RadioButton radioButton = new RadioButton(this);
//            radioButton.setText(openingHour);
//            gridLayout.addView(radioButton);
//        }
//
//        // Add the GridLayout to the RadioGroup
//        radioGroup.addView(gridLayout);
//    }

    /**Partially working, can select more rb vertically*/
//    private void populateOpeningHoursRadioGroup(RadioGroup radioGroup, List<String> openingHours) {
//        // Create a new LinearLayout to hold the RadioButtons
//        LinearLayout linearLayout = new LinearLayout(this);
//        linearLayout.setOrientation(LinearLayout.VERTICAL);
//
//        // Create a new RadioGroup for each row of RadioButtons
//        RadioGroup rowRadioGroup = new RadioGroup(this);
//        rowRadioGroup.setOrientation(RadioGroup.HORIZONTAL);
//        linearLayout.addView(rowRadioGroup);
//
//        // Add the RadioButtons to the LinearLayout
//        int i = 0;
//        for (String openingHour : openingHours) {
//            RadioButton radioButton = new RadioButton(this);
//            radioButton.setText(openingHour);
//
//            // Add the RadioButton to the current row RadioGroup
//            rowRadioGroup.addView(radioButton);
//
//            // When the current row RadioGroup has 4 buttons, create a new row RadioGroup
//            if (++i % 4 == 0) {
//                rowRadioGroup = new RadioGroup(this);
//                rowRadioGroup.setOrientation(RadioGroup.HORIZONTAL);
//                linearLayout.addView(rowRadioGroup);
//            }
//        }
//
//        // Add the LinearLayout to the main RadioGroup
//        radioGroup.addView(linearLayout);
//    }

//    private void populateOpeningHoursRadioGroup(RadioGroup radioGroup, List<String> openingHours) {
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
//
//            radioButton.setOnClickListener(v -> {
//                // Uncheck all other RadioButtons in the same row and column
//                int selectedId = radioButton.getId();
//                int row = selectedId / 4;
//                int col = selectedId % 4;
//                for (int j = 0; j < openingHours.size(); j++) {
//                    if (j / 4 == row || j % 4 == col) {
//                        View view = gridLayout.getChildAt(j);
//                        if (view instanceof RadioButton && view != v) {
//                            ((RadioButton) view).setChecked(false);
//                        }
//                    }
//                }
//            });
//
//            gridLayout.addView(radioButton);
//        }
//
//        // Add the GridLayout to the RadioGroup
//        radioGroup.addView(gridLayout);
//    }

    private void populateOpeningHoursRadioGroup(RadioGroup radioGroup, List<String> openingHours) {
        // Set up the GridLayout
        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(4);
        gridLayout.setRowCount((int) Math.ceil(openingHours.size() / 4.0));

        // Create and add the RadioButton views
        for (int i = 0; i < openingHours.size(); i++) {
            RadioButton radioButton = new RadioButton(this);
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
                            }
                        }
                    }
                }
            });
        }

        // Add the GridLayout to the RadioGroup
        radioGroup.addView(gridLayout);
    }







    private List<String> getOpeningHours()
    {
        List<String> openingHours = new LinkedList<>();

        openingHours.add("9:00");
        openingHours.add("9:30");
        openingHours.add("10:00");
        openingHours.add("10:30");

        openingHours.add("11:00");
        openingHours.add("9:30");
        openingHours.add("10:00");
        openingHours.add("10:30");

        openingHours.add("11:00");
        openingHours.add("9:30");
        openingHours.add("10:00");
        openingHours.add("10:30");

        openingHours.add("11:00");
        openingHours.add("9:30");
        openingHours.add("10:00");
        openingHours.add("10:30");

        openingHours.add("11:00");
        openingHours.add("9:30");
        openingHours.add("10:00");
        openingHours.add("10:30");

        return openingHours;
    }



}