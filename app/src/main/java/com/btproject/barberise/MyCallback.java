package com.btproject.barberise;

import android.graphics.Bitmap;

import java.util.ArrayList;

public interface MyCallback {

    void onProfileDataLoaded(String imageURL, String username, Bitmap imageBitmap);

    void onCategoriesLoaded(ArrayList<String> categoryName);

    void onHoursLoaded();

}
