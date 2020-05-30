package com.markosopcic.cycler.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.markosopcic.cycler.data.model.Location


@Dao
interface LocationDAO {
    @Insert
    fun AddLocation(location: Location)

    @Query("select * from Location where eventId = :id")
    fun GetLocationsForEvent(id: Long): List<Location>

    @Delete
    fun RemoveLocations(locations: List<Location>)
}