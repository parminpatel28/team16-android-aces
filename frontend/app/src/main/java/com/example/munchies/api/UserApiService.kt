package com.example.munchies.api

import com.example.munchies.model.Friendship
import com.example.munchies.model.User
import retrofit2.Call
import retrofit2.http.*

interface UserApiService {

    // Fetch user by ID
    @GET("api/user/{userId}")
    fun getUserById(@Path("userId") userId: String): Call<User>

    // Fetch all user
    @GET("api/user")
    fun getUsers(): Call<List<User>>

    // Delete user by ID
    @DELETE("api/user/{userId}")
    fun deleteUser(@Path("userId") userId: Int): Call<Void>


    // Fetch all user friends
    @GET("api/friendship/{userId}")
    fun getUserFriends(@Path("userId") userId: String): Call<List<User>>

    // Send a friend request to friendId
    @POST("api/friendship/{userId}/{friendId}")
    fun addUserFriend(@Path("userId") userId: Int, @Path("friendId") friendId: Int): Call<Friendship>

    // Accept a friend request from senderId
    @POST("api/friendship/{senderId}/{receiverId}/accept")
    fun acceptFriendRequest(@Path("senderId") userId: Int, @Path("receiverId") friendId: Int): Call<Void>

//    // Reject a friend request from senderId
//    @POST("api/friendship/{senderId}/{receiverId}/reject")
//    fun rejectFriendRequest(@Path("senderId") userId: Int, @Path("receiverId") friendId: Int): Call<Void>

    // Delete a friend/reject a friend request/cancel friend request
    @DELETE("api/friendship/{userId}/{friendId}")
    fun deleteUserFriend(@Path("userId") userId: Int, @Path("friendId") friendId: Int): Call<Void>
}
