package com.btproject.barberise.settings;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.btproject.barberise.R;

public class ProfileSettingsActivity extends AppCompatActivity {

    private Button signOutButton,removeAccButton;

    private EditText nameEditText,surnameEditText,emailEditText,phoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        initItems();

        //TODO implement auto save if user changed EditTexts

        removeAccButton.setOnClickListener(view ->
        {
            Toast.makeText(getApplicationContext(),"Not implemented yet",Toast.LENGTH_SHORT).show();
        });

        signOutButton.setOnClickListener(view ->
        {
            Toast.makeText(getApplicationContext(),"Not implemented yet",Toast.LENGTH_SHORT).show();
        });

    }

    private void initItems()
    {
        signOutButton = findViewById(R.id.signOutButton);
        removeAccButton = findViewById(R.id.deleteAccountButton);
        /**Set correct color for remove acc button*/
        setColor(removeAccButton,R.color.red);

        nameEditText = findViewById(R.id.firstnameEditText);
        surnameEditText = findViewById(R.id.surnameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);

    }

    private void setColor(Button button, @ColorRes int id)
    {
        int redColor = ContextCompat.getColor(this, id);
        button.setBackgroundTintList(ColorStateList.valueOf(redColor));
    }

}
