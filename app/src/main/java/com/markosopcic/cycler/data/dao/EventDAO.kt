package com.markosopcic.cycler.data.dao

import androidx.room.*
import com.markosopcic.cycler.data.model.Event

@Dao
interface EventDAO {

    @Insert
    fun addEvent(event : Event) : Long

    @Delete
    fun deleteEvent(event : Event)

    @Query("select * from event where localId = :id")
    fun getEventById(id : Long) : Event

    @Update
    fun updateEvent(event : Event)

}