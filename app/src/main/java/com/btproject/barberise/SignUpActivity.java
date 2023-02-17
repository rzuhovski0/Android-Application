package com.btproject.barberise;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.btproject.barberise.navigation.MenuActivity;

public class SignUpActivity extends AppCompatActivity {

    private Button saveEmailButton;
    private Button skipEmailButton;
    
    private EditText emailEditText;


    EncryptedSharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Objects.requireNonNull(getSupportActionBar()).hide();   //hide action bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        hideSystemUI();
        initializeButtons();

        skipEmailButton.setOnClickListener(view -> {
            Intent Menu = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(Menu);
        });

        saveEmailButton.setOnClickListener(view -> {
            if (TextUtils.isEmpty(emailEditText.getText().toString())) {
                // empty edit text
                Toast.makeText(getApplicationContext(), "Empty field not allowed!",
                        Toast.LENGTH_SHORT).show();
            }else if(!Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                // e-mail is invalid
                Toast.makeText(getApplicationContext(), "Email has an incorrect format!",
                        Toast.LENGTH_SHORT).show();
            }else{
                saveData();
                Intent Menu = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(Menu);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        String masterKeyAlias = null;
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize/open an instance of
        // EncryptedSharedPreferences on below line.
        try {
            // on below line initializing our encrypted
            // shared preferences and passing our key to it.
            EncryptedSharedPreferences sharedPreferences = (EncryptedSharedPreferences) EncryptedSharedPreferences.create(
                    // passing a file name to share a preferences
                    "preferences",
                    masterKeyAlias,
                    getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            // on below line creating a variable to
            // get the data from shared prefs.
            String name = sharedPreferences.getString("usrEmail", "");

            // on below line we are setting data
            // to our name and age edit text.
            emailEditText.setText(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveData() {
        // creating a master key for
        // encryption of shared preferences.
        String masterKeyAlias = null;
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Initialize/open an instance of
        // EncryptedSharedPreferences on below line.
        try {
            // on below line initializing our encrypted
            // shared preferences and passing our key to it.
            EncryptedSharedPreferences sharedPreferences = (EncryptedSharedPreferences) EncryptedSharedPreferences.create(
                    // passing a file name to share a preferences
                    "preferences",
                    masterKeyAlias,
                    getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            // on below line we are storing data in shared preferences file.
            sharedPreferences.edit().putString("usrEmail", emailEditText.getText().toString()).apply();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeButtons() {
        saveEmailButton = findViewById(R.id.sendSmsButton);
        skipEmailButton = findViewById(R.id.skipEmailButton);
        emailEditText = findViewById(R.id.phoneNoEditText);
    }

    private void hideSystemUI() {
        View decorView = this.getWindow().getDecorView();
        int uiOptions = decorView.getSystemUiVisibility();
        int newUiOptions = uiOptions;
        //newUiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        //newUiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE;
        newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(newUiOptions);
    }

}