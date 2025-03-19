package com.example.munchies.repository

import android.util.Log
import com.example.munchies.api.ApiClient
import com.example.munchies.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.gson.Gson

class UserRepository {

    private val apiService = ApiClient.userService

    fun getUserById(userId: String, onResult: (User?) -> Unit) {

        Log.d("UserRepository", "Getting user $userId")

        apiService.getUserById(userId).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Log.d("UserRepository", "User gotten successfully! Response: ${response.body()}")
                    onResult(response.body())
                } else {
                    Log.e("UserRepository", "Error Response Code: ${response.code()}, Body: ${response.errorBody()?.string()}")
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("UserRepository", "API request failed: ${t.message}")
                onResult(null)
            }
        })

    }

    fun getAllUsers(onResult: (List<User>?) -> Unit) {
        Log.d("UserRepository", "Getting all users")

        apiService.getUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    Log.d("UserRepository", "Users gotten successfully! Response: ${response.body()}")
                    onResult(response.body())
                } else {
                    Log.e("UserRepository", "Error Response Code: ${response.code()}, Body: ${response.errorBody()?.string()}")
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e("UserRepository", "API request failed: ${t.message}")
                onResult(null)
            }
        })

    }
}
