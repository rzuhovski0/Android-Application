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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class timeLayoutTestingActivity extends AppCompatActivity {

    RadioGroup radioGroup;
//    GridLayout gridLayout;

    String[] strings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_layout_testing);

////        radioGroup = findViewById(R.id.radioGroup);
////        gridLayout = findViewById(R.id.gridLayout);
//
//        strings = new String[5];
//        strings[0] = "9:30";
//        strings[1] = "9:30";
//        strings[2] = "9:30";
//        strings[3] = "9:30";
//        strings[4] = "9:30";
//
//
//
//
//
//                                                // Create GridLayout and set column count to 4
//        gridLayout.setColumnCount(4);
//
//        // Create new RadioGroup and add to GridLayout
//        gridLayout.addView(radioGroup);
//
//        // Add RadioButtons to RadioGroup
//        for (int i = 0; i < 4; i++) {
//            RadioButton radioButton = new RadioButton(getApplicationContext());
//            radioButton.setText(strings[i]);
//            radioGroup.addView(radioButton);
//
//            // If there are more than 4 RadioButtons, add the additional RadioButtons to new RadioGroups
//            if ((i + 1) % 4 == 0 && i != 4 - 1) {
//                // Create new RadioGroup and add to GridLayout
//                radioGroup = new RadioGroup(getApplicationContext());
//                gridLayout.addView(radioGroup);
//
//                // Create new RadioButton and add to new RadioGroup
//                radioButton = new RadioButton(getApplicationContext());
//                radioButton.setText(strings[i + 1]);
//                radioGroup.addView(radioButton);
//            }
//        }

        LinearLayout radioGroup = findViewById(R.id.radio_group); // Get the parent LinearLayout
        radioGroup.setOrientation(LinearLayout.HORIZONTAL); // Set orientation to horizontal

        int radioButtonCounter = 0; // Counter to keep track of the number of radio buttons added

        // Add radio buttons to the layout
        int numberOfRadioButtons = 8;
        for (int i = 0; i < numberOfRadioButtons; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText("Radio Button " + i);
            radioButton.setId(i);
            radioGroup.addView(radioButton);

            radioButtonCounter++;

            // Check if the counter has reached 4
            if (radioButtonCounter == 4) {
                // Add a new LinearLayout with vertical orientation as a child of the main LinearLayout
                LinearLayout verticalLinearLayout = new LinearLayout(this);
                verticalLinearLayout.setOrientation(LinearLayout.VERTICAL);
                radioGroup.addView(verticalLinearLayout);

                radioButtonCounter = 0; // Reset the counter
            }
        }


    }


}