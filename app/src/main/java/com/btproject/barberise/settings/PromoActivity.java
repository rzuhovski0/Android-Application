package com.btproject.barberise.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.btproject.barberise.R;
import com.btproject.barberise.navigation.profile.PartnerProfileActivity;

public class PromoActivity extends AppCompatActivity {

    private Button useButton;
    private EditText promoCodeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);

        // TODO implement logic for barber registration et. -> load registration code from db
        // Currently hard-coded "123456"

        useButton = findViewById(R.id.usePromoCodeButton);
        promoCodeEditText = findViewById(R.id.promoCodeEditText);

        useButton.setOnClickListener(view ->
        {
            String enteredCode = promoCodeEditText.getText().toString();
            if(enteredCode.equals("123456"))
            {
                Intent intent = new Intent(getApplicationContext(), PartnerProfileActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(),R.string.invalid_code,Toast.LENGTH_LONG).show();
            }
        });
    }
}