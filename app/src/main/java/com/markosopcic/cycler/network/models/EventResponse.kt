package com.markosopcic.cycler.network.models

data class EventResponse(
    val description: String,
    val id: String,
    val name: String,
    val ownerId: String,
    val ownerName: String,
    val startTime: String
)