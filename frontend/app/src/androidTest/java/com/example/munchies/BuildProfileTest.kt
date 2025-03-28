package com.example.munchies

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileBuilderActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(ProfileBuilderActivity::class.java)

    @Test
    fun allViewsDisplayed() {
        onView(withId(R.id.nameEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.usernameEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.bioEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.emailEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.profilePictureImageView)).check(matches(isDisplayed()))
        onView(withId(R.id.finishButton)).check(matches(isDisplayed()))
    }

    @Test
    fun submitWithEmptyFields_showsError() {
        onView(withId(R.id.finishButton)).perform(click())
        onView(withText("Please fill in all required fields"))
            .check(matches(isDisplayed()))
    }
}
