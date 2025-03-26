package com.example.munchies.api

import com.example.munchies.model.Review
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ReviewApiService {

    // Fetch review by ID
    @GET("api/reviews/{reviewId}")
    fun getReviewById(@Path("reviewId") reviewId: Long): Call<Review>

    // Fetch reviews by user
    @GET("api/reviews/user/{userId}")
    suspend fun getReviewsByUser(@Path("userId") userId: String): Response<List<Review>>

    // Create a new review
    @POST("api/reviews")
    fun createReview(@Body review: Review): Call<Review>

    @GET("/api/s3/presigned-url")
    fun getPreSignedUrl(@Query ("fileName") fileName: String, @Query ("reviewId") reviewId: String): Call<Map<String, String>>

    @PUT("api/reviews/{reviewId}/photos")
    fun updateReviewPhotos(
        @Path("reviewId") reviewId: Int,
        @Body photoUrls: List<String>
    ): Call<Void>

    // Delete review by ID
    @DELETE("api/reviews/{reviewId}")
    fun deleteReview(@Path("reviewId") reviewId: Long): Call<Void>
}
