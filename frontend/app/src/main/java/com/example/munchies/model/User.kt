package com.example.munchies.model

import android.location.Location
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.time.Instant

@Parcelize
data class User(
    val id: String,
    val name: String,
    val username: String,
    val profilePicture: String?,
    val userBio: String?,
    val accountCreationDate: String,
    val emailAddress: String,
    val savedReviews: List<Review>? = emptyList(),
) : Parcelable
