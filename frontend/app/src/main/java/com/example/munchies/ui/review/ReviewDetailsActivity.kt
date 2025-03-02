package com.example.munchies.ui.review

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.munchies.databinding.ActivityReviewDetailsBinding
import com.example.munchies.model.Review

class ReviewDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val review: Review? = intent.getParcelableExtra<Review>("review")

        review?.let {
            binding.reviewText.text = it.caption
            binding.restaurantName.text = it.restaurants.joinToString()
            binding.overallRatingBar.rating = it.rating.toFloat()
//            binding.reviewDate.text = it.date
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}
