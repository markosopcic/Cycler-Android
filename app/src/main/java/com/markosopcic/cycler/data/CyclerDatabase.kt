package com.markosopcic.cycler.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.markosopcic.cycler.data.dao.EventDAO
import com.markosopcic.cycler.data.dao.LocationDAO
import com.markosopcic.cycler.data.model.Event
import com.markosopcic.cycler.data.model.Location

@Database(version = 1, entities = arrayOf(Event::class,Location::class), exportSchema = false)
abstract class CyclerDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDAO

    abstract fun locationDao(): LocationDAO

    companion object {
        @Volatile
        private var INSTANCE: CyclerDatabase? = null

        fun getDatabase(context: Context): CyclerDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CyclerDatabase::class.java,
                    "cycler_database"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }

    }
}