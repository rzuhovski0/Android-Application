package com.btproject.barberise.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.btproject.barberise.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText emailEditTextResetPass;
    private Button buttonResetPass;
    private ProgressBar progressBarResetPass;
    private TextView backToLoginTextView;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Init components
        progressBarResetPass = findViewById(R.id.progressBarResetPass);
        progressBarResetPass.setVisibility(View.GONE);

        backToLoginTextView = findViewById(R.id.backToLoginTextView);
        backToLoginTextView.setVisibility(View.GONE);

        buttonResetPass = findViewById(R.id.buttonResetPass);
        emailEditTextResetPass = findViewById(R.id.emailEditTextResetPass);

        // Init FirebaseAuth
        auth = FirebaseAuth.getInstance();

        buttonResetPass.setOnClickListener(view ->
        {
            resetPassword();
        });

        backToLoginTextView.setOnClickListener(view ->
        {
            finish();
        });
    }

    private void resetPassword()
    {
        // trim() if extra spaces present
        String email = emailEditTextResetPass.getText().toString().trim();
        if(email.isEmpty())
        {
            Toast.makeText(getApplicationContext(),R.string.empty_email,Toast.LENGTH_LONG).show();
            emailEditTextResetPass.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Toast.makeText(getApplicationContext(),R.string.valid_email,Toast.LENGTH_LONG).show();
            emailEditTextResetPass.requestFocus();
            return;
        }
        progressBarResetPass.setVisibility(View.VISIBLE);
        backToLoginTextView.setVisibility(View.VISIBLE);

        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                Toast.makeText(getApplicationContext(),R.string.check_email,Toast.LENGTH_LONG).show();
            }else
            {
                Toast.makeText(getApplicationContext(),R.string.sth_went_wrong,Toast.LENGTH_LONG).show();
            }
        });

    }
}