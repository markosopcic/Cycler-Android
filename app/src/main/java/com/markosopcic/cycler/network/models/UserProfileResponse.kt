package com.markosopcic.cycler.network.models

data class UserProfileResponse(
    val dateJoined: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val numFriends: Int
)