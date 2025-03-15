package com.example.munchies.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.time.Instant

@Parcelize
data class Review(
    @SerializedName("review_id") val reviewID: Int,
    @SerializedName("user") val user: User,
    @SerializedName("caption") val caption: String,
    @SerializedName("photos") val photos: List<String>? = emptyList(),
    @SerializedName("location") val location: Location,
    @SerializedName("date") val date: String,  // Change Instant -> String for JSON compatibility
    @SerializedName("rating") val rating: Double,
    @SerializedName("likes") val likes: Int = 0,
//    val comments: List<Comment> = emptyList()
) : Parcelable
