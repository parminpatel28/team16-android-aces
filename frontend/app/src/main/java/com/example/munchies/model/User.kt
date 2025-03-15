package com.example.munchies.model

import android.location.Location
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.Instant

@Parcelize
data class User(
    val id: Int,
    val name: String,
    val username: String,
    val profilePicture: String?,
    val userBio: String?,
    val accountCreationDate: String,
    val emailAddress: String,
    val location: String?,
    val savedReviews: List<Int> = emptyList(),
) : Parcelable
