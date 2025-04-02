package com.example.munchies.repository

import android.util.Log
import com.example.munchies.api.ApiClient
import com.example.munchies.model.Review
import com.google.android.libraries.places.api.model.kotlin.review
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

    suspend fun getReviewsByUser(userId: String): List<Review>? {
        return try {
            val response = apiService.getReviewsByUser(userId)
            if (response.isSuccessful) {
                Log.d("ReviewRepository", "Fetched reviews successfully: ${response.body()}")
                response.body()
            } else {
                Log.e("ReviewRepository", "Error fetching reviews: ${response.code()}, Body: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("ReviewRepository", "API request failed: ${e.message}", e)
            null
        }
    }

    fun requestPresignedUrl(fileName: String, reviewId: String, onUrlReceived: (String) -> Unit) {
        apiService.getPreSignedUrl(fileName, reviewId).enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    val presignedUrl = response.body()?.get("url") ?: ""
                    Log.d("ReviewRepository", "Pre-Signed URL: $presignedUrl")
                    onUrlReceived(presignedUrl)
                } else {
                    Log.e("ReviewRepository", "Failed to get pre-signed URL: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Log.e("S3 Upload", "Error fetching pre-signed URL", t)
            }
        })
    }

    fun updateReviewPhotos(reviewId: Int, photoUrls: List<String>) {
        apiService.updateReviewPhotos(reviewId, photoUrls).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("ReviewRepository", "Successfully updated review photos!")
                } else {
                    Log.e("ReviewRepository", "Failed to update photos. Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ReviewRepository", "Network error when updating photos: ${t.message}")
            }
        })
    }

    fun likeReview(reviewId: Int) {
        apiService.likeReview(reviewId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("ReviewRepository", "Successfully liked review!")
                } else {
                    Log.e("ReviewRepository", "Failed to like review. Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ReviewRepository", "Network error when liking review: ${t.message}")
            }
        })
    }

    fun dislikeReview(reviewId: Int) {
        apiService.dislikeReview(reviewId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("ReviewRepository", "Successfully removed like!")
                } else {
                    Log.e("ReviewRepository", "Failed to remove like. Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ReviewRepository", "Network error when liking review: ${t.message}")
            }
        })
    }

    suspend fun getReviewsByRestaurantId(restaurantId: String): List<Review>? {
        return try {
            val response = apiService.getReviewsByRestaurantId(restaurantId)
            if (response.isSuccessful) {
                Log.d("ReviewRepository", "Fetched reviews successfully: ${response.body()}")
                response.body()
            } else {
                Log.e("ReviewRepository", "Error fetching reviews: ${response.code()}, Body: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("ReviewRepository", "API request failed: ${e.message}", e)
            null
        }
    }

}
