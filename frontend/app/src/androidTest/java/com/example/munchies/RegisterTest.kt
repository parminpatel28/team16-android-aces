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
class RegisterActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun emptyFields_showsSnackbarError() {
        onView(withId(R.id.registerButton)).perform(click())

        onView(withId(R.id.createAccountBtn)).perform(click())

        onView(withText("Please fill in all fields"))
            .check(matches(isDisplayed()))
    }
}
