package com.example.munchies.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchies.api.ApiClient.userService
import com.example.munchies.model.Review
import com.example.munchies.repository.FriendRepository
import com.example.munchies.repository.ReviewRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant

class HomeViewModel : ViewModel() {

    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> = _reviews

    private val reviewRepository = ReviewRepository()
    private val friendRepository = FriendRepository()

    private fun fetchReviews() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        friendRepository.getUserFriends(userId) { friends ->
            viewModelScope.launch {
                val allReviews = friends.orEmpty().mapNotNull { friend ->
                    try {
                        reviewRepository.getReviewsByUser(friend.id)
                    } catch (e: Exception) {
                        Log.e("HomeViewModel", "Failed to fetch reviews for ${friend.id}", e)
                        null
                    }
                }.flatten()

                _reviews.value = allReviews
                Log.d("HomeViewModel", "Fetched ${allReviews.size} total reviews")
            }
        }
    }

    init {
        fetchReviews()
    }
}
