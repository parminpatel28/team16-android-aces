package com.example.munchies.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

enum class FriendRequestStatus {
    PENDING,
    ACCEPTED,
    REJECTED
}

@Parcelize
data class Friendship(
    val userId: Int,
    val friendId: Int,
    val status: FriendRequestStatus,
    val updatedAt: LocalDateTime,
) : Parcelable
