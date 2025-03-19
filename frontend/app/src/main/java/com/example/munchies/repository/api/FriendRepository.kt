package com.example.munchies.repository

import android.util.Log
import com.example.munchies.api.ApiClient
import com.example.munchies.model.Friend
import com.example.munchies.model.Friendship
import com.example.munchies.model.User
import com.example.munchies.model.UserManager
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

    fun getIncomingFriendRequests(userId: String, onResult: (List<User>?) -> Unit) {
        Log.d("FriendRepository", "Getting incoming friend requests for user $userId")
        apiService.getIncomingRequests(userId).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    Log.d("FriendRepository", "Incoming requests gotten successfully! Response: ${response.body()}")
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

    fun getOutgoingFriendRequests(userId: String, onResult: (List<User>?) -> Unit) {
        Log.d("FriendRepository", "Getting outgoing friend requests for user $userId")
        apiService.getOutgoingRequests(userId).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    Log.d("FriendRepository", "Outgoing requests gotten successfully! Response: ${response.body()}")
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


    fun addFriend(userId: String, friendId: String, onResult: (Void?) -> Unit) {
        val gson = Gson()

        Log.d("FriendRepository", "Adding friend $friendId to user $userId")

        apiService.addUserFriend(userId, friendId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("FriendRepository", "Friend request sent successfully! Response: ${response.body()}")
                    onResult(response.body())
                } else {
                    Log.e("FriendRepository", "Error Response Code: ${response.code()}, Body: ${response.errorBody()?.string()}")
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("FriendRepository", "API request failed: ${t.message}")
                onResult(null)
            }
        })
    }

    fun acceptFriendRequest(userId: String, friendId: String, onResult: (Void?) -> Unit) {
        apiService.acceptFriendRequest(userId, friendId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("FriendRepository", "Friend accepted successfully! Response: ${response.body()}")
                    onResult(response.body())
                } else {
                    Log.e("FriendRepository", "Error Response Code: ${response.code()}, Body: ${response.errorBody()?.string()}")
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("FriendRepository", "API request failed: ${t.message}")
                onResult(null)
            }
        })
    }

    fun deleteFriend(userId: String, friendId: String, onResult: (Void?) -> Unit) {
        apiService.deleteUserFriend(userId, friendId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("FriendRepository", "Friend removed successfully! Response: ${response.body()}")
                    onResult(response.body())
                } else {
                    Log.e("FriendRepository", "Error Response Code: ${response.code()}, Body: ${response.errorBody()?.string()}")
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("FriendRepository", "API request failed: ${t.message}")
                onResult(null)
            }
        })
    }

    fun fetchUserById(userId: String, onResult: (User?) -> Unit) {
        val gson = Gson()

        Log.d("FriendRepository", "Fetching user $userId")

        apiService.getUserById(userId).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Log.d("FriendRepository", "User fetched successfully! Response: ${response.body()}")
                    UserManager.currentUser = response.body()
                    onResult(response.body())
                } else {
                    Log.e("FriendRepository", "Error Response Code: ${response.code()}, Body: ${response.errorBody()?.string()}")
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("FriendRepository", "API request failed: ${t.message}")
                onResult(null)
            }
        })

    }
}
