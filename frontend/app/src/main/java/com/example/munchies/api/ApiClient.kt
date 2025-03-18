package com.example.munchies.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

//    val reviewService: ReviewApiService by lazy {
//        retrofit.create(ReviewApiService::class.java)
//    }

    val userService: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }
}