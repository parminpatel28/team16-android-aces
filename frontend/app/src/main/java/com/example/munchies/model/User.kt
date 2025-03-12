package com.example.munchies.model

data class UserProfile(
    val name: String,
    val username: String,
    val profilePicture: String,
    val userBio: String,
    val location_id: String,
    val emailAddress: String,
    val friends: Map<String, Any> = emptyMap(),
    val savedReviews: Map<String, Any> = emptyMap()
)

data class UserProfileResponse(
    val success: Boolean,
    val message: String
)

