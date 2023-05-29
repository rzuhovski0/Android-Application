package com.btproject.barberise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class AppSettingsActivity extends AppCompatActivity {

    private TextView switchLangTextView, subscribeNewsTextView;

    private String currentLanguage;
    private static final String LANGUAGE_ENGLISH = "en";
    private static final String LANGUAGE_SLOVAK = "sk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);

        switchLangTextView = findViewById(R.id.switchLangTextView);
        subscribeNewsTextView = findViewById(R.id.subscribeNewsTextView);

        switchLangTextView.setOnClickListener(view -> {
//            updateLanguage();
            Toast.makeText(getApplicationContext(),R.string.error_lang,Toast.LENGTH_LONG).show();
        });

        subscribeNewsTextView.setOnClickListener(view -> {
//            updateLanguage();
            Toast.makeText(getApplicationContext(),R.string.news_error,Toast.LENGTH_LONG).show();
        });
    }


    private void updateLanguage() {
//        if (currentLanguage.equals(LANGUAGE_ENGLISH)) {
//            setAppLanguage(LANGUAGE_SLOVAK);
//            currentLanguage = LANGUAGE_SLOVAK;
//        } else {
//            setAppLanguage(LANGUAGE_ENGLISH);
//            currentLanguage = LANGUAGE_ENGLISH;
//        }
    }

//    public void setAppLanguage(String languageCode) {
//        Locale locale = new Locale(languageCode);
//        Configuration configuration = getResources().getConfiguration();
//        configuration.setLocale(locale);
//
//        Context context = createConfigurationContext(configuration);
//        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
//
//        // Save the selected language preference if needed
//
//        // Restart the activity or refresh UI elements to reflect the language change
//        recreate();
//    }



}