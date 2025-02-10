package com.example.munchies.ui.review

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.munchies.R
import com.example.munchies.model.Review

class ReviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        val reviewId = intent.getIntExtra("review_id", -1)

        // Simulated data (replace with real fetching logic)
        val review = getReviewById(reviewId)

        // Bind review details to UI
        findViewById<TextView>(R.id.userName).text = review?.user
        findViewById<TextView>(R.id.restaurantName).text = review?.restaurant
        findViewById<TextView>(R.id.reviewContent).text = review?.content

        // TODO: Images and timestamp?
    }

    // Simulated function to fetch a review (replace with backend call)
    private fun getReviewById(id: Int): Review? {
        val reviews = listOf(
            Review(1, "Friend #1", "McDonald's", "The junior chicken is really good!"),
            Review(2, "Friend #2", "Lazeez", "I <3 their chicken on the rocks!"),
            Review(3, "Friend #3", "Los Rolling Tacos", "5 stars for the birria tacos!"),
            Review(4, "Friend #4", "Nuri Village", "Just tried it... YUM!!"),
            Review(5, "Friend #5", "Kabob Hut", "Highly recommend the lamb skewers!"),
            Review(6, "Friend #6", "Taco Bell", "DO NOT TRY THE SUSHI XXX")
        )
        return reviews.find { it.id == id }
    }
}
