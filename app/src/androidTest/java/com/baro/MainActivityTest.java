package com.baro;


import android.app.Activity;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {



    @Rule
    public IntentsTestRule<MainActivity> activityRule
            = new IntentsTestRule<>(MainActivity.class);


    @Test
    public void clickOnCreateButtonShouldLaunchCreateActivity() {


        onView(withId(R.id.btn_create)).perform(click());

        intended(hasComponent(CreateActivity.class.getName()));
    }


    @Test
    public void clickOnAccountButtonShouldLaunchCreateActivity() {

        onView(withId(R.id.btn_account)).perform(click());

        intended(hasComponent(AccountActivity.class.getName()));
    }


    @Test
    public void clickOnShareButtonShouldLaunchCreateActivity() {

        onView(withId(R.id.btn_share)).perform(click());

        intended(hasComponent(ShareActivity.class.getName()));
    }

}
