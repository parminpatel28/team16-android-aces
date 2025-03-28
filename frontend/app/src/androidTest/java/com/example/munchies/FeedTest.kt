package com.example.munchies

import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import com.google.firebase.auth.FirebaseAuth
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FeedTest {

    @get:Rule
    val activityRule = ActivityTestRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        FirebaseAuth.getInstance().signOut()
    }

    @After
    fun cleanUp() {
        FirebaseAuth.getInstance().signOut()
    }

    @Test
    fun canScrollThroughFeed() {
        performLogin("kailin@gmail.com")

        // Wait for feed to load
        onView(isRoot()).perform(waitFor(5000))

        onView(withId(R.id.recyclerFeed))
            .check(matches(isDisplayed()))

        // Scroll a bit
        onView(withId(R.id.recyclerFeed))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(3))
    }

    @Test
    fun clickingReview_opensReviewDetails() {
        performLogin("kailin@gmail.com")

        // Wait for feed to load
        onView(isRoot()).perform(waitFor(5000))

        onView(withId(R.id.recyclerFeed))
            .check(matches(isDisplayed()))

        // Click first review
        onView(withId(R.id.recyclerFeed))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // Confirm we're in ReviewDetailsActivity
        onView(withId(R.id.reviewText))
            .check(matches(isDisplayed()))
    }
}
