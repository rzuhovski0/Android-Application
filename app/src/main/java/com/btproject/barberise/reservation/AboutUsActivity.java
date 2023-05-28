package com.btproject.barberise.reservation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.btproject.barberise.R;
import com.btproject.barberise.adapters.ReservationCardAdapter;
import com.btproject.barberise.adapters.ServicesCardAdapter;

import java.util.ArrayList;

public class AboutUsActivity extends AppCompatActivity {

    private TextView barberShopNameTextView;
    private ServicesCardAdapter servicesCardAdapter;
    private ArrayList<Category> categories;
    private RecyclerView recyclerView;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        barberShopNameTextView = findViewById(R.id.barberNameInAboutUsTextView);

        // Set-up RecyclerView
        recyclerView = findViewById(R.id.recyclerViewInAboutUs);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));

        // Get the Intent that started this Activity
        categories = (ArrayList<Category>) getIntent().getSerializableExtra("categories");
        name = getIntent().getStringExtra("name");

        // Apply name
        barberShopNameTextView.setText(name);

        // Connect adapter to recyclerView
        servicesCardAdapter = new ServicesCardAdapter(getApplicationContext(),categories);
        recyclerView.setAdapter(servicesCardAdapter);

    }
}