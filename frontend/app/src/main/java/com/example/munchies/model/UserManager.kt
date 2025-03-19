package com.example.munchies.model

object UserManager {
    var currentUser: User? = null
    var friends: List<User>? = null
    var incomingFriendRequests: List<User>? = null
    var outgoingFriendRequests: List<User>? = null
}