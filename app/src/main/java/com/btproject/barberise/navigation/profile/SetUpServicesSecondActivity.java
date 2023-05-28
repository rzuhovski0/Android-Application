package com.btproject.barberise.navigation.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.btproject.barberise.R;

import java.util.ArrayList;
import java.util.List;

public class SetUpServicesSecondActivity extends AppCompatActivity{

    LinearLayout layoutList;
    Button buttonAdd;

    List<String> teamList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_services_second);

        layoutList = findViewById(R.id.layout_list);
        buttonAdd = findViewById(R.id.button_add);

        buttonAdd.setOnClickListener(view -> {
            addView();
        });

        teamList.add("Team");
        teamList.add("India");
        teamList.add("Australia");
        teamList.add("England");

    }

    private void addView() {
        View cricketerView = getLayoutInflater().inflate(R.layout.row_add_cricketer,null,false);
        EditText editText = (EditText) cricketerView.findViewById(R.id.edit_cricketer_name);
        AppCompatSpinner spinnerTeam = (AppCompatSpinner)cricketerView.findViewById(R.id.spinner_team);
        ImageView imageClose = (ImageView)cricketerView.findViewById(R.id.image_remove);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,teamList);
        imageClose.setOnClickListener(view -> {
            removeView(cricketerView);
        });

        layoutList.addView(cricketerView);
    }

    private void removeView(View view)
    {
        layoutList.removeView(view);
    }

}