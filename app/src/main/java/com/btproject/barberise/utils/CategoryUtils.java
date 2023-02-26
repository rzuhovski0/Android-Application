package com.btproject.barberise.utils;

import com.btproject.barberise.reservation.Category;
import com.btproject.barberise.reservation.Subcategory;

import java.util.ArrayList;

public class CategoryUtils {

    public static double getSubcategoryPriceString(ArrayList<Category> categories,String categoryString,String subcategoryString)
    {
        for(Category category : categories)
        {
            if(category.getName().equals(categoryString)){
                ArrayList<Subcategory> subcategories = category.getSubcategories();
                for(Subcategory subcategory : subcategories)
                {
                    if(subcategory.getName().equals(subcategoryString))
                        return subcategory.getPrice();
                }
            }
        }
        return 0.0;
    }

}
