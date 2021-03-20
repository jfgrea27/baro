package com.baro.ui.spalsh;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewInteraction;
import androidx.test.rule.ActivityTestRule;

import com.baro.R;
import com.baro.constants.FileEnum;
import com.baro.constants.JSONEnum;
import com.baro.helpers.FileHelper;
import com.baro.helpers.JSONHelper;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SplashActivityAlreadyLoggedInUserTest {

    String VALID_UUID = "486d063c-58bf-4105-86ad-62f91036126c";

    @Rule
    public ActivityTestRule<SplashActivity> mActivityTestRule = new ActivityTestRule<>(SplashActivity.class);

    @Before
    public void setUpUser() {

        Path userMetadataPath = Paths.get(
                ApplicationProvider.getApplicationContext().getExternalFilesDir(null).toString(),
                FileEnum.USER_DIRECTORY.key,
                FileEnum.META_DATA_FILE.key);

        File userMetadataFile = new File(userMetadataPath.toString());

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(JSONEnum.USER_UUID_KEY.key, VALID_UUID);
        hashMap.put(JSONEnum.USER_NAME_KEY.key, "valid_username");
        JSONObject jsonObject = JSONHelper.createJSONFromHashMap(hashMap);

        FileHelper.writeToFile(userMetadataFile, jsonObject.toString());

    }

    public static void mySleep (int val) {
        try {
            TimeUnit.SECONDS.sleep(val);
        } catch (InterruptedException e) {
            Log.e("SleepError" ,"Thread interrupted");
        }
    }

    @Test
    public void createUserTest() throws JSONException {

        mySleep (3);

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.btn_account),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction imageButton2 = onView(
                allOf(withId(R.id.btn_share),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        imageButton2.check(matches(isDisplayed()));

        ViewInteraction imageButton3 = onView(
                allOf(withId(R.id.btn_create),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        imageButton3.check(matches(isDisplayed()));

        // File System
        Path userDirectoryPath = Paths.get(
                ApplicationProvider.getApplicationContext().getExternalFilesDir(null).toString(),
                FileEnum.USER_DIRECTORY.key,
                FileEnum.META_DATA_FILE.key);

        File userMetaFile = new File(userDirectoryPath.toString());

        String content = FileHelper.readFile(userMetaFile);
        JSONObject jsonObject = JSONHelper.createJSONFromString(content);

        assertEquals(jsonObject.get(JSONEnum.USER_NAME_KEY.key), "valid_username");
        assertEquals(jsonObject.get(JSONEnum.USER_UUID_KEY.key), VALID_UUID);
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