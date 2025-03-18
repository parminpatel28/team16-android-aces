package com.example.munchies.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String,
    val name: String,
    val username: String,
    val profilePicture: String?,
    val userBio: String?,
    val accountCreationDate: String,
    val emailAddress: String,
    val location: String?,
    val savedReviews: List<Int> = emptyList(),
) : Parcelable
