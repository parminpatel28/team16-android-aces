package com.example.munchies.api

import com.example.munchies.model.Friend
import com.example.munchies.model.User
import retrofit2.Call
import retrofit2.http.*

interface UserApiService {

    // Fetch user by ID
    @GET("api/user/{userId}")
    fun getUserById(@Path("userId") userId: Long): Call<User>

    // Fetch all user
    @GET("api/user")
    fun getUsers(): Call<List<User>>

    // Fetch all user
    @GET("api/user/friend")
    fun getUserFriends(userId: Long): Call<Map<Int, Any>>

    // Create a new friend for user
    @POST("api/user/{userId}/friend/{friendId}")
    fun addUserFriend(@Path("userId") userId: Long, @Path("friendId") friendId: Long): Call<Friend>

    // Delete user by ID
    @DELETE("api/user/{userId}")
    fun deleteUser(@Path("userId") userId: Long): Call<Void>
}