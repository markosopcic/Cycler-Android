package com.markosopcic.cycler.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Event(
    @PrimaryKey(autoGenerate = true) val localId: Long?,
    val remoteId: String?,
    val ownerId: String?,
    val startTime: Long,
    val name: String,
    var Duration: Long,
    var Meters: Long
)

