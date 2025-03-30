package com.example.munchies

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import com.google.firebase.auth.FirebaseAuth
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Before @After
    fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
    }

    @Before
    fun setUp() {
        onView(withId(R.id.loginButton)).perform(click())
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

        onView(isRoot()).perform(waitFor(1500))

        onView(withText("Login failed"))
            .check(matches(isDisplayed()))

        // Still on login screen — check that login button is visible
        onView(withId(R.id.loginBtn)).check(matches(isDisplayed()))
    }

    @Test
    fun loginWithEmptyField_showsError() {
        // Input email
        onView(withId(R.id.emailLogin))
            .perform(replaceText("user@gmail.com"), closeSoftKeyboard())

        // Tap login
        onView(withId(R.id.loginBtn))
            .perform(click())

        onView(isRoot()).perform(waitFor(1500))

        onView(withText("Please fill in all fields"))
            .check(matches(isDisplayed()))

        // Still on login screen — check that login button is visible
        onView(withId(R.id.loginBtn)).check(matches(isDisplayed()))
    }

    @Test
    fun successfullyLogout() {
        performLogin("taylor@gmail.com")

        onView(isRoot()).perform(waitFor(2000))

        // Wait for the MainActivity to load by checking a known view
        onView(withId(R.id.recyclerFeed))
            .check(matches(isDisplayed()))

        // Then try clicking the profile tab
        onView(withId(R.id.navigation_profile))
            .perform(click())

        // Wait for profile content
        onView(withId(R.id.btnLogout))
            .check(matches(isDisplayed()))

        // Click the Logout button
        onView(withId(R.id.btnLogout)).perform(click())

        // Confirm the AlertDialog
        onView(withText("Confirm")).inRoot(isDialog()).perform(click())

        // Wait for navigation
        onView(isRoot()).perform(waitFor(2000))

        // Check that we're back on the Login screen
        onView(withId(R.id.loginBtn)).check(matches(isDisplayed()))
    }
}
