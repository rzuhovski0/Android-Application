package com.btproject.barberise.utils;

import com.btproject.barberise.reservation.Category;
import com.btproject.barberise.reservation.Subcategory;

import java.util.ArrayList;
import java.util.HashMap;

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

    public static ArrayList<HashMap<String,ArrayList<Subcategory>>> getSubcategoriesFromCategories(ArrayList<Category> categories)
    {

        ArrayList<HashMap<String,ArrayList<Subcategory>>> subcategories = new ArrayList<>();
        HashMap<String,ArrayList<Subcategory>> subcategoriesMap;

        for(Category category : categories)
        {
            subcategoriesMap = new HashMap<>();
            subcategoriesMap.put(category.getName(),category.getSubcategories());
            subcategories.add(subcategoriesMap);
        }
        return subcategories;
    }

}
