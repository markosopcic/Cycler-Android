package com.markosopcic.cycler.network.models

data class UserDetails(
    val dateJoined: String,
    val email: String,
    val firstName: String,
    val friendshipRequestId: Any,
    val friendshipRequestReceived: Boolean,
    val friendshipRequestSent: Boolean,
    val fullName: String,
    val id: String,
    val isFriend: Boolean,
    val lastLogin: String,
    val lastName: String,
    val numOfFriends: Int
)