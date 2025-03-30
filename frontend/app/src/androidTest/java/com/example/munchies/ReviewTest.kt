package com.example.munchies

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.Instrumentation
import android.content.Intent
import android.os.Environment
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasType
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.google.firebase.auth.FirebaseAuth
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.time.Instant

@RunWith(AndroidJUnit4::class)
class ReviewTest {

    @get:Rule
    val activityRule = ActivityTestRule(LoginActivity::class.java)

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
        loginAndNavigateToReviews()

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

    @Test
    fun submitNewReviewWithImage() {
        Intents.init()

        val context = InstrumentationRegistry.getInstrumentation().targetContext

        loginAndNavigateToReviews()

        // Prepare image for upload
        val destFile = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "test_image.jpg"
        )
        copyAssetToExternalStorage(context, "test_image.jpg", destFile.absolutePath)

        // Stub the image picker intent
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",  // <- Confirm this matches your Manifest provider
            destFile
        )

        context.grantUriPermission(
            context.packageName,
            uri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        )

        val resultIntent = Intent().apply {
            data = uri
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        }
        intending(allOf(
            hasAction(Intent.ACTION_GET_CONTENT),
            hasType("image/*")
        )).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, resultIntent)
        )

        // Tap the add review button
        onView(withId(R.id.addReviewButton)).perform(click())
        onView(isRoot()).perform(waitFor(2000))

        // Tap the button that launches the image picker
        onView(withId(R.id.btnChooseFile)).perform(click())

        // Fill out the rest of the review
        onView(withId(R.id.overallRatingBar)).perform(setRating(4.0f))

        val uniqueID = Instant.now()
        onView(withId(R.id.reviewText))
            .perform(replaceText("Espresso test image review $uniqueID"), closeSoftKeyboard())

        // Submit the review
        onView(withId(R.id.submitReviewButton)).perform(click())
        onView(isRoot()).perform(waitFor(3000)) // Wait for network/API return

        // Refresh the list
        onView(withId(R.id.swipeRefreshLayout)).perform(swipeDown())
        onView(isRoot()).perform(waitFor(3000))

        // Confirm the review appears
        onView(withText("Espresso test review $uniqueID"))
            .check(matches(isDisplayed()))

        // Click on review
        onView(withText("Espresso test review $uniqueID"))
            .perform(click())
        onView(isRoot()).perform(waitFor(3000))

        // Check review details
        onView(withId(R.id.reviewText))
            .check(matches(withText("Espresso test review $uniqueID")))

        onView(withId(R.id.overallRatingBar))
            .check(matches(withRating(4.0f)))

        onView(withId(R.id.photosContainer))
            .check(matches(hasDescendant(isAssignableFrom(ImageView::class.java))))

        Intents.release()
    }
}