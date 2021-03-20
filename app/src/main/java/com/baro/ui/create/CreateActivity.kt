package com.baro.ui.create

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

import com.baro.R

class CreateActivity : AppCompatActivity() {
    private lateinit var courseEditText: EditText
    private lateinit var languageButton: ImageButton
    private lateinit var categoryButton: ImageButton
    private lateinit var createButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        configureCourseEditText()
        configureCategoryButton()
        configureLanguageButton()
        configureCreateButton()
    }

    private fun configureCourseEditText() {
        courseEditText = findViewById<EditText?>(R.id.edit_course_name)

        // TODO Implement this method. Should change the courseName field as user types in
    }

    private fun configureLanguageButton() {
        languageButton = findViewById<ImageButton?>(R.id.btn_language)
        languageButton.setOnClickListener(View.OnClickListener { v: View? -> })
    }

    private fun configureCategoryButton() {
        categoryButton = findViewById<ImageButton?>(R.id.btn_category)
        categoryButton.setOnClickListener(View.OnClickListener { v: View? -> })
    }

    private fun configureCreateButton() {
        createButton = findViewById<ImageButton?>(R.id.btn_create)
        createButton.setOnClickListener(View.OnClickListener { v: View? -> })
    }
}