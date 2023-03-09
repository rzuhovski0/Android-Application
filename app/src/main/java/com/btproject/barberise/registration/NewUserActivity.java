package com.btproject.barberise.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.btproject.barberise.R;
import com.btproject.barberise.navigation.MenuActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class NewUserActivity extends AppCompatActivity {

    // UI
    private Button singInButton;
    private EditText nameEditText, surnameEditText, emailEditText;
    private TextView phone;

    // Client Data
    private String usedId;
    private String phoneNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        initUI();

        getClientDataFromIntent();

        phone.setText(phoneNo);

        singInButton.setOnClickListener(view -> {

            String name = nameEditText.getText().toString();
            String surname = surnameEditText.getText().toString();
            String email = emailEditText.getText().toString();

            if(name.isEmpty()){
                Toast.makeText(getApplicationContext(),R.string.set_name,Toast.LENGTH_LONG).show();
                return;
            }

            if(surname.isEmpty())
            {
                Toast.makeText(getApplicationContext(),R.string.set_surname,Toast.LENGTH_LONG).show();
                return;
            }

            storeClientDataToDatabase(name,surname,phoneNo,email);

        });


    }

    private void storeClientDataToDatabase(String name, String surname, String phoneNo,String email)
    {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("clients").child(usedId);

        // Create client's data map
        HashMap<String, String> clientData = new HashMap<>();

        clientData.put("name",name);
        clientData.put("surname",surname);
        clientData.put("email",email);
        clientData.put("phoneNo",phoneNo);

        // Store the data
        dbRef.setValue(clientData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                launchMenuActivity();
            }
        });
    }

    private void launchMenuActivity()
    {
        Intent intent = new Intent(NewUserActivity.this, MenuActivity.class);

        //To prevent user from going back to previous activity by clicking back button
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void getClientDataFromIntent()
    {
        usedId = getIntent().getStringExtra("USER_ID");
        phoneNo = getIntent().getStringExtra("PHONE_NUMBER");
    }

    private void initUI()
    {
        singInButton = findViewById(R.id.singInButton);
        nameEditText = findViewById(R.id.firstnameEditText);
        surnameEditText = findViewById(R.id.surnameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phone = findViewById(R.id.phoneTextView);
    }

}