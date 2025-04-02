package com.example.munchies.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchies.api.ApiClient.userService
import com.example.munchies.api.UserService
import com.example.munchies.model.Review
import com.example.munchies.repository.FriendRepository
import com.example.munchies.repository.ReviewRepository
import com.example.munchies.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.time.Instant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeViewModel : ViewModel() {

    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> = _reviews
    private val _savedReviewids = MutableLiveData<List<Int>>()
    val savedReviewids: LiveData<List<Int>> = _savedReviewids
    private val _refresh = MutableLiveData<Boolean>()
    val refresh: LiveData<Boolean> = _refresh

    private val reviewRepository = ReviewRepository()
    private val friendRepository = FriendRepository()
    private val userRepository = UserRepository()
    private val userService: UserService


    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    init {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d("ProfileViewModel", "OkHttp: $message")
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/") // Android emulator localhost
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        userService = retrofit.create(UserService::class.java)
        refreshFeed()
    }
    fun likeReview(review: Review){
        if (review.reviewID != null && userId != null){
            if(review.liked == null){
                reviewRepository.likeReview(review.reviewID)
                review.liked = true

            }
            else if (review.liked!!){
                reviewRepository.dislikeReview(review.reviewID)
                review.liked = false
            }
            else{
                reviewRepository.likeReview(review.reviewID)
                review.liked = true
            }


            userRepository.updateLikes(userId, review)

        }

    }
    fun refreshFeed() {
        _refresh.value = true
        loadUserData()
        fetchReviews()
        _refresh.value = false
    }


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

                allReviews.forEach { review ->
                    review.liked = savedReviewids.value?.contains(review.reviewID)
                }

                _reviews.value = allReviews
                Log.d("HomeViewModel", "Fetched ${allReviews.size} total reviews")
            }
        }
    }

    private fun loadUserData() {


        Log.d("HomeViewModel", "Loading user data")

        viewModelScope.launch {
            try {
                if (userId != null){
                    val response = userService.getUserById(userId)
                    Log.d("HomeViewModel", "Response code: ${response.code()}")
                    if (response.isSuccessful) {
                        response.body()?.let { user ->
                            Log.d("HomeViewModel", "User data received: $user")
                            _savedReviewids.value = user.savedReviews?.map {
                                it.reviewID!!
                            }

                        } ?: run {
                            Log.e("HomeViewModel", "Response body is null")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("HomeViewModel", "Error response: $errorBody")
                    }

                }


            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception loading user data", e)

                e.printStackTrace()
            }
        }
    }


}
