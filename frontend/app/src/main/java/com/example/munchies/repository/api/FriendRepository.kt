package com.example.munchies.repository

import android.util.Log
import com.example.munchies.api.ApiClient
import com.example.munchies.model.Friend
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.gson.Gson

class FriendRepository {

    private val apiService = ApiClient.userService

    fun getUserFriends(userId: Long, onResult: (Map<Int,Any>?) -> Unit) {
        val gson = Gson()

        Log.d("FriendRepository")

        apiService.getUserFriends(userId).enqueue(object : Callback<Map<Int,Any>> {
            override fun onResponse(call: Call<Map<Int, Any>>, response: Response<Map<Int, Any>>) {
                if (response.isSuccessful) {
                    Log.d("FriendRepository", "Friends gotten successfully! Response: ${response.body()}")
                    onResult(response.body())
                } else {
                    Log.e("FriendRepository", "Error Response Code: ${response.code()}, Body: ${response.errorBody()?.string()}")
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<Map<Int, Any>>, t: Throwable) {
                Log.e("FriendRepository", "API request failed: ${t.message}")
                onResult(null)
            }
        })

    }

    fun createFriend(userId: Long, friendId: Long, friendData: Map<String, Any>, onResult: (Friend?) -> Unit) {
        val gson = Gson()
        val json = gson.toJson(friendData)
        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

        Log.d("FriendRepository", "Final JSON Payload: $json")

        apiService.addUserFriend(userId, friendId).enqueue(object : Callback<Friend> {
            override fun onResponse(call: Call<Friend>, response: Response<Friend>) {
                if (response.isSuccessful) {
                    Log.d("FriendRepository", "Friend added successfully! Response: ${response.body()}")
                    onResult(response.body())
                } else {
                    Log.e("FriendRepository", "Error Response Code: ${response.code()}, Body: ${response.errorBody()?.string()}")
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<Friend>, t: Throwable) {
                Log.e("FriendRepository", "API request failed: ${t.message}")
                onResult(null)
            }
        })
    }
}
