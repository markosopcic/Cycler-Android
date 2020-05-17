package com.markosopcic.cycler.network.models

import java.util.*

data class FriendRequestResponse(
    val id: String,
    val sender: String,
    val senderName: String,
    val timeSent: Date
)