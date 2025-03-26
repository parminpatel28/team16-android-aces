package com.example.munchies

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import com.google.firebase.auth.FirebaseAuth
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(LoginActivity::class.java)

    @Before
    fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
    }

    @Test
    fun loginWithValidCredentials_showsFeed() {
        // Input email
        onView(withId(R.id.emailLogin))
            .perform(replaceText("taylor@gmail.com"), closeSoftKeyboard())

        // Input password
        onView(withId(R.id.passwordLogin))
            .perform(replaceText("munchies"), closeSoftKeyboard())

        // Tap login
        onView(withId(R.id.loginBtn))
            .perform(click())

        onView(isRoot()).perform(waitFor(5000))

        // Check for the feed recycler
        onView(withId(R.id.recyclerFeed))
            .check(matches(isDisplayed()))
    }

    @Test
    fun loginWithInvalidCredentials_showsError() {
        // Input invalid email
        onView(withId(R.id.emailLogin))
            .perform(replaceText("user@gmail.com"), closeSoftKeyboard())

        // Input invalid password
        onView(withId(R.id.passwordLogin))
            .perform(replaceText("password"), closeSoftKeyboard())

        // Tap login
        onView(withId(R.id.loginBtn))
            .perform(click())

        onView(isRoot()).perform(waitFor(2000))

        onView(allOf(withText("Login failed"), withParent(withId(com.google.android.material.R.id.snackbar_text))))
            .check(matches(isDisplayed()))

        // Still on login screen — check that login button is visible
        onView(withId(R.id.loginBtn)).check(matches(isDisplayed()))
    }
}
