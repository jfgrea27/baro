package com.baro.ui.spalsh;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

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
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static androidx.core.os.BundleKt.bundleOf;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4ClassRunner.class)
public class ShareActivityNewLoggingTest {

    @Rule
    public ActivityTestRule<SplashActivity> mActivityTestRule = new ActivityTestRule<>(SplashActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.CAMERA",
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE");


    @Before
    public void deletePreviousUser() {

        Path userDirectoryPath = Paths.get(
                ApplicationProvider.getApplicationContext().getExternalFilesDir(null).toString(),
                FileEnum.USER_DIRECTORY.key);

        File userDirectoryFile = new File(userDirectoryPath.toString());

        FileHelper.deleteFile(userDirectoryFile);
    }

    @Ignore
    public static void mySleep (int val) {
        try {
            TimeUnit.SECONDS.sleep(val);
        } catch (InterruptedException e) {
            Log.e("SleepError" ,"Thread interrupted");
        }
    }

    @Test
    public void createUserCredentialsShouldPassIfCredentialsWereSaved() throws JSONException {

        mySleep (3);

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.edit_text_username),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container_view),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("valid_username"), closeSoftKeyboard());


        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.edit_text_password),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container_view),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("valid_password"), closeSoftKeyboard());


        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.btn_next),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container_view),
                                        0),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

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
        assertNotNull(jsonObject.get(JSONEnum.USER_UUID_KEY.key));
    }

    @Test
    public void createUserShouldNotCreateUserIfCredentialsAreNotStrongEnough() throws JSONException {

        mySleep (3);

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.edit_text_username),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container_view),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("false"), closeSoftKeyboard());


        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.edit_text_password),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container_view),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("valid_password"), closeSoftKeyboard());


        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.btn_next),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_container_view),
                                        0),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.btn_account),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        imageButton.check(doesNotExist());

        ViewInteraction imageButton2 = onView(
                allOf(withId(R.id.btn_share),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        imageButton2.check(doesNotExist());

        ViewInteraction imageButton3 = onView(
                allOf(withId(R.id.btn_create),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        imageButton3.check(doesNotExist());

        // File System
        Path userMetaFile = Paths.get(
                ApplicationProvider.getApplicationContext().getExternalFilesDir(null).toString(),
                FileEnum.USER_DIRECTORY.key,
                FileEnum.META_DATA_FILE.key);

        assertNull(FileHelper.getFileAtPath(userMetaFile));
    }

    @Test
    public void selectThumbnailPictureShouldSaveThumbnailPictureWhenCreatingUser() throws JSONException {
        // TODO



    }

    @Test
    public void selectCameraShouldSaveThumbnailPicture() throws JSONException {
        // TODO

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
