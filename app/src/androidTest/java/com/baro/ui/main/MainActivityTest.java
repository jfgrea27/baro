package com.baro.ui.main;


import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.baro.AccountActivity;
import com.baro.R;
import com.baro.ui.create.CreateActivity;
import com.baro.ui.main.MainActivity;
import com.baro.ui.share.ShareActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
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


        onView(ViewMatchers.withId(R.id.btn_create)).perform(click());

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
