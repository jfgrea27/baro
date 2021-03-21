package com.baro.ui.main

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.baro.ui.account.AccountActivity
import com.baro.R
import com.baro.ui.learn.LearnActivity
import com.baro.ui.share.ShareActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class MainActivityTest {
    @get:Rule
     var activityRule: IntentsTestRule<MainActivity?>? = IntentsTestRule(MainActivity::class.java)

    @Test
    fun clickOnCreateButtonShouldLaunchCreateActivity() {
        Espresso.onView(ViewMatchers.withId(R.id.im_account)).perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(AccountActivity::class.java.name))
        Espresso.pressBack()
        sleep(0.5.toLong())
        Espresso.onView(ViewMatchers.withId(R.id.btn_share)).perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(ShareActivity::class.java.name))
        Espresso.pressBack()
        sleep(0.5.toLong())
        Espresso.onView(ViewMatchers.withId(R.id.btn_learn)).perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(LearnActivity::class.java.name))
    }

}