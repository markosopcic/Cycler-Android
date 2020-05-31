package com.markosopcic.cycler.network.models

data class UserEventData(
    val userId : String,
    val durationSeconds: Int,
    val meters: Int,
    val userName: String
)

data class UserEventViewResponse(
    val accepted: Int,
    val description: Any,
    val endTime: String,
    val finished: Boolean,
    val id: String,
    val invited: Int,
    val name: String,
    val ownerId: String,
    val ownerName: String,
    val `private`: Boolean,
    val startTime: String,
    val userEventData: List<UserEventData>,
    val userIds: Any
)