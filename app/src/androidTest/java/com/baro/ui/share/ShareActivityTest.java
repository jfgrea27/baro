package com.baro.ui.share;


import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.baro.R;
import com.baro.ui.spalsh.SplashActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ShareActivityTest {

    @Rule
    public ActivityTestRule<SplashActivity> mActivityTestRule = new ActivityTestRule<>(SplashActivity.class);


    public static void mySleep (int val) {
        try {
            TimeUnit.SECONDS.sleep(val);
        } catch (InterruptedException e) {
            Log.e("SleepError" ,"Thread interrupted");
        }
    }

    @Test
    public void shareActivityTest() {

        boolean shareDoesNotExist = true;


        while (shareDoesNotExist) {
            try {
                mySleep (1);
                ViewInteraction attemptClick = onView(
                        allOf(withId(R.id.btn_share),
                                childAtPosition(
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0),
                                        1),
                                isDisplayed()));
                attemptClick.perform(click());
                shareDoesNotExist = false;
            } catch (androidx.test.espresso.NoMatchingViewException e) {
                mySleep (1);
                return;
            }
        }


        ViewInteraction imageButton = onView(
                allOf(withId(R.id.btn_internet),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction imageButton2 = onView(
                allOf(withId(R.id.btn_local),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        imageButton2.check(matches(isDisplayed()));

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.btn_internet),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        pressBack();

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.btn_local),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction imageButton3 = onView(
                allOf(withId(R.id.btn_receive),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        imageButton3.check(matches(isDisplayed()));

        ViewInteraction imageButton4 = onView(
                allOf(withId(R.id.btn_send),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        imageButton4.check(matches(isDisplayed()));

        ViewInteraction appCompatImageButton4 = onView(
                allOf(withId(R.id.btn_receive),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageButton4.perform(click());

        pressBack();
        System.out.println("Reached Send");

        ViewInteraction appCompatImageButton5 = onView(
                allOf(withId(R.id.btn_send),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatImageButton5.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
