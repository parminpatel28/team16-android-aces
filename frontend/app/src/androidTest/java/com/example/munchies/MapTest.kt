package com.example.munchies

import android.widget.EditText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.example.munchies.ui.map.PlaceAdapter
import com.google.firebase.auth.FirebaseAuth
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MapFragmentTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @get:Rule
    val disableAnimationsRule = DisableAnimationsRule()

    @Before
    @After
    fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
    }

    @Test
    fun searchForLazeez_andClickWriteReview() {
        // Login
        performLogin("taylor@gmail.com")

        // Wait for app to load
        onView(isRoot()).perform(waitFor(3000))

        // Navigate to the Map tab
        onView(withId(R.id.navigation_map)).perform(click())

        // Wait for map to load
        onView(isRoot()).perform(waitFor(5000))
        
        // Type the search query
        onView(allOf(isAssignableFrom(EditText::class.java), isDescendantOfA(withId(R.id.searchView))))
            .perform(typeText("Lazeez Shawarma"), closeSoftKeyboard())

        // Wait for results to load
        onView(isRoot()).perform(waitFor(5000))

        // Click "Write Review" button on the first result
        onView(withId(R.id.searchResultsRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<PlaceAdapter.PlaceViewHolder>(
                    0,
                    clickChildViewWithId(R.id.writeReviewButton)
                )
            )

        // Confirm that the review screen opened and is prepopulated
        onView(withId(R.id.tagRestaurantsDropdown))
            .check(matches(withText(containsString("Lazeez Shawarma"))))
    }
}
