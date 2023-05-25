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

public class SetUpServicesActivity extends AppCompatActivity {

    private ArrayList<Category> categories = new ArrayList<>();
    private CategoryCardAdapter categoryCardAdapter;
    private RecyclerView recyclerView;
    private Button addCategoryButton;

    private TextView saveTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_services);

        addCategoryButton = findViewById(R.id.addCategoryButton);
        saveTextView = findViewById(R.id.inSetServicesSaveTextView);

        initRecyclerView();

        addCategoryButton.setOnClickListener(view ->
        {
            // Create new category
            Category category = new Category();
            categories.add(category);

            // Update categories inside adapter
//            categoryCardAdapter.addCategory(category);

            //Get item index and notify adapter
            int index = getItemIndex(category);
//            categoryCardAdapter.notifyItemInserted(index);
            categoryCardAdapter.notifyDataSetChanged();
        });

        initAdapter();

        saveTextView.setOnClickListener(view -> {
            RegistrationActivity.categories = categories;
            onBackPressed();
        });
    }

    private int getItemIndex(Category categoryInput) {
        int i = 0;
        for(Category category : categories) {
            if(category == categoryInput) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private void initRecyclerView()
    {
        recyclerView = findViewById(R.id.inServicesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));
    }

    private void initAdapter()
    {
        categoryCardAdapter = new CategoryCardAdapter(getApplicationContext(),categories);
        recyclerView.setAdapter(categoryCardAdapter);
    }
}