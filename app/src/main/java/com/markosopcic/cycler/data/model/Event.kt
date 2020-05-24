package com.markosopcic.cycler.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDateTime

@Entity
data class Event(@PrimaryKey(autoGenerate = true) val localId : Int, val remoteId: String, val ownerId : String, val startTime : String, val name : String)