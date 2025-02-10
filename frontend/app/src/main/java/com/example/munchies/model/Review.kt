package com.example.munchies.model

data class Review(
    val id: Int,
    val user: String,        // User who posted the review
    val restaurant: String,  // Restaurant being reviewed
    val content: String,     // Review text
    val image: String? = null,   // Optional image of food/restaurant
    val profileImage: String? = null // Optional profile image
    // TODO: Add ratings, timestamp/date, and other info??
)
