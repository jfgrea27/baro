package com.baro.ui.create;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.baro.R;

public class CreateActivity extends AppCompatActivity {

    private EditText courseEditText;
    private ImageButton languageButton;
    private ImageButton categoryButton;
    private ImageButton createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        configureCourseEditText();
        configureCategoryButton();
        configureLanguageButton();
        configureCreateButton();
    }


    private void configureCourseEditText() {
        courseEditText = findViewById(R.id.edit_course_name);

       // TODO Implement this method. Should change the courseName field as user types in
    }

    private void configureLanguageButton() {
        languageButton = findViewById(R.id.btn_language);

        languageButton.setOnClickListener(v -> {
            // TODO Implement this method. Should somehow get users to select language
        });
    }


    private void configureCategoryButton() {
        categoryButton = findViewById(R.id.btn_category);

        categoryButton.setOnClickListener(v -> {
            // TODO Implement this method. Should somehow get users to select category - perhaps looking at the emojies on the keyboard?
        });
    }


    private void configureCreateButton() {
        createButton = findViewById(R.id.btn_create);

        createButton.setOnClickListener(v -> {


            // TODO get the language and categories - must discuss this
        });
    }


}