package com.example.munchies.api

import com.example.munchies.model.Review
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.io.Serializable

data class CreateUserRequest(
    val firebaseUserId: String,
    val name: String,
    val username: String,
    val profilePicture: String?,
    val userBio: String?,
    val emailAddress: String
)

data class User(
    val id: String,
    val name: String,
    val username: String,
    val profilePicture: String?,
    val userBio: String?,
    val accountCreationDate: String,
    val emailAddress: String,
    val savedReviews: List<Review>? = emptyList(),
)

interface UserService {

    @POST("api/user")
    suspend fun createUser(@Body request: CreateUserRequest): Response<User>

    @GET("api/user/{id}")
    suspend fun getUserById(@Path("id") id: String): Response<User>

    @PUT("api/user/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body request: CreateUserRequest): Response<User>
} 