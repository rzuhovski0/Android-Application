package com.btproject.barberise.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.FontRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;

import com.btproject.barberise.R;

public class LayoutUtils {

    public static RadioGroup.LayoutParams getLayoutParams(Resources resources)
    {
        //Layout params for radio buttons
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                getPx(270,resources),
                getPx(50,resources)
        );
        params.setMargins(getPx(8,resources), 0, getPx(8,resources), getPx(16,resources));
        params.weight = 1.0f;
        params.gravity = Gravity.CENTER;

        return params;
    }

    public static int getPx(int dp, Resources resources)
    {
        float scale = resources.getDisplayMetrics().density;
        return((int) (dp * scale + 0.5f));
    }

    public static Typeface getTypeface(@FontRes int id, Context context)
    {
        //example id -> R.font.sen
        return ResourcesCompat.getFont(context, id);
    }

    public static RadioButton prettifyButton(Context context,Resources resources)
    {
        RadioButton radioButton = new RadioButton(context);
        //Set-up background selector, disable Rb Button & set text properties
        //Set-up layout params and font params
        radioButton.setLayoutParams(getLayoutParams(resources));
        radioButton.setTypeface(getTypeface(R.font.sen,context));
        radioButton.setBackgroundResource(R.drawable.radio_selector);
        radioButton.setButtonDrawable(R.drawable.radio_null_button);
        radioButton.setTextColor(Color.BLACK);
        radioButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        radioButton.setTextSize(14);
        radioButton.setElevation(getPx(4,resources));

        return radioButton;
    }

    public static RadioButton getEmptyButton(Context context)
    {
        RadioButton radioButton = new RadioButton(context);
        radioButton.setTypeface(getTypeface(R.font.sen,context));
        radioButton.setBackgroundColor(Color.TRANSPARENT);
        radioButton.setButtonDrawable(R.drawable.radio_null_button);
        radioButton.setTextColor(Color.BLACK);
        return radioButton;
    }

    public static GridLayout.LayoutParams getGridLayoutParams(Resources resources)
    {
        // Set the horizontal gravity of the layout params to center the row of elements
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.width = GridLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.setGravity(Gravity.CENTER_HORIZONTAL);
        layoutParams.setMargins(getPx(12,resources), 0, getPx(12,resources), getPx(20,resources));

        return layoutParams;
    }

    public static ConstraintSet getConstraintSet(ConstraintLayout constraintLayout,int viewId)
    {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.centerHorizontally(viewId, ConstraintSet.PARENT_ID);
        return constraintSet;
    }

}