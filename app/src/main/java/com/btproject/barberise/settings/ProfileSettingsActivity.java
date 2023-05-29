package com.btproject.barberise.settings;

import static com.btproject.barberise.utils.DatabaseUtils.getCurrentUser;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.btproject.barberise.R;
import com.btproject.barberise.VerifyPhoneNoActivity;
import com.btproject.barberise.database.clientDAO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProfileSettingsActivity extends AppCompatActivity {

    private Button signOutButton,removeAccButton,saveChangesButton;

    private EditText nameEditText,surnameEditText,emailEditText;
    private TextView phoneEditText;

    private FirebaseAuth mAuth;

    private FirebaseUser currentUser;

    private static HashMap<String,String> clientDataMap = new HashMap<>();
    private boolean animationApplied = false;

    private String newName, newSurname, newEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        initUI();

        setClientCredentials();


        removeAccButton.setOnClickListener(view ->
        {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                user.delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Account deleted successfully
                        mAuth.signOut();
                        // Add any additional code to handle the sign out process, such as navigating back to the login screen
                        startActivity(new Intent(ProfileSettingsActivity.this, VerifyPhoneNoActivity.class));
                        finish();   //removes current activity from backstack
                    } else {
                        Toast.makeText(getApplicationContext(),"Ups, something went wrong.. Try again.",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        signOutButton.setOnClickListener(view ->
        {
            mAuth.signOut();
            // Add any additional code to handle the sign out process, such as navigating back to the login screen
            startActivity(new Intent(ProfileSettingsActivity.this, VerifyPhoneNoActivity.class));
            finish();   //removes current activity from backstack
        });

        saveChangesButton.setOnClickListener(view ->
        {
            newName = nameEditText.getText().toString();
            if(!newName.equals(clientDataMap.get("name")))
                updateValue("name",newName);

            newSurname = surnameEditText.getText().toString();
            if(!newSurname.equals(clientDataMap.get("surname")))
                updateValue("surname",newSurname);

            newEmail = emailEditText.getText().toString();
            if(!newEmail.equals(clientDataMap.get("email")))
                updateValue("email",newEmail);

        });


    }

    private void updateValue(String value,String newValue)
    {
        DatabaseReference clientRef = FirebaseDatabase.getInstance().getReference("clients").child(currentUser.getUid()).child(value);
        clientRef.setValue(newValue).addOnCompleteListener(task -> {
            if(task.isSuccessful())
                Toast.makeText(getApplicationContext(),R.string.data_update,Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(),R.string.data_update_error,Toast.LENGTH_LONG).show();
        });
        setClientCredentials();
    }

    private void setTextChangedListenerForDataViews()
    {
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(animationApplied)
                    return;

                // Apply fadeIn animation to the button
                saveChangesButton.setVisibility(View.VISIBLE);
                Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                saveChangesButton.startAnimation(fadeIn);
                animationApplied = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        surnameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(animationApplied)
                    return;

                // Apply fadeIn animation to the button
                saveChangesButton.setVisibility(View.VISIBLE);
                Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                saveChangesButton.startAnimation(fadeIn);
                animationApplied = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(animationApplied)
                    return;

                // Apply fadeIn animation to the button
                saveChangesButton.setVisibility(View.VISIBLE);
                Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                saveChangesButton.startAnimation(fadeIn);
                animationApplied = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setClientCredentials()
    {
        clientDAO.DataExistsCallback dataExistsCallback = userExists -> {
            nameEditText.setText(clientDataMap.get("name"));
            surnameEditText.setText(clientDataMap.get("surname"));
            phoneEditText.setText(clientDataMap.get("phoneNo"));
            if(clientDataMap.get("email") != null)
                emailEditText.setText(clientDataMap.get("email"));
            setTextChangedListenerForDataViews();
        };
        getClientCredentialsFromDatabase(dataExistsCallback);

    }

    private void getClientCredentialsFromDatabase(clientDAO.DataExistsCallback dataExistsCallback)
    {

        currentUser = getCurrentUser();

        if(currentUser == null)
            return;

        DatabaseReference clientRef = FirebaseDatabase.getInstance().getReference("clients").child(currentUser.getUid());

        clientRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String surname = snapshot.child("surname").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                String phoneNo = snapshot.child("phoneNo").getValue(String.class);

                // Populate the clientDataMap with the retrieved data
                clientDataMap.put("name", name);
                clientDataMap.put("surname", surname);
                clientDataMap.put("email", email);
                clientDataMap.put("phoneNo", phoneNo);

                dataExistsCallback.onDataInvalid(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void initUI()
    {
        mAuth = FirebaseAuth.getInstance();

        signOutButton = findViewById(R.id.singInButton);
        removeAccButton = findViewById(R.id.deleteAccountButton);
        saveChangesButton = findViewById(R.id.saveChangesButton);

        // Initially, hide the button
        saveChangesButton.setVisibility(View.GONE);

        /**Set correct color for remove acc button*/
        setColor(removeAccButton,R.color.red);

        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null) {
            setColor(signOutButton, R.color.green);
            signOutButton.setText(R.string.sign_up);
        }

        nameEditText = findViewById(R.id.firstnameEditText);
        surnameEditText = findViewById(R.id.surnameEditText);
        phoneEditText = findViewById(R.id.phoneTextView);
        emailEditText = findViewById(R.id.emailEditText);

    }

    private void setColor(Button button, @ColorRes int id)
    {
        int color = ContextCompat.getColor(this, id);
        button.setBackgroundTintList(ColorStateList.valueOf(color));
    }

}
