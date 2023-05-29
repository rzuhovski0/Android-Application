package com.btproject.barberise.navigation.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.btproject.barberise.R;
import com.btproject.barberise.adapters.ConfServicesCardAdapter;
import com.btproject.barberise.adapters.ReservationCardAdapter;
import com.btproject.barberise.reservation.Category;
import com.btproject.barberise.reservation.Subcategory;

import java.util.ArrayList;

public class ConfigureServicesActivity extends AppCompatActivity {

    private EditText noOfServicesEditText,noOfSubServicesEditText;
    private TextView saveSettingsButton,textView19,textView18,textView20;
    private Button btnConfirm;
    private RecyclerView recyclerView;
    private ArrayList<Category> categories = new ArrayList<>();
    private ConfServicesCardAdapter confServicesCardAdapter;

    String sourceActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_services);

        initItems();

        sourceActivity = getIntent().getStringExtra("sourceActivity");

        // Init visibility
        recyclerView.setVisibility(View.GONE);

        btnConfirm.setOnClickListener(view -> {
            // Get the text input from the noOfServicesEditText
            String numOfServicesString = noOfServicesEditText.getText().toString();
            // Get the text input from the noOfSubServicesEditText
            String numOfSubServicesString = noOfSubServicesEditText.getText().toString();
            try {
                // Parse the string into an integer
                int numOfServices = Integer.parseInt(numOfServicesString);
                int numOfSubServices = Integer.parseInt(numOfSubServicesString);

                if(numOfServices > 0 && numOfSubServices > 0)
                {
                    createCategories(numOfServices,numOfSubServices);
                    updateView();
                }

            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(),"Make sure you inputted text!",Toast.LENGTH_LONG).show();
            }
        });

        saveSettingsButton.setOnClickListener(view -> {
            if (sourceActivity.equals("PartnerProfileSettingsActivity")) {
                PartnerProfileSettingsActivity.categories = categories;
                PartnerProfileSettingsActivity.servicesUpdated = true;
            } else if (sourceActivity.equals("RegistrationActivity")) {
                RegistrationActivity.categories = categories;
            }
            onBackPressed();
        });
    }

    private void createCategories(int numOfServices, int numOfSubServices)
    {
        for(int i = 0; i < numOfServices;i++)
        {
            Category category = new Category();
            for(int j = 0; j < numOfSubServices;j++){
                Subcategory subcategory = new Subcategory();
                category.addSubcategory(subcategory);
            }
            categories.add(category);
        }
    }

    private void updateView()
    {
        // Hide editTexts
        noOfServicesEditText.setVisibility(View.GONE);
        noOfSubServicesEditText.setVisibility(View.GONE);
        btnConfirm.setVisibility(View.GONE);
        textView19.setVisibility(View.GONE);
        textView18.setVisibility(View.GONE);
        textView20.setVisibility(View.GONE);

        // Display recyclerView
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));

        confServicesCardAdapter = new ConfServicesCardAdapter(categories,getApplicationContext());
        recyclerView.setAdapter(confServicesCardAdapter);
    }

    private void initItems()
    {
        // Init items
        textView19 = findViewById(R.id.textView19);
        textView18 = findViewById(R.id.textView18);
        textView20 = findViewById(R.id.textView20);
        noOfServicesEditText = findViewById(R.id.noOfServicesEditText);
        noOfSubServicesEditText = findViewById(R.id.noOfSubServicesEditText);
        saveSettingsButton = findViewById(R.id.saveSettingsButton);
        btnConfirm = findViewById(R.id.btnConfirm);
        recyclerView = findViewById(R.id.inConfRecyclerView);
    }
}