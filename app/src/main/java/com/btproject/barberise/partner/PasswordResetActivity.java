package com.btproject.barberise.partner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.btproject.barberise.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends AppCompatActivity {

    private Button resetButton;
    private EditText emailEditText;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        resetButton = findViewById(R.id.buttonReset);
        emailEditText = findViewById(R.id.emailEditTextReset);
        auth = FirebaseAuth.getInstance();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                if(!email.equals("")){
                    passwordReset(email);
                }
            }
        });

    }

    public void passwordReset(String email)
    {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(PasswordResetActivity.this, "Please check your email", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(PasswordResetActivity.this, "There is a problem", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}