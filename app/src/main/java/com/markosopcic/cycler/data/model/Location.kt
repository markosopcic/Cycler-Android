package com.markosopcic.cycler.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Location(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    val eventId: Long,
    val userId: String,
    val longitude: Double,
    val latitude: Double,
    val time: Long
)