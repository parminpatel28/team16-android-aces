package com.example.munchies

import android.view.View
import android.widget.RatingBar
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.Matcher
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement


fun waitFor(millis: Long): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> = isRoot()

        override fun getDescription(): String = "Wait for $millis milliseconds."

        override fun perform(uiController: UiController, view: View?) {
            uiController.loopMainThreadForAtLeast(millis)
        }
    }
}

fun performLogin(email: String, password: String = "munchies") {
    onView(withId(R.id.emailLogin))
        .perform(replaceText(email), closeSoftKeyboard())

    onView(withId(R.id.passwordLogin))
        .perform(replaceText(password), closeSoftKeyboard())

    onView(withId(R.id.loginBtn))
        .perform(click())
}

fun clickChildViewWithId(id: Int): ViewAction {
    return object : ViewAction {
        override fun getConstraints() = null
        override fun getDescription() = "Click on a child view with specified id."
        override fun perform(uiController: UiController, view: View) {
            view.findViewById<View>(id).performClick()
        }
    }
}

class DisableAnimationsRule : TestRule {
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                Runtime.getRuntime().exec(arrayOf("settings", "put", "global", "window_animation_scale", "0"))
                Runtime.getRuntime().exec(arrayOf("settings", "put", "global", "transition_animation_scale", "0"))
                Runtime.getRuntime().exec(arrayOf("settings", "put", "global", "animator_duration_scale", "0"))
                base.evaluate()
            }
        }
    }
}

fun loginAndNavigateToReviews(email: String = "taylor@gmail.com") {
    performLogin(email)

    // Wait for the main screen to load (feed)
    onView(isRoot()).perform(waitFor(2000))
    onView(withId(R.id.recyclerFeed))
        .check(matches(isDisplayed()))

    // Now go to the Reviews tab
    onView(withId(R.id.navigation_review))
        .perform(click())

    // Wait for the reviews page to load
    onView(isRoot()).perform(waitFor(1000))
    onView(withId(R.id.recyclerReviews))
        .check(matches(isDisplayed()))
}

fun setRating(rating: Float): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> = isAssignableFrom(RatingBar::class.java)
        override fun getDescription(): String = "Set RatingBar to $rating stars"
        override fun perform(uiController: UiController, view: View) {
            (view as RatingBar).rating = rating
        }
    }
}