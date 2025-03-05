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

    fun createReview(reviewData: Map<String, Any>, onResult: (Review?) -> Unit) {
        val gson = Gson()
        val json = gson.toJson(reviewData)
        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

        Log.d("ReviewRepository", "Final JSON Payload: $json")

        apiService.createReview(requestBody).enqueue(object : Callback<Review> {
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
}
