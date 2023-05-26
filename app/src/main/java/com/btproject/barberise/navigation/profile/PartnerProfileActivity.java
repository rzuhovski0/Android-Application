package com.btproject.barberise.navigation.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.btproject.barberise.R;
import com.btproject.barberise.partner.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class PartnerProfileActivity extends AppCompatActivity {

    private Button buttonRegister,buttonLogin;
    private EditText emailRegisterEditText,passwordRegisterEditText;
    private EditText emailLoginEditText,passwordLoginEditText;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_profile);

        buttonRegister = findViewById(R.id.buttonRegister);
        buttonLogin = findViewById(R.id.buttonLogin);

        emailRegisterEditText = findViewById(R.id.emailRegisterEditText);
        passwordRegisterEditText = findViewById(R.id.passwordEditText);

        emailLoginEditText = findViewById(R.id.emailLoginEditText);
        passwordLoginEditText = findViewById(R.id.passwordLoginEditText);

        auth = FirebaseAuth.getInstance();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailRegisterEditText.getText().toString();
                String password = passwordRegisterEditText.getText().toString();

                if(!email.equals("") && !password.equals("")){
                    Intent intent = new Intent(PartnerProfileActivity.this,RegistrationActivity.class);
                    intent.putExtra("email",email);
                    intent.putExtra("password",password);
                    startActivity(intent);
                }else {
                    Toast.makeText(PartnerProfileActivity.this, "Email and password mustn't be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonLogin.setOnClickListener(view -> {
            String email = emailLoginEditText.getText().toString();
            String password = passwordLoginEditText.getText().toString();
            if(!email.equals("") && !password.equals("")) {
                signIn(email,password);
            }else {
                Toast.makeText(PartnerProfileActivity.this,
                        "Please enter an email and password",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void signIn(String email, String password)
    {
        auth.signOut();
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Intent i = new Intent(getApplicationContext(), PartnerSignedInActivity.class);
                    Toast.makeText(PartnerProfileActivity.this,"Sign in is successful",Toast.LENGTH_LONG).show();
                    startActivity(i);
                }else {
                    Toast.makeText(PartnerProfileActivity.this,"Sign in is not successful",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}