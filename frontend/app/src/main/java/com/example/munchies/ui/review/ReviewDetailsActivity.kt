package com.example.munchies.ui.review

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.munchies.databinding.ActivityReviewDetailsBinding
import com.example.munchies.model.Review
import com.google.android.material.chip.Chip
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class ReviewDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewDetailsBinding

    private fun formatDate(instant: String): String {
        return try {
            val parsedInstant = Instant.parse(instant)
            val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")
                .withLocale(Locale.getDefault())
                .withZone(ZoneId.systemDefault())
            formatter.format(parsedInstant)
        } catch (e: Exception) {
            instant
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val review: Review? = intent.getParcelableExtra<Review>("review")

        review?.let {
//            binding.reviewerName.text = it.user // TODO: pull username from db
            binding.reviewText.text = it.caption
            binding.overallRatingBar.rating = it.rating.toFloat()
            binding.reviewDate.text = formatDate(it.date)
            binding.likesCount.text = it.likes.toString()

        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}
