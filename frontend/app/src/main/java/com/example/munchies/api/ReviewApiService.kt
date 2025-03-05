package com.example.munchies.api

import com.example.munchies.model.Review
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ReviewApiService {

    // Fetch review by ID
    @GET("api/reviews/{reviewId}")
    fun getReviewById(@Path("reviewId") reviewId: Long): Call<Review>

    // Create a new review
    // TODO: fix type mismatch and use Review object
//    @POST("api/reviews")
//    fun createReview(@Body review: Review): Call<Review>
    @POST("api/reviews")
    fun createReview(@Body requestBody: RequestBody): Call<Review>


    // Delete review by ID
    @DELETE("api/reviews/{reviewId}")
    fun deleteReview(@Path("reviewId") reviewId: Long): Call<Void>
}
