package com.btproject.barberise.navigation.profile;

import static com.btproject.barberise.utils.LayoutUtils.getPx;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.btproject.barberise.R;

public class SetUpServicesTestingActivity extends AppCompatActivity {

    private EditText categoriesNumEditText;

    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_services_testing);

        categoriesNumEditText = findViewById(R.id.numOfCategoriesEditText);
        layout = findViewById(R.id.linearLayoutForCategories);

        categoriesNumEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                layout.removeAllViews();
                displayCategoriesEditTexts();
            }
        });



    }

    private void displayCategoriesEditTexts() {
        String numFields = categoriesNumEditText.getText().toString();
        int num = 0;
        if (!numFields.equals("")) {
            num = Integer.parseInt(numFields);

            for (int i = 0; i < num; i++) {
                EditText editText = (EditText) getLayoutInflater().inflate(R.layout.edit_text_custom, layout, false);
                editText.setId(View.generateViewId());

                customizeEditText(editText,i);

                layout.addView(editText);
            }
        }
    }

    private void customizeEditText(EditText editText,int i)
    {

        editText.setLayoutParams(new LinearLayout.LayoutParams(
                250,
                48, // Set height to 48dp
                1)); // Set weight to 1

//        editText.setMinWidth(getPx(150,getResources()));
        editText.setMinHeight(getPx(48,getResources()));
        editText.setHint("Category " + i);

        // Set layout parameters
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 25, 0, 25); // Set margins
        editText.setLayoutParams(layoutParams);
    }

}