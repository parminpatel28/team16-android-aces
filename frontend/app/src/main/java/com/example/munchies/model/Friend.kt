package com.example.munchies.model

import android.graphics.Picture
import android.os.Parcelable
import android.os.UserHandle
import kotlinx.parcelize.Parcelize
import java.time.Instant
import java.time.LocalDateTime

@Parcelize
data class Friend(
    val userId: Int,
    val name: String,
    val username: String,
    val profilePicture: String,
) : Parcelable
