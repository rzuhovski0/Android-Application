package com.btproject.barberise;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.btproject.barberise.R;

public class AboutAppActivity extends AppCompatActivity {

    private TextView rateAppTextView, instagramTextView, legalTermsTextView, contactTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        rateAppTextView = findViewById(R.id.textView24);
        instagramTextView = findViewById(R.id.textView27);
        legalTermsTextView = findViewById(R.id.textView28);
        contactTextView = findViewById(R.id.textView29);

        rateAppTextView.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(),R.string.rate_warn,Toast.LENGTH_LONG).show();
        });

        instagramTextView.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(),R.string.instagram_warn,Toast.LENGTH_LONG).show();
        });

        legalTermsTextView.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(),R.string.terms_warn,Toast.LENGTH_LONG).show();
        });

        contactTextView.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(),R.string.contact_warn,Toast.LENGTH_LONG).show();
        });
    }
}