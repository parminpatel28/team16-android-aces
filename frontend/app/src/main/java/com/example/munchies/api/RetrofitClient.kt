//package com.example.munchies.api
//
//import com.example.munchies.model.Review
//import retrofit2.Call
//import retrofit2.http.*
//
//interface ReviewApiService {
//
//    // Fetch all reviews from a user
//    @GET("api/reviews/user/{userId}")
//    fun getReviewsByUser(@Path("userId") userId: Long): Call<List<Review>>
//
//    // Fetch all reviews for a restaurant
//    @GET("api/reviews/restaurant/{restaurantId}")
//    fun getReviewsByRestaurant(@Path("restaurantId") restaurantId: String): Call<List<Review>>
//
//    // Fetch reviews where the user is tagged
//    @GET("api/reviews/tagged/{userId}")
//    fun getTaggedReviews(@Path("userId") userId: Long): Call<List<Review>>
//
//    // Fetch review by ID
//    @GET("api/reviews/{reviewId}")
//    fun getReviewById(@Path("reviewId") reviewId: Long): Call<Review>
//
//    // Create a new review
//    @POST("api/reviews")
//    fun createReview(@Body review: Review): Call<Review>
//
//    // Delete review by ID
//    @DELETE("api/reviews/{reviewId}")
//    fun deleteReview(@Path("reviewId") reviewId: Long): Call<Void>
//}

package com.example.munchies.api

import com.example.munchies.model.UserProfile
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// Define the API service
interface ApiService {
    @POST("api/users")
    suspend fun createUserProfile(@Body userProfile: UserProfile): retrofit2.Response<UserProfile>
}

// Retrofit Client to create a Retrofit instance
object RetrofitClient {
    private const val BASE_URL = "http://localhost:8080/" // Change to your backend URL

    // Create the Retrofit instance
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Create the ApiService instance
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}

