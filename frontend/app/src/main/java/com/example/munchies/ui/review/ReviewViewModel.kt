package com.example.munchies.ui.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.munchies.model.Review
import java.time.Instant

class ReviewViewModel : ViewModel() {

    private val _reviews = MutableLiveData<MutableList<Review>>(mutableListOf())
    val reviews: LiveData<MutableList<Review>> = _reviews

    init {
        // random seed review for now
        _reviews.value = mutableListOf(
            Review(
                1, "Test User", "Test Restaurant",
                location = "Location",
                date = Instant.now(),
                rating = 5.0,
            )
        )
    }

    fun addReview(review: Review) {
        val currentReviews = _reviews.value ?: mutableListOf()
        currentReviews.add(0, review)
        _reviews.value = currentReviews
    }
}
