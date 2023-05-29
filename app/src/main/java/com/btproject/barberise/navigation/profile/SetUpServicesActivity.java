package com.btproject.barberise.navigation.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.btproject.barberise.R;
import com.btproject.barberise.adapters.CategoryCardAdapter;
import com.btproject.barberise.reservation.Category;

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

        addCategoryButton = findViewById(R.id.btnConfirm);
        saveTextView = findViewById(R.id.saveSettingsButton);
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
        });
    }

    private void initRecyclerView()
    {
        recyclerView = findViewById(R.id.inConfRecyclerView);
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