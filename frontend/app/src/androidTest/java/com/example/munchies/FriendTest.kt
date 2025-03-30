package com.example.munchies

import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import com.google.firebase.auth.FirebaseAuth
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FriendTest {

    @get:Rule
    val activityRule = ActivityTestRule(LoginActivity::class.java)

    @Before
    @After
    fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
    }

    @Test
    fun viewFriendsList_displaysFriends() {
        loginAndNavigateToFriends()

        // Wait for list to load
        onView(isRoot()).perform(waitFor(2000))

        // Check at least one friend is shown in RecyclerView
        onView(withId(R.id.recyclerFriends))
            .check(matches(hasDescendant(withText("Matthew"))))
    }

    @Test
    fun viewPendingRequests_showsRequestList() {
        loginAndNavigateToFriends()

        onView(withId(R.id.btnFriendRequests)).perform(click())
        onView(isRoot()).perform(waitFor(2000))

        onView(allOf(
            withId(R.id.noFriendRequests),
            withText("You have no friend requests")
        )).check(matches(isDisplayed()))
    }

    @Test
    fun addFriend_addsToPending() {
        loginAndNavigateToFriends()

        // Tap add friends button
        onView(withId(R.id.btnAddFriends)).perform(click())

        // Tap "Add Friend"
        onView(withId(R.id.recyclerView)).perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                hasDescendant(withText("wafafwafwaf")),
                clickChildViewWithId(R.id.addFriendButton)
            )
        )
        onView(isRoot()).perform(waitFor(3000))

        // Check if friend request is pending
        onView(withId(R.id.recyclerView)).check(
            matches(hasDescendant(allOf(
                withId(R.id.removeFriendButton),
                withText("Cancel Request")
            )))
        )

        // Clean up: cancel request
        onView(withId(R.id.recyclerView)).perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                hasDescendant(withText("wafafwafwaf")),
                clickChildViewWithId(R.id.removeFriendButton)
            )
        )
        onView(isRoot()).perform(waitFor(3000))
    }

}
