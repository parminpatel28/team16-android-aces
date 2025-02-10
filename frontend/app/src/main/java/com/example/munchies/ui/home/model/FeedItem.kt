package com.example.munchies.ui.home.model

data class FeedItem(
    val id: Int,
    val title: String,
    val content: String,
    val image: String? = null, // Add image field URL
    val profileImage: String? = null // Add profile image URL
)
