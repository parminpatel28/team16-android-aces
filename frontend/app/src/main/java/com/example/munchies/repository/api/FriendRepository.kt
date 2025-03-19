package com.example.munchies.repository

import android.util.Log
import com.example.munchies.api.ApiClient
import com.example.munchies.model.Friend
import com.example.munchies.model.Friendship
import com.example.munchies.model.User
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.gson.Gson

class FriendRepository {

    private val apiService = ApiClient.userService

    fun getUserFriends(userId: String, onResult: (List<User>?) -> Unit) {

        Log.d("FriendRepository", "Getting friends for user $userId")

        apiService.getUserFriends(userId).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    Log.d("FriendRepository", "Friends gotten successfully! Response: ${response.body()}")
                    onResult(response.body())
                } else {
                    Log.e("FriendRepository", "Error Response Code: ${response.code()}, Body: ${response.errorBody()?.string()}")
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e("FriendRepository", "API request failed: ${t.message}")
                onResult(null)
            }
        })

    }

    fun createFriend(userId: String, friendId: Int, onResult: (Friendship?) -> Unit) {
        val gson = Gson()

        Log.d("FriendRepository", "Adding friend $friendId to user $userId")

        apiService.addUserFriend(userId, friendId).enqueue(object : Callback<Friendship> {
            override fun onResponse(call: Call<Friendship>, response: Response<Friendship>) {
                if (response.isSuccessful) {
                    Log.d("FriendRepository", "Friend request sent successfully! Response: ${response.body()}")
                    onResult(response.body())
                } else {
                    Log.e("FriendRepository", "Error Response Code: ${response.code()}, Body: ${response.errorBody()?.string()}")
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<Friendship>, t: Throwable) {
                Log.e("FriendRepository", "API request failed: ${t.message}")
                onResult(null)
            }
        })
    }
}
