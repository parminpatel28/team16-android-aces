package com.example.munchies.ui.review

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.munchies.api.UserService
import com.example.munchies.model.Review
import com.example.munchies.repository.ReviewRepository
import com.example.munchies.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ReviewViewModel : ViewModel() {
    private val reviewRepository = ReviewRepository()
    private val userRepository = UserRepository()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    private val _refresh = MutableLiveData<Boolean>()
    val refresh: LiveData<Boolean> = _refresh
    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> = _reviews
    private val _savedReviewids = MutableLiveData<List<Int>>()
    val savedReviewids: LiveData<List<Int>> = _savedReviewids

    private val userService: UserService
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

    fun submitReview(reviewData: Review, onResult: (Review?) -> Unit) {
        reviewRepository.createReview(reviewData) { response ->
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

    fun likeReview(review: Review){
        if (review.reviewID != null && userId!= null){
            reviewRepository.likeReview(review.reviewID)

            userRepository.updateLikes(userId, review)

        }

    }

    fun refreshFeed(){
        _refresh.value = true
        loadUserData()
        fetchReviewsByUser()
        _refresh.value = false
    }

    private fun fetchReviewsByUser() {
        _refresh.value = true
        viewModelScope.launch {
            if(userId != null){
                val reviews = withContext(Dispatchers.IO) {
                    reviewRepository.getReviewsByUser(userId)
                }


                if (reviews != null) {
                    reviews.forEach{ review: Review -> run {
                            review.liked = savedReviewids.value?.contains(review.reviewID)

                        }
                    }
                    _reviews.value = reviews!!
                } else {
                    Log.e("ReviewFragment", "Failed to fetch reviews")
                }



            }

        }
        _refresh.value = false
    }

    private fun loadUserData() {


        Log.d("HomeViewModel", "Loading user data")

        viewModelScope.launch {
            try {
                if (userId != null){
                    val response = userService.getUserById(userId)
                    Log.d("ReviewViewModel", "Response code: ${response.code()}")
                    if (response.isSuccessful) {
                        response.body()?.let { user ->
                            Log.d("ReviewViewModel", "User data received: $user")
                            _savedReviewids.value = user.savedReviews?.map {
                                it.reviewID!!
                            }

                        } ?: run {
                            Log.e("ReviewViewModel", "Response body is null")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("ReviewViewModel", "Error response: $errorBody")
                    }

                }


            } catch (e: Exception) {
                Log.e("ReviewViewModel", "Exception loading user data", e)

                e.printStackTrace()
            }
        }
    }

}
