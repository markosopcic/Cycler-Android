package com.markosopcic.cycler.network.forms

data class APILocationModel(val Longitude: Double, val Latitude: Double, val TimeMillis: Long)

data class EventModel(
    val UserId: String,
    val EventId: String?,
    val StarTimeMillis: Long,
    val EndTimeMillis: Long,
    val Name: String,
    val OwnerId: String?,
    val Locations: List<APILocationModel>,
    val Duration: Long,
    val Meters: Long
)