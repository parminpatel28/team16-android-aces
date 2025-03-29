package com.example.munchies

import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.google.firebase.auth.FirebaseAuth
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import com.google.android.libraries.places.api.model.kotlin.localDate
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant

@RunWith(AndroidJUnit4::class)
class ReviewTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    @After
    fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
    }

    @Test
    fun swipeToRefresh_reviewsAreReloaded() {
        loginAndNavigateToReviews()

        onView(withId(R.id.swipeRefreshLayout)).perform(swipeDown())

        // RecyclerView is displayed and still has items
        onView(withId(R.id.recyclerReviews)).check(matches(isDisplayed()))
    }

    @Test
    fun clickReview_opensReviewDetails() {
        loginAndNavigateToReviews()

        // Wait for data to load
        onView(isRoot()).perform(waitFor(3000))

        // Click the first item in the list
        onView(withId(R.id.recyclerReviews))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // Optional: Check for a detail element (like caption or restaurant name)
        onView(withId(R.id.reviewText)).check(matches(isDisplayed()))
    }

    @Test
    fun clickAddReviewButton_opensReviewActivity() {
        loginAndNavigateToReviews()

        onView(withId(R.id.addReviewButton)).perform(click())

        // Check that you're now in the review activity (e.g., based on a unique view)
        onView(withId(R.id.overallRatingBar)).check(matches(isDisplayed()))
    }

    @Test
    fun submitNewReview_displaysInListAfterRefresh() {
        loginAndNavigateToReviews("elaine@gmail.com")

        // Tap the add review button
        onView(withId(R.id.addReviewButton)).perform(click())
        onView(isRoot()).perform(waitFor(1000))

        // Set rating
        onView(withId(R.id.overallRatingBar)).perform(setRating(4.0f))

        var unqiqueID = Instant.now()
        // Type in review text
        onView(withId(R.id.reviewText))
            .perform(replaceText("Espresso test review ${unqiqueID}"), closeSoftKeyboard())

        // Submit the review
        onView(withId(R.id.submitReviewButton)).perform(click())
        onView(isRoot()).perform(waitFor(3000)) // Wait for API & return


        // Refresh the list
        onView(withId(R.id.swipeRefreshLayout)).perform(swipeDown())
        onView(isRoot()).perform(waitFor(3000))

        // Confirm the review appears in the list
        onView(withText("Espresso test review ${unqiqueID}"))
            .check(matches(isDisplayed()))

    }

}