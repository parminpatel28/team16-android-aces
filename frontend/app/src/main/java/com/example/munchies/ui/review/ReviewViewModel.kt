package com.example.munchies.ui.review

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.munchies.model.Review
import com.example.munchies.repository.ReviewRepository

class ReviewViewModel : ViewModel() {
    private val repository = ReviewRepository()

    private val _reviews = MutableLiveData<MutableList<Review>>(mutableListOf())
    val reviews: LiveData<MutableList<Review>> = _reviews

    fun submitReview(reviewData: Review, onResult: (Review?) -> Unit) {
        repository.createReview(reviewData) { response ->
            if (response != null) {
                Log.d("SubmitReview", response.toString())
                Log.d("ReviewViewModel", "Review submitted successfully!")
                onResult(response)  // <-- Pass the review with reviewID to UI
            } else {
                Log.e("ReviewViewModel", "Failed to submit review")
                onResult(null)
            }
        }
    }

}
