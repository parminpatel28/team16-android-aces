package com.example.munchies.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Review(
    @SerializedName("id") val reviewID: Int?,
    @SerializedName("user") val user: User,
    @SerializedName("caption") val caption: String,
    @SerializedName("photos") val photos: List<String>? = emptyList(),
//    @SerializedName("location_id") val location: Location?,
    @SerializedName("date") val date: String,
    @SerializedName("rating") val rating: Double,
    @SerializedName("likes") val likes: Int = 0,
    @SerializedName("savedReviews") val savedReviews: List<Review>? = emptyList(),
//    val comments: List<Comment> = emptyList()
) : Parcelable
