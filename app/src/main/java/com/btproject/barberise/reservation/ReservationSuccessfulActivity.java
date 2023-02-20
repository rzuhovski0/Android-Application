package com.btproject.barberise.reservation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.btproject.barberise.R;
import com.btproject.barberise.navigation.MenuActivity;
import com.btproject.barberise.navigation.profile.PartnerProfileActivity;
import com.btproject.barberise.navigation.profile.RegistrationActivity;

import java.awt.font.TextAttribute;

public class ReservationSuccessfulActivity extends AppCompatActivity {

    private TextView checkHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_successful);

        checkHere = findViewById(R.id.checkHere);

        checkHere.setOnClickListener(view -> {
            Intent intent = new Intent(ReservationSuccessfulActivity.this, MenuActivity.class);
            intent.putExtra("openReservations",true);
            startActivity(intent);
            finish();
        });
    }
}