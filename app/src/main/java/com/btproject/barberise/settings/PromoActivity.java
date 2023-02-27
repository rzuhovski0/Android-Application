package com.btproject.barberise.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.btproject.barberise.R;

public class PromoActivity extends AppCompatActivity {

    private Button useButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);

        //TODO implement logic for barber registration

        useButton = findViewById(R.id.usePromoCodeButton);
        useButton.setOnClickListener(view ->
        {
            Toast.makeText(getApplicationContext(),R.string.invalid_code,Toast.LENGTH_LONG).show();
        });
    }
}