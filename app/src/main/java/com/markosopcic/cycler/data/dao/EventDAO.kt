package com.markosopcic.cycler.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.markosopcic.cycler.data.model.Event

@Dao
interface EventDAO {

    @Insert
    fun addEvent(event : Event)

    @Delete
    fun deleteEvent(event : Event)

    @Query("select * from event where localId = :id")
    fun getEventById(id : Int) : Event

}