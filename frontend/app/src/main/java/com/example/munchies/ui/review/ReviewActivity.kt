package com.example.munchies.ui.review

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.munchies.databinding.ActivityReviewBinding
import com.example.munchies.model.Review

class ReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewBinding
    private val reviewViewModel: ReviewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Leave a Review"

        binding.submitReviewButton.setOnClickListener {
            val overallRating = binding.overallRatingBar.rating.toInt()
            val reviewText = binding.reviewText.text.toString().trim()

            if (reviewText.isEmpty()) {
                Toast.makeText(this, "Please enter a review", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newReview = Review(
                reviewID = (0..1000).random(),
                poster = "Anonymous",
                caption = reviewText,
                restaurants = listOf("Unknown Restaurant"),
                location = null,
                date = "2024-02-26",
                rating = overallRating,
                likes = 0
            )

            reviewViewModel.addReview(newReview)
            Toast.makeText(this, "Review Submitted!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
