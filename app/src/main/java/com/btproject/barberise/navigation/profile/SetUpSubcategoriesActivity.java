package com.btproject.barberise.navigation.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.btproject.barberise.R;
import com.btproject.barberise.adapters.CategoryCardAdapter;
import com.btproject.barberise.adapters.SubcategoryCardAdapter;
import com.btproject.barberise.reservation.Category;
import com.btproject.barberise.reservation.Subcategory;

import java.util.ArrayList;

public class SetUpSubcategoriesActivity extends AppCompatActivity {

    private Button buttonAddSubcategory;
    private TextView categoryName,buttonSave;
    private ArrayList<Subcategory> subcategories = new ArrayList<>();
    private SubcategoryCardAdapter subcategoryCardAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_subcategories);

        // Init items
        buttonAddSubcategory = findViewById(R.id.buttonAddSubcategory);
        buttonSave = findViewById(R.id.buttonSave);
        categoryName = findViewById(R.id.categoryNameTextView);

        initRecyclerView();

        buttonAddSubcategory.setOnClickListener(view ->
        {
            // Create new subcategory
            Subcategory subcategory = new Subcategory();
            subcategories.add(subcategory);

            if(!subcategories.isEmpty())
                subcategoryCardAdapter.notifyDataSetChanged();
        });

        initAdapter();
    }

    private void initRecyclerView()
    {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));
    }

    private void initAdapter()
    {
        subcategoryCardAdapter = new SubcategoryCardAdapter(getApplicationContext(),subcategories);
        recyclerView.setAdapter(subcategoryCardAdapter);
    }
}