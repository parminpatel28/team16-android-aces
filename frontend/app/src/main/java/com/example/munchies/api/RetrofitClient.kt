package com.example.munchies.api

import com.example.munchies.model.Review
import retrofit2.Call
import retrofit2.http.*

interface ReviewApiService {

    // Fetch all reviews from a user
    @GET("api/reviews/user/{userId}")
    fun getReviewsByUser(@Path("userId") userId: Long): Call<List<Review>>

    // Fetch all reviews for a restaurant
    @GET("api/reviews/restaurant/{restaurantId}")
    fun getReviewsByRestaurant(@Path("restaurantId") restaurantId: String): Call<List<Review>>

    // Fetch reviews where the user is tagged
    @GET("api/reviews/tagged/{userId}")
    fun getTaggedReviews(@Path("userId") userId: Long): Call<List<Review>>

    // Fetch review by ID
    @GET("api/reviews/{reviewId}")
    fun getReviewById(@Path("reviewId") reviewId: Long): Call<Review>

    // Create a new review
    @POST("api/reviews")
    fun createReview(@Body review: Review): Call<Review>

    // Delete review by ID
    @DELETE("api/reviews/{reviewId}")
    fun deleteReview(@Path("reviewId") reviewId: Long): Call<Void>
}
