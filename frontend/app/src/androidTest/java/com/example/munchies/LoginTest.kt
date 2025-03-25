package com.example.munchies

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import com.google.firebase.auth.FirebaseAuth
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

        // Now check for the feed recycler
        onView(withId(R.id.recyclerFeed))
            .check(matches(isDisplayed()))
    }
}
