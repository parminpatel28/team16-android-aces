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



            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                friendRepository.getUserFriends(userId) { friends ->

                        var fetchedReviews : List<Review> = emptyList()


                        viewModelScope.launch {//worst code ever written please change this when you wake up tomorrow

                            friends.orEmpty().map { friend ->

                                async{
                                    reviewRepository.getReviewsByUser(friend.id) { revs ->
                                        if (revs != null) {
                                            fetchedReviews += revs
                                            _reviews.value = fetchedReviews
                                        }
                                    }

                                }


                            }
                        }



                        Log.d("HomeViewModel", "${fetchedReviews}")
                        _reviews.value = fetchedReviews

                }
            }

    }

    init {
        fetchReviews()
    }
}
