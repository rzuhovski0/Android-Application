package com.btproject.barberise;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ContactSupportActivity extends AppCompatActivity {

    private EditText yourMessageEditText;
    private Button button;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_support);
        yourMessageEditText = findViewById(R.id.yourMessageEditText);
        progressBar = findViewById(R.id.progressBar);
        button = findViewById(R.id.button);

        progressBar.setVisibility(View.GONE);


        button.setOnClickListener(view -> {
            createTimer();
        });
    }

    private void createTimer()
    {
        progressBar.setVisibility(View.VISIBLE);

        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Update UI with the remaining time
            }

            public void onFinish() {
                // Counter finished, perform actions
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),R.string.message_success,Toast.LENGTH_LONG).show();
            }
        }.start();
    }
}