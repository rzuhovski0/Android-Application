package com.btproject.barberise.utils;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.core.content.res.ResourcesCompat;

import com.btproject.barberise.R;

public class ReservationUtils {

    public static void selectCorrectTextColor(int checkedId, RadioGroup radioGroup)
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
