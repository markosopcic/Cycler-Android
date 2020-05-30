package com.markosopcic.cycler.network.models

data class EventFeedResponse(
    val duration: Int,
    val endTime: String,
    val meters: Int,
    val name: String,
    val startTime: String,
    val user: String
)