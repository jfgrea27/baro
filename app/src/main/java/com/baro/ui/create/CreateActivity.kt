package com.baro.ui.create

import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

import com.baro.R
import com.baro.constants.CategoryEnum
import com.baro.constants.FileEnum
import com.baro.constants.JSONEnum
import com.baro.constants.LanguageEnum
import com.baro.helpers.FileHelper
import com.baro.helpers.JSONHelper
import com.baro.models.Course
import com.baro.models.User
import java.io.File
import java.nio.file.Paths
import java.util.*
import kotlin.collections.HashMap

class CreateActivity : AppCompatActivity() {
    private lateinit var courseEditText: EditText
    private lateinit var languageButton: ImageButton
    private lateinit var categoryButton: ImageButton
    private lateinit var createButton: ImageButton

    private var user: User? = null
    // TODO change these when we have more categories and languages
    private var categoryEnum: CategoryEnum? = CategoryEnum.AGRICULTURE
    private var languageEnum: LanguageEnum? = LanguageEnum.ENGLISH

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        configureCourseEditText()
        configureCategoryButton()
        configureLanguageButton()
        configureCreateButton()
    }

    private fun configureCourseEditText() {
        courseEditText = findViewById(R.id.edit_course_name)
    }

    private fun configureLanguageButton() {
        languageButton = findViewById(R.id.btn_language)
        languageButton.setOnClickListener {

        }
    }

    private fun configureCategoryButton() {
        categoryButton = findViewById(R.id.btn_category)
        categoryButton.setOnClickListener {

        }
    }

    private fun configureCreateButton() {
        createButton = findViewById(R.id.btn_create)
        createButton.setOnClickListener {

            val userCredentialsRetrieve = UserCredentialsRetrieve()
            userCredentialsRetrieve.execute()
            if (user != null &&
                    categoryEnum != null &&
                    languageEnum != null &&
                    courseEditText.text.toString().length > 5) {
                        // TODO refactor this so that the user is guided in creating a course
                val courseCreation = CourseCreation()
                courseCreation.execute()
            }
        }
    }


    private inner class CourseCreation : AsyncTask<Void?, Void?, Boolean?>() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        override fun doInBackground(vararg voids: Void?): Boolean? {
            createCourse()
            return true
        }


        @RequiresApi(api = Build.VERSION_CODES.O)
        private fun createCourse(): Boolean {
            val course = Course(courseEditText.text.toString(), UUID.randomUUID(), user, languageEnum, categoryEnum)

            return saveCourseFileSystem(course)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun saveCourseFileSystem(course: Course): Boolean {

            val courseMetaPath = Paths.get(
                    getExternalFilesDir(null).toString(),
                    FileEnum.COURSE_DIRECTORY.key,
                    course.getCourseUUID().toString(),
                    FileEnum.META_DATA_FILE.key)


            val courseMetaFile = File(courseMetaPath.toString())
            val courseHashMap = HashMap<String?, String?>()
            courseHashMap[JSONEnum.USER_UUID_KEY.key!!] = course.getCreator()?.getUserUUID().toString()
            courseHashMap[JSONEnum.COURSE_NAME_KEY.key!!] = course.getCourseName()
            courseHashMap[JSONEnum.COURSE_UUID_KEY.key!!] = course.getCourseUUID().toString()
            courseHashMap[JSONEnum.COURSE_LANGUAGE.key!!] = course.getCourseLanguage().toString()
            courseHashMap[JSONEnum.COURSE_CATEGORY.key!!] = course.getCourseCategory().toString()
            val content = FileHelper.readFile(courseMetaFile)
            val jsonObject = content?.let { JSONHelper.createJSONFromString(it) }
            FileHelper.writeToFile(courseMetaFile, jsonObject.toString())
            return true
        }
    }

    private inner class UserCredentialsRetrieve : AsyncTask<Void?, Void?, Boolean?>() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        override fun doInBackground(vararg voids: Void?): Boolean? {
            user = retrieveUserCredentials()
            return true
        }


        @RequiresApi(api = Build.VERSION_CODES.O)
        private fun retrieveUserCredentials(): User {
            val userMetaPath = Paths.get(
                    getExternalFilesDir(null).toString(),
                    FileEnum.USER_DIRECTORY.key,
                    FileEnum.META_DATA_FILE.key)
            val userMetaFile = File(userMetaPath.toString())
            val content = FileHelper.readFile(userMetaFile)
            val jsonObject = content?.let { JSONHelper.createJSONFromString(it) }

            return  User(
                    UUID.fromString(jsonObject?.get(JSONEnum.USER_UUID_KEY.key).toString()),
                    jsonObject?.get(JSONEnum.USER_NAME_KEY.key).toString()
            )
        }
    }
}