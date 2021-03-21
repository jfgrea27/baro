package com.baro.ui.share

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.baro.R
import com.baro.ui.main.MainActivity
import com.baro.ui.splash.SplashActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep
import java.util.concurrent.TimeUnit

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class ShareActivityTest {
    @get:Rule
    var mActivityTestRule: ActivityTestRule<MainActivity?>? = ActivityTestRule(MainActivity::class.java)

    @Test
    fun shareActivityTest() {

        val attemptClick = Espresso.onView(
                Matchers.allOf(ViewMatchers.withId(R.id.btn_share),
                        childAtPosition(
                                childAtPosition(
                                        ViewMatchers.withId(android.R.id.content),
                                        0),
                                1),
                        ViewMatchers.isDisplayed()))
        attemptClick.perform(ViewActions.click())

        val imageButton = Espresso.onView(
                Matchers.allOf(ViewMatchers.withId(R.id.btn_internet),
                        ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(android.R.id.content))),
                        ViewMatchers.isDisplayed()))
        imageButton.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        val imageButton2 = Espresso.onView(
                Matchers.allOf(ViewMatchers.withId(R.id.btn_local),
                        ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(android.R.id.content))),
                        ViewMatchers.isDisplayed()))
        imageButton2.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        val appCompatImageButton2 = Espresso.onView(
                Matchers.allOf(ViewMatchers.withId(R.id.btn_internet),
                        childAtPosition(
                                childAtPosition(
                                        ViewMatchers.withId(android.R.id.content),
                                        0),
                                0),
                        ViewMatchers.isDisplayed()))
        appCompatImageButton2.perform(ViewActions.click())
        Espresso.pressBack()
        val appCompatImageButton3 = Espresso.onView(
                Matchers.allOf(ViewMatchers.withId(R.id.btn_local),
                        childAtPosition(
                                childAtPosition(
                                        ViewMatchers.withId(android.R.id.content),
                                        0),
                                1),
                        ViewMatchers.isDisplayed()))
        appCompatImageButton3.perform(ViewActions.click())
        val imageButton3 = Espresso.onView(
                Matchers.allOf(ViewMatchers.withId(R.id.btn_receive),
                        ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(android.R.id.content))),
                        ViewMatchers.isDisplayed()))
        imageButton3.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        val imageButton4 = Espresso.onView(
                Matchers.allOf(ViewMatchers.withId(R.id.btn_send),
                        ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(android.R.id.content))),
                        ViewMatchers.isDisplayed()))
        imageButton4.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        val appCompatImageButton4 = Espresso.onView(
                Matchers.allOf(ViewMatchers.withId(R.id.btn_receive),
                        childAtPosition(
                                childAtPosition(
                                        ViewMatchers.withId(android.R.id.content),
                                        0),
                                0),
                        ViewMatchers.isDisplayed()))
        appCompatImageButton4.perform(ViewActions.click())
        Espresso.pressBack()
        val appCompatImageButton5 = Espresso.onView(
                Matchers.allOf(ViewMatchers.withId(R.id.btn_send),
                        childAtPosition(
                                childAtPosition(
                                        ViewMatchers.withId(android.R.id.content),
                                        0),
                                1),
                        ViewMatchers.isDisplayed()))
        appCompatImageButton5.perform(ViewActions.click())
    }

    companion object {

        private fun childAtPosition(
                parentMatcher: Matcher<View?>?, position: Int): Matcher<View?>? {
            return object : TypeSafeMatcher<View?>() {
                override fun describeTo(description: Description?) {
                    description?.appendText("Child at position $position in parent ")
                    parentMatcher?.describeTo(description)
                }

                public override fun matchesSafely(view: View?): Boolean {
                    val parent = view?.parent
                    return (parent is ViewGroup && parentMatcher?.matches(parent)!! && view == (parent as ViewGroup).getChildAt(position))
                }
            }
        }
    }
}