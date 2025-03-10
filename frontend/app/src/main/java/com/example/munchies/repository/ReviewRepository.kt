package com.example.munchies.repository

import android.util.Log
import com.example.munchies.api.ApiClient
import com.example.munchies.model.Review
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.gson.Gson

class ReviewRepository {

    private val apiService = ApiClient.reviewService

    fun createReview(review: Review, onResult: (Review?) -> Unit) {
        apiService.createReview(review).enqueue(object : Callback<Review> {
            override fun onResponse(call: Call<Review>, response: Response<Review>) {
                if (response.isSuccessful) {
                    Log.d("ReviewRepository", "Review submitted successfully! Response: ${response.body()}")
                    onResult(response.body())
                } else {
                    Log.e("ReviewRepository", "Error Response Code: ${response.code()}, Body: ${response.errorBody()?.string()}")
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<Review>, t: Throwable) {
                Log.e("ReviewRepository", "API request failed: ${t.message}")
                onResult(null)
            }
        })
    }

    fun getReviewsByUser(userId: Int, onResult: (List<Review>?) -> Unit) {
        apiService.getReviewsByUser(userId).enqueue(object : Callback<List<Review>> {
            override fun onResponse(call: Call<List<Review>>, response: Response<List<Review>>) {
                if (response.isSuccessful) {
                    Log.d("ReviewRepository", "Fetched reviews successfully: ${response.body()}")
                    onResult(response.body())
                } else {
                    Log.e("ReviewRepository", "Error fetching reviews: ${response.code()}, Body: ${response.errorBody()?.string()}")
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<List<Review>>, t: Throwable) {
                Log.e("ReviewRepository", "API request failed: ${t.message}")
                onResult(null)
            }
        })
    }
}
