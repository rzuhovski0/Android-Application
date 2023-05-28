package com.btproject.barberise.navigation.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.btproject.barberise.R;
import com.btproject.barberise.adapters.CategoryCardAdapter;
import com.btproject.barberise.adapters.FavoriteCardAdapter;
import com.btproject.barberise.reservation.Category;
import com.btproject.barberise.reservation.Subcategory;

import java.util.ArrayList;

public class SetUpServicesActivity extends AppCompatActivity implements CategoryCardAdapter.CategoryRemoveListener {

    public ArrayList<Category> categories = new ArrayList<>();
    private CategoryCardAdapter categoryCardAdapter;
    private RecyclerView recyclerView;
    private Button addCategoryButton;

    private TextView saveTextView,textView8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_services);

        addCategoryButton = findViewById(R.id.addCategoryButton);
        saveTextView = findViewById(R.id.inSetServicesSaveTextView);
        textView8 = findViewById(R.id.textView8);

        initRecyclerView();

        addCategoryButton.setOnClickListener(view ->
        {
            // Create new category
            Category category = new Category();
            categories.add(category);

            // Add the new category to the adapter
            categoryCardAdapter.notifyDataSetChanged();
        });

        initAdapter();

        saveTextView.setOnClickListener(view -> {
    //            RegistrationActivity.categories = categories;
    //            onBackPressed();
            for(Category category : categories)
            {
                category.getSubcategories();
            }
        });
    }

    private void initRecyclerView()
    {
        recyclerView = findViewById(R.id.inServicesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));
    }

    private void initAdapter()
    {
        categoryCardAdapter = CategoryCardAdapter.getCategoryCardAdapter();
        recyclerView.setAdapter(categoryCardAdapter);
        categoryCardAdapter.setContextAndCategories(getApplicationContext(),categories);
        // Set the CategoryRemoveListener to the adapter
        categoryCardAdapter.setCategoryRemoveListener(this);
    }

    @Override
    public void onCategoryRemoved(Category category) {
        categories.remove(category);
    }

}