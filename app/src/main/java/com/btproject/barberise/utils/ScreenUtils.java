package com.btproject.barberise.utils;

import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ScreenUtils {

    public static int getScreenHeight(WindowManager windowManager)
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int getScreenWidth(WindowManager windowManager)
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

}
