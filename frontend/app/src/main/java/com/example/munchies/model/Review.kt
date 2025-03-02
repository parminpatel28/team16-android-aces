package com.example.munchies.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.Instant

@Parcelize
data class Review(
    val reviewID: Int,
    val poster: String, // should be a user, string for now
    val caption: String,
    val photos: List<String> = emptyList(),
    val taggedUsers: List<String> = emptyList(), // should be list of users, string for now
    val restaurants: List<String> = emptyList(), // should be restaurant list, string for now
    val location: String?, // should be location, string for now
    val date: Instant,
    val rating: Double,
    val likes: Int = 0,
//    val comments: List<Comment> = emptyList()
) : Parcelable
