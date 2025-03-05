package com.example.munchies.ui.review

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.munchies.model.Review
import com.example.munchies.repository.ReviewRepository
import java.time.Instant

class ReviewViewModel : ViewModel() {
    private val repository = ReviewRepository()

    private val _reviews = MutableLiveData<MutableList<Review>>(mutableListOf())
    val reviews: LiveData<MutableList<Review>> = _reviews

    init {
        _reviews.value = mutableListOf(
            Review(1, "Test User", "Test Restaurant", location = "Location", date = Instant.now(), rating = 5.0)
        )
    }

    fun submitReview(reviewData: Map<String, Any>) {
        repository.createReview(reviewData) { response ->
            if (response != null) {
                Log.d("ReviewViewModel", "Review submitted successfully!")
            } else {
                Log.e("ReviewViewModel", "Failed to submit review")
            }
        }
    }
}
