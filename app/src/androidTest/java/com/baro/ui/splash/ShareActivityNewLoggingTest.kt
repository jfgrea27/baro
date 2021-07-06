package com.baro.ui.splash

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.baro.R
import com.baro.constants.FileEnum
import com.baro.constants.JSONEnum
import com.baro.helpers.FileHelper
import com.baro.helpers.JSONHelper
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.json.JSONException
import org.junit.*
import org.junit.runner.RunWith
import java.io.File
import java.lang.Thread.sleep
import java.nio.file.Paths

@RunWith(AndroidJUnit4ClassRunner::class)
class ShareActivityNewLoggingTest {
    @get:Rule
    var mActivityTestRule: ActivityTestRule<SplashActivity?>? = ActivityTestRule(SplashActivity::class.java)

    @get:Rule
    var mGrantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
            "android.permission.CAMERA",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE")

    @Before
    fun deletePreviousUser() {
        val userDirectoryPath = Paths.get(
                ApplicationProvider.getApplicationContext<Context?>().getExternalFilesDir(null).toString(),
                FileEnum.USER_DIRECTORY.key)
        val userDirectoryFile = File(userDirectoryPath.toString())
        FileHelper.deleteFile(userDirectoryFile)
    }

    @Test
    @Throws(JSONException::class)
    fun createUserCredentialsShouldPassIfCredentialsWereSaved() {
        sleep(SplashActivity.SPLASH_TIME_OUT.toLong())
        val appCompatEditText = Espresso.onView(
                Matchers.allOf(ViewMatchers.withId(R.id.edit_text_username),
                        childAtPosition(
                                childAtPosition(
                                        ViewMatchers.withId(R.id.fragment_container_peer_connection),
                                        0),
                                2),
                        ViewMatchers.isDisplayed()))
        appCompatEditText.perform(ViewActions.replaceText("valid_username"), ViewActions.closeSoftKeyboard())

        val appCompatImageButton = Espresso.onView(
                Matchers.allOf(ViewMatchers.withId(R.id.btn_next),
                        childAtPosition(
                                childAtPosition(
                                        ViewMatchers.withId(R.id.fragment_container_peer_connection),
                                        0),
                                1),
                        ViewMatchers.isDisplayed()))
        appCompatImageButton.perform(ViewActions.click())
        val imageButton = Espresso.onView(
                Matchers.allOf(ViewMatchers.withId(R.id.im_account),
                        ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(android.R.id.content))),
                        ViewMatchers.isDisplayed()))
        imageButton.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        val imageButton2 = Espresso.onView(
                Matchers.allOf(ViewMatchers.withId(R.id.btn_share),
                        ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(android.R.id.content))),
                        ViewMatchers.isDisplayed()))
        imageButton2.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // File System
        val userDirectoryPath = Paths.get(
                ApplicationProvider.getApplicationContext<Context?>().getExternalFilesDir(null).toString(),
                FileEnum.USER_DIRECTORY.key,
                FileEnum.META_DATA_FILE.key)
        val userMetaFile = File(userDirectoryPath.toString())
        val content = FileHelper.readFile(userMetaFile)
        val jsonObject = content?.let { JSONHelper.createJSONFromString(it) }
        Assert.assertEquals(jsonObject?.get(JSONEnum.USER_NAME_KEY.key), "valid_username")
        Assert.assertNotNull(jsonObject?.get(JSONEnum.USER_UUID_KEY.key))
    }

    @Test
    @Throws(JSONException::class)
    fun createUserShouldNotCreateUserIfCredentialsAreNotStrongEnough() {
        sleep(SplashActivity.SPLASH_TIME_OUT.toLong())
        val appCompatEditText = Espresso.onView(
                Matchers.allOf(ViewMatchers.withId(R.id.edit_text_username),
                        childAtPosition(
                                childAtPosition(
                                        ViewMatchers.withId(R.id.fragment_container_peer_connection),
                                        0),
                                2),
                        ViewMatchers.isDisplayed()))
        appCompatEditText.perform(ViewActions.replaceText("false"), ViewActions.closeSoftKeyboard())

        val appCompatImageButton = Espresso.onView(
                Matchers.allOf(ViewMatchers.withId(R.id.btn_next),
                        childAtPosition(
                                childAtPosition(
                                        ViewMatchers.withId(R.id.fragment_container_peer_connection),
                                        0),
                                1),
                        ViewMatchers.isDisplayed()))
        appCompatImageButton.perform(ViewActions.click())
        val imageButton = Espresso.onView(
                Matchers.allOf(ViewMatchers.withId(R.id.im_account),
                        ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(android.R.id.content))),
                        ViewMatchers.isDisplayed()))
        imageButton.check(ViewAssertions.doesNotExist())
        val imageButton2 = Espresso.onView(
                Matchers.allOf(ViewMatchers.withId(R.id.btn_share),
                        ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(android.R.id.content))),
                        ViewMatchers.isDisplayed()))
        imageButton2.check(ViewAssertions.doesNotExist())


        // File System
        val userMetaFile = Paths.get(
                ApplicationProvider.getApplicationContext<Context?>().getExternalFilesDir(null).toString(),
                FileEnum.USER_DIRECTORY.key,
                FileEnum.META_DATA_FILE.key)
        Assert.assertNull(FileHelper.getFileAtPath(userMetaFile))
    }

//    @Test
//    @Throws(JSONException::class)
//    fun selectThumbnailPictureShouldSaveThumbnailPictureWhenCreatingUser() {
//        // TODO
//    }
//
//    @Test
//    @Throws(JSONException::class)
//    fun selectCameraShouldSaveThumbnailPicture() {
//        // TODO
//    }

    companion object {

        private fun childAtPosition(
                parentMatcher: Matcher<View?>?, position: Int): Matcher<View?>? {
            return object : TypeSafeMatcher<View?>() {
                override fun describeTo(description: Description?) {
                    description!!.appendText("Child at position $position in parent ")
                    parentMatcher!!.describeTo(description)
                }

                public override fun matchesSafely(view: View?): Boolean {
                    val parent = view?.getParent()
                    return (parent is ViewGroup && parentMatcher!!.matches(parent)
                            && view == (parent as ViewGroup).getChildAt(position))
                }
            }
        }
    }
}